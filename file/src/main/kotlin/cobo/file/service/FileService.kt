package cobo.file.service

import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.file.FileGetListRes
import cobo.file.data.dto.professorFile.ProfessorFilePutReq
import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity

interface FileService {
    fun professorPost(professorFilePostReq: ProfessorFilePostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun professorDelete(fileId: List<Int>): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun professorPut(professorFilePutReq: ProfessorFilePutReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>>
    fun getList(categoryId: Int?): ResponseEntity<CoBoResponseDto<FileGetListRes>>
    fun get(fileId: Int): ResponseEntity<Resource>
}