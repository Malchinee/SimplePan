package top.malchinee.simplepan.utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import top.malchinee.simplepan.exception.BusinessException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;


public class StringTools {
    /**
     * 生成随机数
     * @param count
     * @return
     */
    public static final String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }
}
