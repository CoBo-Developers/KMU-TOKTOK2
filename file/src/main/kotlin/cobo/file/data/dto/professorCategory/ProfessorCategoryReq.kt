package cobo.file.data.dto.professorCategory

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class ProfessorPostCategoryReq(
    @JsonProperty("category")
    val category: String
)

data class ProfessorPutCategoryRes(
    @Schema(description = "수정할 카테고리 명")
    val oldCategory: String,

    @Schema(description = "해당 카테고리의 새로운 이름")
    val newCategory: String
)