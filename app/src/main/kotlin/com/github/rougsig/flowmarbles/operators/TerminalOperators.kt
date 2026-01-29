package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.all
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
fun terminalOperators() = listOf(
  menuHeader("terminal"),
  menuItem(
    label("any"),
    sandbox(
      "any",
      inputs(
        input(
          marble(1, 0),
          marble(2, 150),
          marble(5, 300),
          marble(3, 450),
          marble(4, 600)
        )
      ),
      "any { it > 4 }"
    ) { inputs ->
      flow {
        val hasMatch = inputs[0].any { it.value > 4 }
        val output = if (hasMatch) "T" else "F"
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
          marble(1, 0),
          marble(2, 150),
          marble(3, 300),
          marble(5, 600)
        )
      ),
      "all { it < 5 }"
    ) { inputs ->
      flow {
        val allMatch = inputs[0].all { it.value < 5 }
        val output = if (allMatch) "T" else "F"
        emit(marble(output, 0, Colors.accentColors[0]))
      }
    }
  )
)
