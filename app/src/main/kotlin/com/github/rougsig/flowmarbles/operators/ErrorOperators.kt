package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun errorOperators() = listOf(
  menuHeader("errors"),
  menuItem(
    label("catch"),
    sandbox(
      "catch",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("X", 300),
          marble("C", 450)
        )
      ),
      "map { if (it == \"X\") error(\"boom\") }.catch { emit(\"E\") }"
    ) { inputs ->
      inputs[0]
        .map { value ->
          if (value.value == "X") error("boom")
          value
        }
        .catch { emit(marble("E", 0, Colors.accentColors[0])) }
    }
  )
)
