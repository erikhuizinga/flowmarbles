package com.github.rougsig.flowmarbles.extensions

fun String.toCamelKebabCase(): String {
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
