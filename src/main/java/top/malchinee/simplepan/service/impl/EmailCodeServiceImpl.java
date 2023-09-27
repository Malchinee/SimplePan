package top.malchinee.simplepan.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import top.malchinee.simplepan.entity.constants.Constants;
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

	@Resource
	private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

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

		// TODO 发送验证码
		// 将之前的验证码置为无效
		emailCodeMapper.disableEmailCode(email);

		EmailCode emailCode = new EmailCode();
		emailCode.setCode(code);
		emailCode.setEmail(email);
		emailCode.setStatus(Constants.ZERO);
		emailCode.setCreateTime(new Date());

		emailCodeMapper.insert(emailCode);
	}


}