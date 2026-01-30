package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.transform

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
  ),
  menuItem(
    label("retry"),
    sandbox(
      "retry",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("X", 300),
          marble("C", 450)
        )
      ),
      "first = true<br>transform {<br>&nbsp;&nbsp;&nbsp;&nbsp;if (it == \"X\" && first) { first = false; error(\"boom\") }<br>&nbsp;&nbsp;&nbsp;&nbsp;emit(it)<br>}<br>.retry(1)"
    ) { inputs ->
      var hasErrored = false
      inputs[0]
        .transform { value ->
          if (!hasErrored && value.value == "X") {
            hasErrored = true
            error("boom")
          }
          emit(value)
        }
        .retry(1)
    }
  ),
  menuItem(
    label("retryWhen"),
    sandbox(
      "retryWhen",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("X", 300),
          marble("C", 450)
        )
      ),
      "attempt = 0<br>transform {<br>&nbsp;&nbsp;&nbsp;&nbsp;attempt++<br>&nbsp;&nbsp;&nbsp;&nbsp;if (it == \"X\" && attempt == 1) error(\"boom\")<br>&nbsp;&nbsp;&nbsp;&nbsp;emit(it)<br>}<br>.retryWhen { _, attempt -> delay(200); attempt < 1 }"
    ) { inputs ->
      var attempt = 0
      flow {
        attempt += 1
        inputs[0].collect { value ->
          if (value.value == "X" && attempt == 1) error("boom")
          emit(value)
        }
      }.retryWhen { _, retryAttempt ->
        if (retryAttempt == 0L) {
          delay(200)
          true
        } else {
          false
        }
      }
    }
  )
)
