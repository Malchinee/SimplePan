package top.malchinee.simplepan.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import top.malchinee.simplepan.component.RedisComponent;
import top.malchinee.simplepan.entity.config.AppConfig;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.QQInfoDto;
import top.malchinee.simplepan.entity.dto.SessionWebUserDto;
import top.malchinee.simplepan.entity.dto.SysSettingsDto;
import top.malchinee.simplepan.entity.dto.UserSpaceDto;
import top.malchinee.simplepan.entity.enums.PageSize;
import top.malchinee.simplepan.entity.enums.UserStatusEnum;
import top.malchinee.simplepan.entity.po.FileInfo;
import top.malchinee.simplepan.entity.query.FileInfoQuery;
import top.malchinee.simplepan.entity.query.UserInfoQuery;
import top.malchinee.simplepan.entity.po.UserInfo;
import top.malchinee.simplepan.entity.vo.PaginationResultVO;
import top.malchinee.simplepan.entity.query.SimplePage;
import top.malchinee.simplepan.exception.BusinessException;
import top.malchinee.simplepan.mappers.FileInfoMapper;
import top.malchinee.simplepan.mappers.UserInfoMapper;
import top.malchinee.simplepan.service.EmailCodeService;
import top.malchinee.simplepan.service.UserInfoService;
import top.malchinee.simplepan.utils.JsonUtils;
import top.malchinee.simplepan.utils.OKHttpUtils;
import top.malchinee.simplepan.utils.StringTools;


