@file:Suppress("unused")

package com.verges.smartsensors

val Int.odd: Boolean
    get() = this % 2 != 0

val Int.even: Boolean
    get() = this % 2 == 0
