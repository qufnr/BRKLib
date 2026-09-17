package space.byeoruk.lib.item.manager

import com.google.gson.JsonParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

internal class ItemLanguageManager(val plugin: JavaPlugin) {
    private val langFolderPath = "lang/minecraft"
    private val plainText = PlainTextComponentSerializer.plainText()

    private val translations = ConcurrentHashMap<String, Map<String, String>>()

    fun loadTranslations() {
        val langFolder = File(plugin.dataFolder, langFolderPath)

        if (!langFolder.exists()) {
            langFolder.mkdirs()
        }

        val files = langFolder.listFiles { file -> file.isFile && file.extension.equals("json", ignoreCase = true) }
        if (files.isNullOrEmpty()) {
            plugin.logger.info("No Minecraft language file found in ${langFolder.path}, item names will not be translated.")
            return
        }

        files.forEach { file ->
            val localeCode = file.nameWithoutExtension.lowercase()

            try {
                val entries = readEntries(file)
                translations[localeCode] = entries
                plugin.logger.info("Loaded ${entries.size} item names from $langFolderPath/${file.name}")
            }
            catch (e: Exception) {
                plugin.logger.warning("Failed to read language file $langFolderPath/${file.name}: ${e.message}")
            }
        }
    }

    /**
     * 플레이어에게 보이는 아이템 이름을 평문으로 반환
     *
     * @param item 아이템
     * @param locale 플레이어 언어
     * @return 아이템 이름, 사전에 없으면 서버가 주는 이름(영어 또는 번역 키)
     */
    fun nameOf(item: ItemStack, locale: Locale): String {
        val name = item.effectiveName()

        val translationKey = translationKeyOf(name)
        if (translationKey != null) {
            translationsOf(locale)[translationKey]?.let { return it }
        }

        return plainText.serialize(name)
    }

    /**
     * 컴포넌트 트리에서 첫 번째 번역 키 찾기
     *
     * `ItemStack.effectiveName()` 이 번역 컴포넌트를 스타일이 적용된 부모 안에 담아 돌려주는 경우가 있어서
     * 최상위만 보지 않고 자식까지 흝음
     *
     * @param component 이름 컴포넌트
     * @return 번역 키. 번역 컴포넌트가 없으면 null 반환
     */
    private fun translationKeyOf(component: Component): String? {
        if (component is TranslatableComponent) {
            return component.key()
        }

        component.children().forEach { child ->
            translationKeyOf(child)?.let { return it }
        }

        return null
    }

    /**
     * 언어 파일 엔트리 읽기 (아이템 이름만)
     *
     * @param file 언어 파일
     * @return 번역 키와 번역된 이름 맵 형태로 반환
     */
    private fun readEntries(file: File): Map<String, String> {
        val entries = HashMap<String, String>()

        file.bufferedReader(Charsets.UTF_8).use { reader ->
            JsonParser.parseReader(reader)
                .asJsonObject
                .entrySet()
                .forEach { (key, value) ->
                    if (!key.startsWith("item.") && !key.startsWith("block.")) {
                        return@forEach
                    }

                    if (value.isJsonPrimitive) {
                        entries[key] = value.asString
                    }
                }
        }

        return entries
    }

    /**
     * 플레이어 언어에 맞는 사전 찾기
     *
     * @param locale 플레이어 언어
     * @return 번역 키, 이름 맵. 맞는 언어가 없으면 빈 맵 반환
     */
    private fun translationsOf(locale: Locale): Map<String, String> {
        //  ko_KR -> ko_kr
        val country = locale.country
        val localeCode =
            if (country.isEmpty()) locale.language.lowercase()
            else "${locale.language}_$country".lowercase()

        translations[localeCode]?.let { return it }

        val languagePrefix = "${locale.language.lowercase()}_"
        return translations.entries.firstOrNull { it.key.startsWith(languagePrefix) }?.value ?: emptyMap()
    }
}
