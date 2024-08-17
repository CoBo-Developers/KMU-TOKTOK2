package cobo.file.service

import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

interface FileService {
    fun professorPost(professorFilePostReq: ProfessorFilePostReq): ResponseEntity<HttpStatus>
}