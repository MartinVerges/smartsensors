@file:Suppress("unused")

package com.verges.smartsensors

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.math.BigInteger
import java.util.*
import kotlin.math.pow

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

val Int.odd: Boolean
    get() = this % 2 != 0

val Int.even: Boolean
    get() = this % 2 == 0


fun Int.pow(i: Int): Int {
    return toDouble().pow(i).toInt()
}

private val i1s64: BigInteger = BigInteger.ONE.shiftLeft(64) // 2^64

// Convert a UUID into a BigInteger to for calculation
fun UUID.toBigInteger(): BigInteger {
    var lo: BigInteger = BigInteger.valueOf(this.leastSignificantBits)
    var hi: BigInteger = BigInteger.valueOf(this.mostSignificantBits)

    // If any of lo/hi parts is negative interpret as unsigned
    if (hi.signum() < 0) hi = hi.add(i1s64)
    if (lo.signum() < 0) lo = lo.add(i1s64)
    return lo.add(hi.multiply(i1s64))
}

// Convert a BigInteger to the corresponding UUID representation
fun BigInteger.toUuid(): UUID {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val maxLong: BigInteger = BigInteger.valueOf(Long.MAX_VALUE)
        val parts: Array<BigInteger> = this.divideAndRemainder(i1s64)
        if (maxLong < parts[1]) parts[1] = parts[1].subtract(i1s64)
        if (maxLong < parts[0]) parts[0] = parts[0].subtract(i1s64)
        UUID(parts[0].longValueExact(), parts[1].longValueExact())
    } else {
        UUID.fromString(
            this.toString(16)
                .padStart(32, '0')
                .replace("^(.{8})(.{4})(.{4})(.{4})(.{12})$".toRegex(), "$1-$2-$3-$4-$5")
        )
    }
}

// Convert a BLE 16 bit HexDec UUID to a valid BLE 128 bit UUID
fun String.uuid16toUuid128(bleBase: BigInteger? = null): UUID {
    val base: BigInteger = bleBase ?: "00000000-0000-1000-8000-00805F9B34FB".toUuid().toBigInteger()
    return this.toBigInteger(16)
        .multiply(2.toBigInteger().pow(96))
        .add(base)
        .toUuid()
}
fun String.uuid16toUuid128(bleBase: String?): UUID = uuid16toUuid128(bleBase?.toUuid()?.toBigInteger())
fun String.uuid16toUuid128(bleBase: UUID?): UUID  = uuid16toUuid128(bleBase?.toBigInteger())

// Convert a string to UUID
fun String.toUuid(): UUID = UUID.fromString(this)
