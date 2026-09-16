package space.byeoruk.lib.mojang.dto

data class MojangPlayerSession(
    val id: String,
    val name: String,
    val properties: List<MojangPlayerProperty> = emptyList()
)
