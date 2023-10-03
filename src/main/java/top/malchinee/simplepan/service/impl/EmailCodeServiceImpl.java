package top.malchinee.simplepan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import top.malchinee.simplepan.component.RedisComponent;
import top.malchinee.simplepan.component.RedisUtils;
import top.malchinee.simplepan.entity.config.AppConfig;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.SysSettingsDto;
import top.malchinee.simplepan.entity.enums.PageSize;
import top.malchinee.simplepan.entity.po.UserInfo;
import top.malchinee.simplepan.entity.query.EmailCodeQuery;
import top.malchinee.simplepan.entity.po.EmailCode;
import top.malchinee.simplepan.entity.query.UserInfoQuery;
import top.malchinee.simplepan.entity.vo.PaginationResultVO;
import top.malchinee.simplepan.entity.query.SimplePage;
import top.malchinee.simplepan.exception.BusinessException;
import top.malchinee.simplepan.mappers.EmailCodeMapper;
import top.malchinee.simplepan.mappers.UserInfoMapper;
import top.malchinee.simplepan.service.EmailCodeService;
import top.malchinee.simplepan.utils.StringTools;


/**
 * 邮箱验证码 业务接口实现
 */
@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

	private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

	@Resource
	private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Resource
	private JavaMailSender javaMailSender;

	@Resource
	private AppConfig appConfig;

	@Resource
	private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<EmailCode> findListByParam(EmailCodeQuery param) {
		return this.emailCodeMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(EmailCodeQuery param) {
		return this.emailCodeMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<EmailCode> list = this.findListByParam(param);
		PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(EmailCode bean) {
		return this.emailCodeMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<EmailCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.emailCodeMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<EmailCode> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.emailCodeMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(EmailCode bean, EmailCodeQuery param) {
		return this.emailCodeMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(EmailCodeQuery param) {
		return this.emailCodeMapper.deleteByParam(param);
	}

	/**
	 * 根据Code获取对象
	 */
	@Override
	public EmailCode getEmailCodeByCode(String code) {
		return this.emailCodeMapper.selectByCode(code);
	}

	/**
	 * 根据Code修改
	 */
	@Override
	public Integer updateEmailCodeByCode(EmailCode bean, String code) {
		return this.emailCodeMapper.updateByCode(bean, code);
	}

	/**
	 * 根据Code删除
	 */
	@Override
	public Integer deleteEmailCodeByCode(String code) {
		return this.emailCodeMapper.deleteByCode(code);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public EmailCode getEmailCodeByEmail(String email) {
		return this.emailCodeMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateEmailCodeByEmail(EmailCode bean, String email) {
		return this.emailCodeMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteEmailCodeByEmail(String email) {
		return this.emailCodeMapper.deleteByEmail(email);
	}

	@Override
	public void sendEmailCode(String email, Integer type) {
		if(type.equals(Constants.ZERO)) {
			UserInfo userInfo = userInfoMapper.selectByEmail(email);
			if(null != userInfo) {
				throw new BusinessException("邮箱已存在");
			}
		}

		String code = StringTools.getRandomNumber(Constants.LENGTH_5);

		// 发送验证码
		sendEmailCode(email, code);
		// 将之前的验证码置为无效
		emailCodeMapper.disableEmailCode(email);

		EmailCode emailCode = new EmailCode();
		emailCode.setCode(code);
		emailCode.setEmail(email);
		emailCode.setStatus(Constants.ZERO);
		emailCode.setCreateTime(new Date());

		emailCodeMapper.insert(emailCode);
	}

    @Override
    public void checkCode(String email, String code) {
        EmailCode emailCode = this.emailCodeMapper.selectByEmailAndCode(email, code);
		if(null == emailCode) {
			throw new BusinessException("邮箱验证码不正确");
		}
		if(emailCode.getStatus() == 1 || System.currentTimeMillis() - emailCode.getCreateTime().getTime() > Constants.LENGTH_15 * 1000 * 60) {
			throw new BusinessException("邮箱验证码过期");
		}
		emailCodeMapper.disableEmailCode(email);
    }

    private void sendEmailCode(String toEmail, String code) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(appConfig.getSendUserName());
			helper.setTo(toEmail);

			SysSettingsDto sysSettingsDto = redisComponent.getSysSettingsDto();
			helper.setSubject(sysSettingsDto.getRegisterMailTitle());
			helper.setText(String.format(sysSettingsDto.getRegisterEmailContent(), code));
			helper.setSentDate(new Date());
			javaMailSender.send(mimeMessage);
		}catch (Exception e) {
			logger.error("邮件发送失败", e);
			throw new BusinessException("邮件发送失败");
		}
	}
}
