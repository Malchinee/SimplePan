package top.malchinee.simplepan.utils;
import org.apache.commons.codec.digest.DigestUtils;
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

    public static boolean isEmpty(String str) {
        if(null ==  str || "".equals(str) || "null".equals(str) || "\u0000".equals(str)) {
            return true;
        }else if("".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static String encodeByMd5(String originString) {
        return isEmpty(originString) ? null : DigestUtils.md5Hex(originString);
    }
}
