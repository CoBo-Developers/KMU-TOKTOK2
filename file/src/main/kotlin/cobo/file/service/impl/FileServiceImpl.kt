package cobo.file.service.impl

import cobo.file.config.response.CoBoResponse
import cobo.file.config.response.CoBoResponseDto
import cobo.file.config.response.CoBoResponseStatus
import cobo.file.data.dto.file.FileGetListRes
import cobo.file.data.dto.file.FileGetListResElement
import cobo.file.data.dto.professorFile.ProfessorFilePatchReq
import cobo.file.data.dto.professorFile.ProfessorFilePostReq
import cobo.file.data.entity.Category
import cobo.file.data.entity.File
import cobo.file.repository.CategoryRepository
import cobo.file.repository.FileRepository
import cobo.file.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
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

        val category = categoryRepository.findByName(professorFilePostReq.category).orElseThrow()

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

        fileRepository.save(file)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun professorDelete(fileId: List<Int>): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {

        fileRepository.deleteAllById(fileId)

        return CoBoResponse<CoBoResponseStatus>(CoBoResponseStatus.SUCCESS).getResponseEntity()
    }

    override fun professorPatch(professorFilePatchReq: ProfessorFilePatchReq): ResponseEntity<CoBoResponseDto<CoBoResponseStatus>> {
        val optionalFile = fileRepository.findById(professorFilePatchReq.fileId)

        if(optionalFile.isPresent){
            val file = optionalFile.get()

            file.name = file.fileName

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
}