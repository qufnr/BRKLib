package space.byeoruk.lib.utility.string

import dev.lone.itemsadder.api.FontImages.FontImageWrapper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextReplacementConfig
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

object EmojiUtility {
    private val EMOJI_PATTERN = Regex(":([a-zA-Z0-9_]+):")

    /**
     * 이모지 변환
     *
     * @param namespace ItemsAdder 네임스페이스
     * @return 이모지로 변경된 문자열
     */
    fun String.replaceEmojis(namespace: String? = null): String =
        EMOJI_PATTERN.replace(this) { matchResult ->
            val rawName = matchResult.groupValues[1]
            val fullMatch = matchResult.value

            val targetName = if (rawName.contains(":") || namespace != null)
                rawName
            else
                "$namespace:$rawName"

            val wrapper = FontImageWrapper(targetName)

            if (wrapper.exists())
                wrapper.string
            else
                fullMatch
        }

    /**
     * 이모지 변환
     *
     * @param namespace ItemsAdder 네임스페이스
     * @param colour 이모지 색상
     * @return 이모지로 변환된 컴포넌트
     */
    fun Component.replaceEmojis(namespace: String? = null, colour: TextColor = TextColor.color(255, 255, 255)): Component {
        val replacementConfig = TextReplacementConfig.builder()
            .match(EMOJI_PATTERN.pattern)
            .replacement { matchResult, builder ->
                val rawName = matchResult.group(1) ?: return@replacement builder.build()
                val fullMatch = matchResult.group(0)

                val targetName = if (rawName.contains(":") || namespace == null)
                    rawName
                else
                    "$namespace:$rawName"

                val wrapper = FontImageWrapper(targetName)
                if (wrapper.exists())
                    builder.content(wrapper.string)
                        .color(colour)
                        .decoration(TextDecoration.ITALIC, false)
                else
                    builder.content(fullMatch)
            }
            .build()

        return this.replaceText(replacementConfig)
    }
}