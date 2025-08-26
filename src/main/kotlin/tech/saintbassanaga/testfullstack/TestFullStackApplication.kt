package tech.saintbassanaga.testfullstack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestFullStackApplication

fun main(args: Array<String>) {
    runApplication<TestFullStackApplication>(*args)
}
