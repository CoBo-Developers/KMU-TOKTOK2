package cobo.file.service.impl

import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import cobo.file.repository.CategoryRepository
import cobo.file.service.FileService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FileServiceImpl(
    private val categoryRepository: CategoryRepository,
): FileService {
    override fun professorPost(professorFilePostReq: ProfessorFilePostReq): ResponseEntity<HttpStatus> {


        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}