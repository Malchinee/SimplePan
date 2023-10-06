package top.malchinee.simplepan.component;

import org.springframework.stereotype.Component;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.SysSettingsDto;
import top.malchinee.simplepan.entity.dto.UserSpaceDto;
import top.malchinee.simplepan.entity.po.FileInfo;
import top.malchinee.simplepan.entity.query.FileInfoQuery;
import top.malchinee.simplepan.mappers.FileInfoMapper;

import javax.annotation.Resource;

@Component("redisComponent")
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    public SysSettingsDto getSysSettingsDto() {
        SysSettingsDto sysSettingsDto = (SysSettingsDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if(sysSettingsDto == null) {
            sysSettingsDto = new SysSettingsDto();
            redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingsDto);
        }
        return sysSettingsDto;
    }

    public void saveUserSpaceUse(String userId, UserSpaceDto userSpaceDto) {
        redisUtils.setex(Constants.REDIS_KEY_USER_SPACE_USE + userId, userSpaceDto, Constants.REDIS_KEY_EXPIRES_DAY);
    }

    public UserSpaceDto getUserSpaceUse(String userId) {
        UserSpaceDto spaceDto = (UserSpaceDto) redisUtils.get(Constants.REDIS_KEY_USER_SPACE_USE + userId);
        if(spaceDto == null) {
            spaceDto = new UserSpaceDto();
            Long useSpace = fileInfoMapper.selectUseSpace(userId);
            spaceDto.setUseSpace(useSpace);
            spaceDto.setTotalSpace(getSysSettingsDto().getUserInitUseSpace() * Constants.MB);
            saveUserSpaceUse(userId, spaceDto);
        }
        return spaceDto;
    }

    public Long getFileTempSize(String userId, String fileId) {
        Long currentSize = getFileSizeFromRedis(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId);
        return currentSize;
    }

    public void saveFileTempSize(String userId, String fileId, Long fileSize) {
        Long currentSize = getFileTempSize(userId, fileId);
        redisUtils.setex(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId, currentSize, Constants.REDIS_KEY_EXPIRES_ONE_HOUR);
    }


    private Long getFileSizeFromRedis(String key) {
        Object sizeObj = redisUtils.get(key);
        if(sizeObj == null) {
            return 0L;
        }
        if(sizeObj instanceof Integer) {
            return ((Integer)sizeObj).longValue();
        }else if(sizeObj instanceof Long) {
            return (Long)sizeObj;
        }
        return 0L;
    }
}
