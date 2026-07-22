package space.byeoruk.lib.string

import org.bukkit.Bukkit

object StringUtility {

    /**
     * 문자열로 플레이어 이름 검색 (일부 일치)
     *
     * @param name 이름 (빈 문자일 경우 모두 검색)
     * @param ignoreCase 대소문자 구분 (기본값: false)
     */
    fun getOnlinePlayerNames(name: String = "", ignoreCase: Boolean = false): List<String> =
        Bukkit.getOnlinePlayers()
            .map { it.name }
            .filter { element ->
                if (!name.isBlank()) element.contains(name, ignoreCase)
                else true
            }

    fun String.equals(values: List<String>, ignoreCase: Boolean): Boolean {
        for (value in values) {
            if (this.equals(value, ignoreCase)) {
                return true
            }
        }

        return false
    }

    fun String.contains(values: List<String>, ignoreCase: Boolean): Boolean {
        for (value in values) {
            if (this.contains(values, ignoreCase)) {
                return true
            }
        }

        return false
    }

    /**
     * 단어의 마지막 글자 받침 유무에 따라 알맞는 조사 반환
     *
     * @param josa 조사 문자열
     * @return 조사
     */
    fun String.getJosa(josa: String): String {
        if (this.isEmpty()) {
            return josa
        }

        val lastChar = this.last()

        //  마지막 글자가 한글 음절(가-힣)이 아닌 경우 기본 형태 반환
        if (lastChar.code !in 0xAC00..0xD7A3) {
            return josa.split("/").first().replace("(", "").replace(")", "")
        }

        //  인덱스가 0이면 받침 없음, 8이면 'ㄹ' 받침
        val jongseongIndex = (lastChar.code - 0xAC00) % 28
        val hasJongseong = jongseongIndex > 0
        val isRieul = jongseongIndex == 8

        return when (josa) {
            "을/를", "을를", "을", "를" -> if (hasJongseong) "을" else "를"
            "은/는", "은는", "은", "는" -> if (hasJongseong) "은" else "는"
            "이/가", "이가", "이", "가" -> if (hasJongseong) "이" else "가"
            "(으)로", "로으로", "으로/로", "로/으로", "으로", "로" -> if (hasJongseong && !isRieul) "으로" else "로"
            "와/과", "와과", "와", "과" -> if (hasJongseong) "과" else "와"
            else -> josa
        }
    }

    /**
     * 문자열에 조사를 붙힘
     *
     * @param josa 조사 문자열
     * @return 조사를 붙힌 문자열
     */
    fun String.appendJosa(josa: String): String =
        this + this.getJosa(josa)
}