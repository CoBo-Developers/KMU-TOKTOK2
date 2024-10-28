package cobo.writing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WritingApplication

fun main(args: Array<String>) {
    runApplication<WritingApplication>(*args)
}
