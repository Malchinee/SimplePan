package top.malchinee.simplepan.entity.constants;

public class Constants {

    public static final String CHECK_CODE_KEY = "check_code_key";

    public static final String CHECK_CODE_KEY_EMAIL = "check_code_key_email";

    public static final String FILE_FOLDER_FILE = "/file/";

    public static final String FILE_FOLDER_AVATAR_NAME = "avatar/";

    public static final String AVATAR_SUFFIX = ".jpg";

    public static final String AVATAR_DEFAULT = "default_avatar.jpg";

    public static final Integer LENGTH_5 = 5;
    public static final Integer LENGTH_10 = 10;
    public static final Integer LENGTH_15 = 15;

    public static final String SESSION_KEY = "session_key";

    public static final Integer ZERO = 0;

    public static final String REDIS_KEY_SYS_SETTING = "simplepan:syssetting:";

    public static final String REDIS_KEY_USER_SPACE_USE = "simplepan:user:spaceuse:";

    public static final Integer REDIS_KEY_EXPIRES_ONE_MINUTE = 60;

    public static final Integer REDIS_KEY_EXPIRES_DAY = REDIS_KEY_EXPIRES_ONE_MINUTE * 60 * 24;

    public static final Long MB = 1024 * 1024L;
}
