package com.inkpot.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InkpotServerApplication

fun main(args: Array<String>) {
	runApplication<InkpotServerApplication>(*args)
}
