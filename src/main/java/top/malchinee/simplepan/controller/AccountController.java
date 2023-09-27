package top.malchinee.simplepan.controller;

import java.io.IOException;
import java.util.List;

import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.CreateImageCode;
import top.malchinee.simplepan.entity.vo.ResponseVO;
import top.malchinee.simplepan.exception.BusinessException;
import top.malchinee.simplepan.service.EmailCodeService;
import top.malchinee.simplepan.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户信息 Controller
 */
@RestController("userInfoController")
public class AccountController extends ABaseController{

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private EmailCodeService emailCodeService;

	@RequestMapping("/checkCode")
	public void checkCode(HttpServletResponse response, HttpSession session, Integer type) throws IOException {
		CreateImageCode vCode = new CreateImageCode(130, 38, 5, 10);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Controller", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		String code = vCode.getCode();
		if(type == null || type == 0) {
			session.setAttribute(Constants.CHECK_CODE_KEY, code);
		}else {
			session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL, code);
		}
		vCode.write(response.getOutputStream());
	}

	@RequestMapping("/sendEmailCode")
	public ResponseVO sendEmailCode(HttpSession session, String email, String checkCode, Integer type) {
		try {
			if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
				throw new BusinessException("图片验证码不正确");
			}
			emailCodeService.sendEmailCode(email, type);
			return getSuccessResponseVO(null);
		}finally {
			session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
		}
	}
}