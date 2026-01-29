package com.github.rougsig.flowmarbles.component.kotlindocs

import com.github.rougsig.flowmarbles.core.Component
import com.github.rougsig.flowmarbles.core.html
import com.github.rougsig.flowmarbles.extensions.toCamelKebabCase

private const val DOCS_BASE_URL = "/docs/"
private const val KOTLIN_DOCS_URL = "https://kotlinlang.org/api/kotlinx.coroutines/"
private const val DOCS_PACKAGE_PATH = "kotlinx-coroutines-core/kotlinx.coroutines.flow/"

class KotlinDocs : Component {
  fun setModel(model: String) {
    content.setAttribute("height", "0")
    content.setAttribute("src", "$DOCS_BASE_URL$model")
    linkToOriginal.setAttribute(
      "href",
      "${KOTLIN_DOCS_URL}${docsPath(model)}"
    )
  }

  private val linkToOriginal = html("a") {
    attr("class", "docs_original")
    attr("target", "_blank")
    text = "(original)"
  }
  private val docsSource = html("p") {
    attr("class", "docs_source")
    tag("span") {
      text = "Docs provided by "
    }
    tag("a") {
      attr("target", "_blank")
      attr("href", KOTLIN_DOCS_URL)
      text = "Kotlin docs"
    }
    element(linkToOriginal)
  }
  private val content = html("iframe") {
    attr("class", "docs_content")
    attr("width", "100%")
    attr("onLoad", "calcIframeHeight(this);")
  }

  override val rootNode = html("div") {
    attr("class", "docs")
    element(docsSource)
    element(content)
  }
}

private fun isTypeName(value: String): Boolean {
  return value.firstOrNull()?.isUpperCase() == true
}

private fun docsSlug(value: String): String {
  return if (isTypeName(value)) "-${value.toCamelKebabCase()}" else value.toCamelKebabCase()
}

private fun docsPath(value: String): String {
  val slug = docsSlug(value)
  return if (isTypeName(value)) "${DOCS_PACKAGE_PATH}${slug}/" else "${DOCS_PACKAGE_PATH}${slug}.html"
}
