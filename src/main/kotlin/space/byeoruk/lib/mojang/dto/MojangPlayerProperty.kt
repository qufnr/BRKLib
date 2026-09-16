package space.byeoruk.lib.mojang.dto

data class MojangPlayerProperty(
    val name: String,
    val value: String,
    val signature: String? = null
)