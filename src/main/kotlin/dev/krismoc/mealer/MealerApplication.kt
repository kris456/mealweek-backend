package dev.krismoc.mealer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MealerApplication

fun main(args: Array<String>) {
    runApplication<MealerApplication>(*args)
}
