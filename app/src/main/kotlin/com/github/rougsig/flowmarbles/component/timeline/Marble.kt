package com.github.rougsig.flowmarbles.component.timeline

import com.github.rougsig.flowmarbles.core.Component
import com.github.rougsig.flowmarbles.core.svg
import kotlin.math.max

class Marble<T : Any>(model: Model<T>, posY: Long = 1) : Component {
  private val displayValue = model.value.toString()
  private val isPill = displayValue.startsWith("[") && displayValue.endsWith("]")
  private val pillTextPadding = 30
  private val pillWidth = max(50, displayValue.length * 16 + pillTextPadding)
  private val pillHalfWidth = pillWidth / 2.0

  data class Model<T : Any>(
    val color: String,
    val time: Long,
    val value: T
  ) {
    @Suppress("UNCHECKED_CAST")
    operator fun plus(other: Model<T>): Model<T> {
      val newValue: T = if (this.value as? Int != null && other.value as? Int != null) {
        (value + other.value) as T
      } else if (this.value as? String != null && other.value as? String != null) {
        (this.value + other.value) as T
      } else {
        error("unknown value type: $value. Expected String or Int.")
      }

      return Model(
        other.color,
        time + other.time,
        newValue
      )
    }
  }

  private val shape = if (isPill) {
    svg("rect") {
      attr("fill", model.color)
      attr("width", "$pillWidth")
      attr("height", "50")
      attr("x", "${-pillHalfWidth}")
      attr("y", "-25")
      attr("rx", "25")
      attr("ry", "25")
      attr("stroke", "black")
      attr("stroke-width", "3.5")
    }
  } else {
    svg("circle") {
      attr("fill", model.color)
      attr("r", "25")
      attr("stroke", "black")
      attr("stroke-width", "3.5")
    }
  }

  private val value = svg("text") {
    attr("style", "user-select: none; font-size: 25px; font-family: 'Roboto Mono', monospace;")
    attr("text-anchor", "middle")
    attr("y", "8")
    text = displayValue
  }

  override val rootNode = svg("g") {
    attr("transform", "translate(${model.time}, ${posY * 5})")
    element(shape)
    element(value)
  }
}
