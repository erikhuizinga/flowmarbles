package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.all
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.none
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
fun logicalOperators() = listOf(
  menuHeader("logical"),
  menuItem(
    label("any"),
    sandbox(
      "any",
      inputs(
        input(
          marble("1", 0),
          marble("2", 150),
          marble("5", 300),
          marble("3", 450),
          marble("4", 600)
        )
      ),
      "any { it > 4 }"
    ) { inputs ->
      flow {
        val hasMatch = inputs[0].any { it.value.toInt() > 4 }
        val output = if (hasMatch) "true" else "false"
        emit(marble(output, 0, Colors.accentColors[0]))
      }
    }
  ),
  menuItem(
    label("all"),
    sandbox(
      "all",
      inputs(
        input(
          marble("1", 0),
          marble("2", 150),
          marble("3", 300),
          marble("5", 600)
        )
      ),
      "all { it < 5 }"
    ) { inputs ->
      flow {
        val allMatch = inputs[0].all { it.value.toInt() < 5 }
        val output = if (allMatch) "true" else "false"
        emit(marble(output, 0, Colors.accentColors[0]))
      }
    }
  ),
  menuItem(
    label("none"),
    sandbox(
      "none",
      inputs(
        input(
          marble("1", 0),
          marble("2", 150),
          marble("4", 300)
        )
      ),
      "none { it == 3 }"
    ) { inputs ->
      flow {
        val noneMatch = inputs[0].none { it.value.toInt() == 3 }
        val output = if (noneMatch) "true" else "false"
        emit(marble(output, 0, Colors.accentColors[0]))
      }
    }
  )
)
