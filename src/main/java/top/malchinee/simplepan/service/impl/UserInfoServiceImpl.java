package top.malchinee.simplepan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import top.malchinee.simplepan.component.RedisComponent;
import top.malchinee.simplepan.entity.config.AppConfig;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.SessionWebUserDto;
import top.malchinee.simplepan.entity.dto.SysSettingsDto;
import top.malchinee.simplepan.entity.dto.UserSpaceDto;
import top.malchinee.simplepan.entity.enums.PageSize;
import top.malchinee.simplepan.entity.enums.UserStatusEnum;
import top.malchinee.simplepan.entity.query.UserInfoQuery;
import top.malchinee.simplepan.entity.po.UserInfo;
import top.malchinee.simplepan.entity.vo.PaginationResultVO;
import top.malchinee.simplepan.entity.query.SimplePage;
import top.malchinee.simplepan.exception.BusinessException;
import top.malchinee.simplepan.mappers.UserInfoMapper;
import top.malchinee.simplepan.service.EmailCodeService;
import top.malchinee.simplepan.service.UserInfoService;
import top.malchinee.simplepan.utils.StringTools;


/**
 * 用户信息 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private EmailCodeService emailCodeService;

	@Resource
	private RedisComponent redisComponent;

	@Resource
	private AppConfig appConfig;

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
		// TODO 查询当前用户已经上传文件大小总和
		// userSpaceDto.setUseSpace();
		userSpaceDto.setTotalSpace(userSpaceDto.getTotalSpace());
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
}