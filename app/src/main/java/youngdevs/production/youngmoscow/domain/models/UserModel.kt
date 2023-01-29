package youngdevs.production.youngmoscow.domain.models

data class UserModel(
    val id: String,
    val name: String? = null,
    val email: String?
)
