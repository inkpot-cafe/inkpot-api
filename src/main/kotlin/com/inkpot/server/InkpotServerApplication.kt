package com.inkpot.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
class InkpotServerApplication

data class Greeting(val id: Long, val content: String)

@RestController
class GreetingController {
	val counter = AtomicLong()

	@GetMapping("/greeting")
	fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
		Greeting(counter.incrementAndGet(), "Hello, $name")
}

fun main(args: Array<String>) {
	runApplication<InkpotServerApplication>(*args)
}
