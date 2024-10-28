package cobo.file.data.dto.professorCategory

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ProfessorPostCategoryReq(
    @JsonProperty("category")
    val category: String
)

data class ProfessorPutCategoryReq(
    @Schema(description = "수정할 카테고리 아이디")
    val categoryId: Int,

    @Schema(description = "해당 카테고리의 새로운 이름")
    val name: String
)