package top.malchinee.simplepan.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {

    @Value("${spring.mail.username}")
    private String sendUserName;

    @Value("${admin.emails}")
    private String adminEmails;

    @Value("${project.folder}")
    private String projectFolder;

    /**
     * qq登录相关
     */
    @Value("${qq.app.id:}")
    private String qqAppId;

    @Value("${qq.app.key:}")
    private String qqAppKey;

    @Value("${qq.url.authorization:}")
    private String qqUrlAuthorization;

    @Value("${qq.url.access.token:}")
    private String qqurlAccessToken;

    @Value("${qq.url.openid:}")
    private String qqUrlOpenId;

    @Value("${qq.url.user.info:}")
    private String qqUrlUserInfo;

    @Value("${qq.url.redirect:}")
    private String qqUrlRedirect;

    public String getQqAppId() {
        return qqAppId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public String getQqAppKey() {
        return qqAppKey;
    }

    public String getQqUrlAuthorization() {
        return qqUrlAuthorization;
    }

    public String getQqurlAccessToken() {
        return qqurlAccessToken;
    }

    public String getQqUrlOpenId() {
        return qqUrlOpenId;
    }

    public String getQqUrlUserInfo() {
        return qqUrlUserInfo;
    }

    public String getQqUrlRedirect() {
        return qqUrlRedirect;
    }
}
