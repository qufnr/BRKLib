package space.byeoruk.lib.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

data class LangConfiguration(
    private val config: YamlConfiguration
) {
    fun deserialize(path: String, vararg args: Any?): Component {
        val contents = getString(path, *args)
        return MiniMessage.miniMessage().deserialize(contents)
    }

    fun getString(path: String, vararg args: Any?): String {
        val raw = config.getString(path, "Language path \"$path\" is not found") ?: "Language path \"$path\" is not found"
        return if (args.isEmpty()) raw else format(raw, args)
    }

    /**
     * 문자열 내 "{0}", "{1}", "{2}" ... 형태의 플레이스홀더를
     * 전달된 인자의 순서에 맞는 값으로 치환
     *
     * @param template 치환 대상 문자열
     * @param args 치환할 값 목록 (순서대로 {0}, {1}, {2} ...에 대응)
     * @return 치환이 완료된 문자열. 대응하는 인자가 없는 플레이스홀더는 그대로 유지
     */
    private fun format(template: String, args: Array<out Any?>): String =
        PLACEHOLDER_REGEX.replace(template) { matchResult ->
            val index = matchResult.groupValues[1].toInt()
            args.getOrNull(index)?.toString() ?: matchResult.value
        }

    companion object {
        private val PLACEHOLDER_REGEX = Regex("\\{(\\d+)}")

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
