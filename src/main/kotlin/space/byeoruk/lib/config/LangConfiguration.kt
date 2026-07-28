package space.byeoruk.lib.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

data class LangConfiguration(
    private val config: YamlConfiguration
) {
    fun deserialize(path: String): Component {
        val contents = getString(path)
        return MiniMessage.miniMessage().deserialize(contents)
    }

    fun getString(path: String): String = config.getString(path, "Language path \"$path\" is not found") ?: "Language path \"$path\" is not found"

    companion object {
        fun load(plugin: JavaPlugin, lang: String): LangConfiguration {
            val resourcePath = "lang/$lang.yml"
            val langFile = File(plugin.dataFolder, resourcePath)

            if (!langFile.exists()) {
                if (plugin.getResource(resourcePath) != null)
                    plugin.saveResource(resourcePath, false)
                else {
                    langFile.parentFile.mkdirs()
                    langFile.createNewFile()
                }
            }

            val langYaml = YamlConfiguration.loadConfiguration(langFile)

            val defaultStream = plugin.getResource(resourcePath)
            if (defaultStream != null) {
                val defaultYaml = YamlConfiguration.loadConfiguration(defaultStream.reader(Charsets.UTF_8))
                langYaml.setDefaults(defaultYaml)
            }

            return LangConfiguration(langYaml)
        }
    }
}