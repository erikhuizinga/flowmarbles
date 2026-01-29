package com.github.rougsig.flowmarbles.operators

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
val operators = listOf(
  batchOperators(),
  contextOperators(),
  delayOperators(),
  distinctOperators(),
  emittersOperators(),
  errorOperators(),
  hotFlowOperators(),
  limitOperators(),
  mergeOperators(),
  terminalOperators(),
  transformOperators(),
  zipOperators()
).flatten()
