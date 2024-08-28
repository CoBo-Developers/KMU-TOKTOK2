package cobo.file.data.dto.professorFile

import org.springframework.web.multipart.MultipartFile

data class ProfessorFilePostReq(

    val fileName: String,

    val categoryId: Int,

    val multipartFile: MultipartFile

)

data class ProfessorFilePutReq(
    val fileId: Int,

    val name: String,

    val categoryId:Int
)