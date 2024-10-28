package cobo.file.service.impl

import cobo.file.config.response.CoBoResponse
import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.file.FileGetListRes
import cobo.file.data.dto.file.FileGetListResElement
import cobo.file.data.dto.professorFile.ProfessorFilePutReq
import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import cobo.file.data.entity.Category
import cobo.file.data.entity.File
import cobo.file.repository.CategoryRepository
import cobo.file.repository.FileRepository
import cobo.file.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class FileServiceImpl(
    @Value("\${file.path}")
    private val filePath: String,
    private val categoryRepository: CategoryRepository,
    private val fileRepository: FileRepository
): FileService {
    override fun professorPost(professorFilePostReq: ProfessorFilePostReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        val category = Category(id = professorFilePostReq.categoryId, name = "")

        val originalFileName = professorFilePostReq.multipartFile.originalFilename

        var newName = filePath + UUID.randomUUID()

        val extension = StringUtils.getFilenameExtension(originalFileName)

        if (extension != null)
            newName += ".$extension"

        val filePath = Paths.get(newName)

        val file = File(
            id = null,
            name = professorFilePostReq.fileName,
            fileName = originalFileName ?: newName,
            path = newName,
            size = professorFilePostReq.multipartFile.size,
            deleted = false,
            category = category)

        Files.copy(professorFilePostReq.multipartFile.inputStream, filePath)

        try {
            fileRepository.save(file)
        }
        catch(dataIntegrityViolationException: DataIntegrityViolationException) {
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.BAD_REQUEST).getResponseEntity()
        }

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun professorDelete(fileId: List<Int>): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        fileRepository.deleteAllById(fileId)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun professorPut(professorFilePutReq: ProfessorFilePutReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        val optionalFile = fileRepository.findById(professorFilePutReq.fileId)

        if(optionalFile.isPresent){
            val file = optionalFile.get()

            file.name = professorFilePutReq.name
            file.category = Category(id = professorFilePutReq.categoryId, name = "")

            fileRepository.save(file)

            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
        }
        else{
            return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.NOT_FOUND_FILE).getResponseEntity()
        }
    }

    override fun getList(categoryId: Int?): ResponseEntity<CoBoResponseDto<FileGetListRes>> {
        val category = Category(id = categoryId, name = "")
        val fileGetListResElements = fileRepository.findByCategory(category).map{
            FileGetListResElement(
                id = it.id,
                name = it.name,
                fileName = it.fileName,
                size = it.size,
                createdAt = it.createdAt
            )
        }

        return CoBoResponse(
            FileGetListRes(
                files = fileGetListResElements),
            CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun get(fileId: Int): ResponseEntity<Resource> {

        val optionalFile = fileRepository.findById(fileId)

        if(optionalFile.isEmpty)
            return ResponseEntity(HttpStatus.NOT_FOUND)

        val file = java.io.File(optionalFile.get().path)

        val fileContent = Files.readAllBytes(file.toPath())

        val resource = ByteArrayResource(fileContent)
        val encodedFileName = URLEncoder.encode(optionalFile.get().fileName, StandardCharsets.UTF_8.toString())

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$encodedFileName\"")
            .body(resource)
    }
}