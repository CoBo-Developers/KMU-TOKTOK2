package cobo.file.data.dto.category

data class CategoryGetListResElement(
    val id: Int,

    val name: String
)

data class CategoryGetListRes(
    val categories: List<CategoryGetListResElement>,
)