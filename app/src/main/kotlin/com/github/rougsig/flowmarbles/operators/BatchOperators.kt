package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.chunked
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun batchOperators() = listOf(
  menuHeader("batch"),
  menuItem(
    label("chunked"),
    sandbox(
      "chunked",
      inputs(
        input(
          marble("1", 0),
          marble("2", 150),
          marble("3", 300),
          marble("4", 450),
          marble("5", 600),
          marble("6", 750)
        )
      ),
      "chunked(2)"
    ) { inputs ->
      inputs[0].chunked(2).map { chunk ->
        marble(chunk.joinToString("") { it.value }, 0, Colors.accentColors[0])
      }
    }
  )
)
