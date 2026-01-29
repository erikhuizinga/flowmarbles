package com.github.rougsig.flowmarbles.operators

import com.github.rougsig.flowmarbles.component.timeline.Marble

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
fun hotFlowOperators() = listOf(
  menuHeader("hot"),
  menuItem(
    label("shareIn"),
    sandbox(
      "shareIn",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("C", 300),
          marble("D", 600)
        )
      ),
      "shareIn(scope, WhileSubscribed(), replay = 1)"
    ) { inputs ->
      channelFlow {
        val shared = inputs[0].shareIn(this, SharingStarted.WhileSubscribed(), replay = 1)
        launch {
          shared.collect { value ->
            send(marble("1${value.value}", 0, Colors.colors[0]))
          }
        }
        launch {
          delay(350)
          shared.collect { value ->
            send(marble("2${value.value}", 0, Colors.colors[1]))
          }
        }
      }
    }
  ),
  menuItem(
    label("stateIn"),
    sandbox(
      "stateIn",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("C", 300),
          marble("D", 600)
        )
      ),
      "stateIn(scope, WhileSubscribed(), \"I\")"
    ) { inputs ->
      channelFlow {
        val initial = marble("I", 0, Colors.accentColors[0])
        val state = inputs[0].stateIn(this, SharingStarted.WhileSubscribed(), initial)
        launch {
          state.collect { value ->
            send(marble("1${value.value}", 0, Colors.colors[0]))
          }
        }
        launch {
          delay(350)
          state.collect { value ->
            send(marble("2${value.value}", 0, Colors.colors[1]))
          }
        }
      }
    }
  ),
  menuItem(
    label("mutableSharedFlow"),
    sandbox(
      "MutableSharedFlow",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("C", 300),
          marble("D", 600)
        )
      ),
      "MutableSharedFlow(replay = 1)"
    ) { inputs ->
      channelFlow {
        val shared = MutableSharedFlow<Marble.Model<String>>(replay = 1)
        launch {
          inputs[0].collect { value ->
            shared.emit(value)
          }
        }
        launch {
          shared.collect { value ->
            send(marble("1${value.value}", 0, Colors.colors[0]))
          }
        }
        launch {
          delay(350)
          shared.collect { value ->
            send(marble("2${value.value}", 0, Colors.colors[1]))
          }
        }
      }
    }
  ),
  menuItem(
    label("mutableStateFlow"),
    sandbox(
      "MutableStateFlow",
      inputs(
        input(
          marble("A", 0),
          marble("B", 150),
          marble("C", 300),
          marble("D", 600)
        )
      ),
      "MutableStateFlow(\"I\")"
    ) { inputs ->
      channelFlow {
        val state = MutableStateFlow(marble("I", 0, Colors.accentColors[0]))
        launch {
          inputs[0].collect { value ->
            state.value = value
          }
        }
        launch {
          state.collect { value ->
            send(marble("1${value.value}", 0, Colors.colors[0]))
          }
        }
        launch {
          delay(350)
          state.collect { value ->
            send(marble("2${value.value}", 0, Colors.colors[1]))
          }
        }
      }
    }
  )
)
