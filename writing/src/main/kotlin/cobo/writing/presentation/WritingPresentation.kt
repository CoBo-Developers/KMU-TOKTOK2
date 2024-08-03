package cobo.writing.presentation

import cobo.writing.service.WritingService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/writing")
class WritingPresentation(
    private val writingService: WritingService
) {
}