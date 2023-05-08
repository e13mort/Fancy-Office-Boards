package dev.pavel.dashboard

expect val platform: String

class Greeting {
    fun greeting() = "Hello, $platform!"
}