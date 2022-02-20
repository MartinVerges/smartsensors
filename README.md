[![Create Firmware](https://github.com/MartinVerges/smartsensors/actions/workflows/create-firmware.yml/badge.svg)](https://github.com/MartinVerges/smartsensors/actions/workflows/create-firmware.yml)
[![Create Release](https://github.com/MartinVerges/smartsensors/actions/workflows/create-release.yml/badge.svg)](https://github.com/MartinVerges/smartsensors/actions/workflows/create-release.yml)

# Smart Sensors Android App

This Android APP is used to connect a current Android phone with a smart sensor via Bluetooth Low Energy (BLE).
This project is still in development, but it is in a usable state by now.

## Why?

I wanted to learn Android development and use my ESP32 Bluetooth connectivity.

## Supported Sensors

The following sensors are currently supported:

 * [rv-smart-tanklevel](https://github.com/MartinVerges/rv-smart-tanksensor) (a Tank level indicator)

## Show me how it looks

#### Screenshots from the mobile UI
<img src="screenshots/scan_list.jpg?raw=true" alt="Device Scan List" width="20%"><img src="screenshots/tank_level.jpg?raw=true" alt="Tank level live view" width="20%">

## How to build this Android Project

The best way to use it, by opening it with [Android Stuido](https://developer.android.com/studio).
If you want to run it on the command line, please proceed as follows:

1. Install Java runtime 11 or newer
2. Run these commands:

```
    > ./gradlew build
```

# License

Smart Sensor App (c) by Martin Verges.

Smart Sensor App is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.

You should have received a copy of the license along with this work.
If not, see <http://creativecommons.org/licenses/by-nc-sa/4.0/>.
