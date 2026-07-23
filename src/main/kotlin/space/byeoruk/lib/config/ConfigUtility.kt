package space.byeoruk.lib.config

import org.bukkit.configuration.file.FileConfiguration
import java.math.BigDecimal

object ConfigUtility {
    /**
     * Config 값을 BigDecimal 로 설정
     *
     * @param path config path
     * @param defaultValue 기본값
     * @return Config 설정된 값. 없을 경우 기본값 반환
     */
    fun FileConfiguration.getBigDecimal(path: String, defaultValue: BigDecimal): BigDecimal {
        val doubleValue = this.getDouble(path, defaultValue.toDouble())
        return doubleValue.toBigDecimal()
    }
}