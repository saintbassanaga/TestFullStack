package tech.saintbassanaga.testfullstack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TestFullStackApplication

fun main(args: Array<String>) {
    runApplication<TestFullStackApplication>(*args)
}
