package space.byeoruk.lib.dto.mojang

data class MojangPlayerProperty(
    val name: String,
    val value: String,
    val signature: String? = null
)