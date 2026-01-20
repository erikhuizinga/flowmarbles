package com.github.rougsig.kotlindocs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val BASE_DOCS_URL = "https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/"

fun main() {
  embeddedServer(Netty, port = 8000) {
    routing {
      get("/{page}") {
        try {
          val page = call.parameters["page"]!!
          val pageName = page.toCamelKebabCase()
          val uri = URI(BASE_DOCS_URL)
          val doc = getDocsHtml(pageName)
          println("$BASE_DOCS_URL$pageName.html")

          val pageContents = doc.select("#content, .content")
          if (pageContents.isEmpty()) error("Content not found for: '$page'")
          pageContents.select("a").forEach { link ->
            link.attr("target", "_blank")
            val href = link.attr("href")
            val absoluteHref = uri.resolve(href.takeWhile { it != '#' }).toString()
            link.attr("href", absoluteHref + href.dropWhile { it != '#' })
          }
          val template = Jsoup.parse(Resource.read("/template.html"))
          val root = template.selectFirst("body") ?: error("Template body element missing.")
          pageContents.forEach { root.appendChild(it) }
          root.select(".top-right-position").remove()
          call.respondText(template.html(), ContentType.Text.Html)
        } catch (e: Exception) {
          val error = Jsoup.parse(Resource.read("/error.html"))
          call.respondText(error.html(), ContentType.Text.Html)
        }
      }
    }
  }.start(wait = true)
}

private suspend fun getDocsHtml(pageName: String): Document = withContext(Dispatchers.IO) {
  suspendCoroutine { cont ->
    try {
      val local = Resource.readOrNull("/docs/$pageName.html")
      if (local != null) {
        cont.resume(Jsoup.parse(local))
        return@suspendCoroutine
      }
      cont.resume(Jsoup.connect("$BASE_DOCS_URL$pageName.html").get())
    } catch (e: Throwable) {
      cont.resumeWithException(e)
    }
  }
}

private fun String.toCamelKebabCase(): String {
  val builder = StringBuilder()
  this.forEach {
    if (it.isUpperCase()) {
      builder.append("-")
      builder.append(it.lowercaseChar())
    } else {
      builder.append(it)
    }
  }
  return builder.toString()
}

private object Resource {
  fun read(name: String): String {
    return Resource::class.java.getResource(name)!!.readText()
  }

  fun readOrNull(name: String): String? {
    return Resource::class.java.getResource(name)?.readText()
  }
}
