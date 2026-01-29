package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy

@FlowPreview
@ExperimentalCoroutinesApi
fun distinctOperators() = listOf(
  menuHeader("distinct"),
  menuItem(
    label("distinctUntilChanged"),
    sandbox(
      "distinctUntilChanged",
      inputs(
        input(
          marble("1", 0, color = Colors.colors[0]),
          marble("1", 150, color = Colors.colors[0]),
          marble("2", 300, color = Colors.colors[1]),
          marble("2", 450, color = Colors.colors[1]),
          marble("3", 600, color = Colors.colors[2]),
          marble("3", 750, color = Colors.colors[2]),
          marble("4", 900, color = Colors.colors[3])
        )
      ),
      "distinctUntilChanged()"
    ) { inputs -> inputs[0].distinctUntilChanged { old, new -> old.value == new.value } }
  ),
  menuItem(
    label("distinctUntilChangedBy"),
    sandbox(
      "distinctUntilChangedBy",
      inputs(
        input(
          marble("A1", 0, color = Colors.colors[0]),
          marble("A2", 150, color = Colors.colors[0]),
          marble("B1", 300, color = Colors.colors[1]),
          marble("B2", 450, color = Colors.colors[1]),
          marble("B3", 600, color = Colors.colors[1]),
          marble("C1", 750, color = Colors.colors[2]),
          marble("D1", 900, color = Colors.colors[3])
        )
      ),
      "distinctUntilChangedBy { it.value.first() }"
    ) { inputs ->
      inputs[0].distinctUntilChangedBy { it.value.first() }
    }
  )
)
