package cobo.file.data.dto.professorFile

import org.springframework.web.multipart.MultipartFile

data class ProfessorFilePostReq(

    val fileName: String,

    val category: String,

    val multipartFile: MultipartFile

)