package cobo.writing.service.impl

import cobo.writing.repository.WritingRepository
import cobo.writing.service.WritingService
import org.springframework.stereotype.Service

@Service
class WritingServiceImpl(
    private val writingRepository : WritingRepository
): WritingService {
}