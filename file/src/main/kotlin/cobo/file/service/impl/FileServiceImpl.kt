package cobo.file.service.impl

import cobo.file.repository.CategoryRepository
import cobo.file.service.FileService
import org.springframework.stereotype.Service

@Service
class FileServiceImpl(
    private val categoryRepository: CategoryRepository,
): FileService {
}