/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private EmailCodeService emailCodeService;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private AppConfig appConfig;

	@Resource
	private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
		return this.userInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoQuery param) {
		return this.userInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateUserInfoByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据NickName获取对象
	 */
	@Override
	public UserInfo getUserInfoByNickName(String nickName) {
		return this.userInfoMapper.selectByNickName(nickName);
	}

	/**
	 * 根据NickName修改
	 */
	@Override
	public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
		return this.userInfoMapper.updateByNickName(bean, nickName);
	}

	/**
	 * 根据NickName删除
	 */
	@Override
	public Integer deleteUserInfoByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}

	/**
	 * 根据QqOpenId获取对象
	 */
	@Override
	public UserInfo getUserInfoByQqOpenId(String qqOpenId) {
		return this.userInfoMapper.selectByQqOpenId(qqOpenId);
	}

	/**
	 * 根据QqOpenId修改
	 */
	@Override
	public Integer updateUserInfoByQqOpenId(UserInfo bean, String qqOpenId) {
		return this.userInfoMapper.updateByQqOpenId(bean, qqOpenId);
	}

	/**
	 * 根据QqOpenId删除
	 */
	@Override
	public Integer deleteUserInfoByQqOpenId(String qqOpenId) {
		return this.userInfoMapper.deleteByQqOpenId(qqOpenId);
	}

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void register(String email, String nickName, String password, String emailCode) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(null != userInfo) {
			throw new BusinessException("邮箱账号已经存在");
		}
		UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
		if(null != nickNameUser) {
			throw new BusinessException("昵称已经存在");
		}
		// 校验邮箱验证码
		emailCodeService.checkCode(email, emailCode);

		String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
		userInfo = new UserInfo();
		userInfo.setUserId(userId);
		userInfo.setEmail(email);
		userInfo.setPassword(StringTools.encodeByMd5(password));
		userInfo.setNickName(nickName);

		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
		userInfo.setUseSpace(0L);
		SysSettingsDto sysSettingsDto = redisComponent.getSysSettingsDto();
		userInfo.setTotalSpace(sysSettingsDto.getUserInitUseSpace() * Constants.MB);
		this.userInfoMapper.insert(userInfo);
	}

    @Override
    public SessionWebUserDto login(String email, String password) {

		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(null == userInfo || !userInfo.getPassword().equals(password)) {
			throw new BusinessException("账号或者密码错误");
		}
		if(UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())) {
			throw new BusinessException("账号已禁用, 请联系管理员");
		}
		UserInfo updateInfo = new UserInfo();
		updateInfo.setLastLoginTime(new Date());
		this.userInfoMapper.updateByUserId(updateInfo, userInfo.getUserId());

		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setUserId(userInfo.getUserId());
		sessionWebUserDto.setNickName(userInfo.getNickName());
		if(ArrayUtils.contains(appConfig.getAdminEmails().split(","), email)) {
			sessionWebUserDto.setAdmin(true);
		}else {
			sessionWebUserDto.setAdmin(false);
		}
		// 用户空间
		UserSpaceDto userSpaceDto = new UserSpaceDto();
		Long useSpace = fileInfoMapper.selectUseSpace(userInfo.getUserId());
		userSpaceDto.setUseSpace(useSpace);
		userSpaceDto.setTotalSpace(userInfo.getTotalSpace());
		redisComponent.saveUserSpaceUse(userInfo.getUserId(), userSpaceDto);
		return sessionWebUserDto;
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(null == userInfo || !userInfo.getPassword().equals(password)) {
			throw new BusinessException("邮箱账号不存在");
		}
		emailCodeService.checkCode(email, emailCode);
		UserInfo updateInfo = new UserInfo();
		updateInfo.setPassword(StringTools.encodeByMd5(password));
		this.userInfoMapper.updateByEmail(updateInfo, email);
	}

    @Override
    public SessionWebUserDto qqLogin(String code) {
		// 1. 获取通过回调code 获取accessToken
		String accessToken = getQQAccessToken(code);
		// 2. 获取qq_openid
		String openId = getQQOpenId(accessToken);
		UserInfo user = this.userInfoMapper.selectByQqOpenId(openId);

		String avatar = null;
		if(user == null) {
			// 自动注册
			// 3. 获取用户的qq基本信息
			QQInfoDto qqUserInfo = getQQUserInfo(accessToken, openId);
			user = new UserInfo();
			String nickName = qqUserInfo.getNickName();
			nickName = nickName.length() > Constants.LENGTH_20 ? nickName.substring(0, Constants.LENGTH_20) : nickName;
			avatar = StringTools.isEmpty(qqUserInfo.getFigureurl_qq_2()) ? qqUserInfo.getFigureurl_qq_1() : qqUserInfo.getFigureurl_qq_2();
			Date curDate = new Date();
			user.setQqOpenId(openId);
			user.setJoinTime(curDate);
			user.setNickName(nickName);
			user.setUserId(StringTools.getRandomNumber(Constants.LENGTH_10));
			user.setLastLoginTime(curDate);
			user.setStatus(UserStatusEnum.ENABLE.getStatus());
			user.setUseSpace(0L);
			user.setTotalSpace(redisComponent.getSysSettingsDto().getUserInitUseSpace() * Constants.MB);
			this.userInfoMapper.insert(user);
			user = userInfoMapper.selectByQqOpenId(openId);
		}else {
			UserInfo updateInfo = new UserInfo();
			updateInfo.setLastLoginTime(new Date());
			avatar = user.getQqAvatar();
			this.userInfoMapper.updateByQqOpenId(updateInfo, openId);
		}
		SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
		sessionWebUserDto.setUserId(user.getUserId());
		sessionWebUserDto.setNickName(user.getNickName());
		sessionWebUserDto.setAvatar(avatar);

		if(ArrayUtils.contains(appConfig.getAdminEmails().split(","), user.getEmail() == null ? "" : user.getEmail())) {
			sessionWebUserDto.setAdmin(true);
		}else {
			sessionWebUserDto.setAdmin(false);
		}
		UserSpaceDto userSpaceDto = new UserSpaceDto();
		Long useSpace = fileInfoMapper.selectUseSpace(user.getUserId());
		userSpaceDto.setUseSpace(useSpace);
		userSpaceDto.setTotalSpace(user.getTotalSpace());
		redisComponent.saveUserSpaceUse(user.getUserId(), userSpaceDto);
		return sessionWebUserDto;
    }

	private String getQQAccessToken(String code) {
		String accessToken = null;
		String url = null;
		try {
			url = String.format(appConfig.getQqurlAccessToken(), appConfig.getQqAppId(), appConfig.getQqAppKey(), code, URLEncoder.encode(appConfig.getQqUrlRedirect(),"utf-8"));
		}catch (UnsupportedEncodingException e) {
			logger.error("encode失败");
		}
		String tokenResult = OKHttpUtils.getRequest(url);
		if(tokenResult != null || tokenResult.indexOf(Constants.VIEW_OBJ_RESULT_KEY) != -1) {
			logger.error("获取qqToken失败:{}", tokenResult);
			throw new BusinessException("获取qqToken失败");
		}
		String[] params = tokenResult.split("&");
		if(params != null && params.length > 0) {
			for(String p : params) {
				if(p.indexOf("access_token") != -1) {
					accessToken = p.split("=")[1];
					break;
				}
			}
		}
		return accessToken;
	}

	private String getQQOpenId(String accessToken) throws BusinessException {
		// 获取openId
		String url = String.format(appConfig.getQqUrlOpenId(), accessToken);
		String openIDResult = OKHttpUtils.getRequest(url);
		String tmpJson = this.getQQResp(openIDResult);
		if(tmpJson == null) {
			logger.error("调qq接口获取openID失败, tempJson:{}", tmpJson);
			throw new BusinessException("调qq接口获取openID失败");
		}
		Map jsonData = JsonUtils.convertJson2Obj(tmpJson, Map.class);
		if(jsonData == null) {
			logger.error("调qq接口获取openID失败：{}", jsonData);
			throw new BusinessException("调qq接口获取openID失败");
		}
		return String.valueOf(jsonData.get("openid"));
	}

	private String getQQResp(String result) {
		if(StringUtils.isNotBlank(result)) {
			int pos = result.indexOf("callback");
			if(pos != -1) {
				int start = result.indexOf("(");
				int end = result.indexOf(")");
				String jsonStr = result.substring(start + 1, end - 1);
				return jsonStr;
			}
		}
		return null;
	}

	private QQInfoDto getQQUserInfo(String accessToken, String qqOpenId) throws BusinessException {
		String url = String.format(appConfig.getQqUrlUserInfo(), accessToken, appConfig.getQqAppId(), qqOpenId);
		String response = OKHttpUtils.getRequest(url);
		if(StringUtils.isNotBlank(response)) {
			QQInfoDto qqInfo = JsonUtils.convertJson2Obj(response, QQInfoDto.class);
			if(qqInfo.getRet() != 0) {
				logger.error("qqInfo:{}", response);
				throw new BusinessException("调qq接口获取用户信息异常");
			}
			return qqInfo;
		}
		throw new BusinessException("调qq接口获取用户信息异常");
	}
}