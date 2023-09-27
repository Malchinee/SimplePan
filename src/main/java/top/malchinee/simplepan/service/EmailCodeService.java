package top.malchinee.simplepan.service;

import java.util.List;

import top.malchinee.simplepan.entity.query.EmailCodeQuery;
import top.malchinee.simplepan.entity.po.EmailCode;
import top.malchinee.simplepan.entity.vo.PaginationResultVO;


/**
 * 邮箱验证码 业务接口
 */
public interface EmailCodeService {

	/**
	 * 根据条件查询列表
	 */
	List<EmailCode> findListByParam(EmailCodeQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(EmailCodeQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery param);

	/**
	 * 新增
	 */
	Integer add(EmailCode bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<EmailCode> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<EmailCode> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(EmailCode bean,EmailCodeQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(EmailCodeQuery param);

	/**
	 * 根据Code查询对象
	 */
	EmailCode getEmailCodeByCode(String code);


	/**
	 * 根据Code修改
	 */
	Integer updateEmailCodeByCode(EmailCode bean,String code);


	/**
	 * 根据Code删除
	 */
	Integer deleteEmailCodeByCode(String code);


	/**
	 * 根据Email查询对象
	 */
	EmailCode getEmailCodeByEmail(String email);


	/**
	 * 根据Email修改
	 */
	Integer updateEmailCodeByEmail(EmailCode bean,String email);


	/**
	 * 根据Email删除
	 */
	Integer deleteEmailCodeByEmail(String email);

	/**
	 * 发送邮箱验证码
	 * @param email 邮箱
	 * @param type 类型
	 */
	void sendEmailCode(String email, Integer type);

}