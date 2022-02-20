package com.verges.smartsensors

import java.util.*

object BleGattAttributes {
    private val attributes = HashMap<UUID, String>()

    //private const val UNKNOWN_UUID = "Unknown UUID"

    val SERVICE_GENERIC_ACCESS = "1800".uuid16toUuid128()
    val SERVICE_GENERIC_ATTRIBUTE = "1801".uuid16toUuid128()

    val CHARACTERISTIC_ENV_SENSING = "181A".uuid16toUuid128()
    val CHARACTERISTIC_GENERIC_LEVEL = "2AF9".uuid16toUuid128()

    val CLIENT_CHARACTERISTIC_CONFIG = "2902".uuid16toUuid128()
    val CLIENT_CHARACTERISTIC_FORMAT = "2904".uuid16toUuid128()

    //fun lookup(uuid: UUID, defaultName: String = UNKNOWN_UUID): String = attributes[uuid] ?: defaultName

    init {
        attributes[SERVICE_GENERIC_ACCESS] = "Generic Access"
        attributes[SERVICE_GENERIC_ATTRIBUTE] = "Generic Attribute"
        attributes[CLIENT_CHARACTERISTIC_CONFIG] = "Client Characteristic Configuration"
        attributes[CLIENT_CHARACTERISTIC_FORMAT] = "Characteristic Presentation Format"

        attributes[CHARACTERISTIC_ENV_SENSING] = "Environmental Sensing"
        attributes[CHARACTERISTIC_GENERIC_LEVEL] = "Generic Level"
    }
}
