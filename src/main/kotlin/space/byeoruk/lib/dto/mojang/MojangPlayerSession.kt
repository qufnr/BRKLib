package space.byeoruk.lib.dto.mojang

data class MojangPlayerSession(
    val id: String,
    val name: String,
    val properties: List<MojangPlayerProperty> = emptyList()
)
