package top.malchinee.simplepan.controller;

import org.apache.commons.lang3.StringUtils;
import top.malchinee.simplepan.component.RedisComponent;
import top.malchinee.simplepan.entity.config.AppConfig;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.service.FileInfoService;
import top.malchinee.simplepan.utils.StringTools;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

public class CommonFileController extends ABaseController{

    @Resource
    protected FileInfoService fileInfoService;

    @Resource
    protected AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;


    protected void getImage(HttpServletResponse response, String imageFolder, String imageName) {
        if(StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
            return;
        }
        String imageSuffix = StringTools.getFileSuffix(imageName);
        String filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + "/" + imageName;
        imageSuffix = imageSuffix.replace(".", "");
        String contentType = "image/" + imageSuffix;
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "max-age=2592000");
        readFile(response, filePath);
    }
}
