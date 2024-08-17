package cobo.file.service

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface FileService {
    fun professorPost(professorFilePostReq: ProfessorFilePostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun delete(fileId: List<Int>): ResponseEntity<HttpStatus>
}