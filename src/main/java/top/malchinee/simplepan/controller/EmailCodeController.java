package top.malchinee.simplepan.controller;

import java.util.List;

import top.malchinee.simplepan.entity.query.EmailCodeQuery;
import top.malchinee.simplepan.entity.po.EmailCode;
import top.malchinee.simplepan.entity.vo.ResponseVO;
import top.malchinee.simplepan.service.EmailCodeService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 邮箱验证码 Controller
 */
@RestController("emailCodeController")
@RequestMapping("/emailCode")
public class EmailCodeController extends ABaseController{

	@Resource
	private EmailCodeService emailCodeService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(EmailCodeQuery query){
		return getSuccessResponseVO(emailCodeService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(EmailCode bean) {
		emailCodeService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<EmailCode> listBean) {
		emailCodeService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<EmailCode> listBean) {
		emailCodeService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Code查询对象
	 */
	@RequestMapping("/getEmailCodeByCode")
	public ResponseVO getEmailCodeByCode(String code) {
		return getSuccessResponseVO(emailCodeService.getEmailCodeByCode(code));
	}

	/**
	 * 根据Code修改对象
	 */
	@RequestMapping("/updateEmailCodeByCode")
	public ResponseVO updateEmailCodeByCode(EmailCode bean,String code) {
		emailCodeService.updateEmailCodeByCode(bean,code);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Code删除
	 */
	@RequestMapping("/deleteEmailCodeByCode")
	public ResponseVO deleteEmailCodeByCode(String code) {
		emailCodeService.deleteEmailCodeByCode(code);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email查询对象
	 */
	@RequestMapping("/getEmailCodeByEmail")
	public ResponseVO getEmailCodeByEmail(String email) {
		return getSuccessResponseVO(emailCodeService.getEmailCodeByEmail(email));
	}

	/**
	 * 根据Email修改对象
	 */
	@RequestMapping("/updateEmailCodeByEmail")
	public ResponseVO updateEmailCodeByEmail(EmailCode bean,String email) {
		emailCodeService.updateEmailCodeByEmail(bean,email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email删除
	 */
	@RequestMapping("/deleteEmailCodeByEmail")
	public ResponseVO deleteEmailCodeByEmail(String email) {
		emailCodeService.deleteEmailCodeByEmail(email);
		return getSuccessResponseVO(null);
	}
}