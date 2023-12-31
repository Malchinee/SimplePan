package top.malchinee.simplepan.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.malchinee.simplepan.entity.constants.Constants;
import top.malchinee.simplepan.entity.dto.SessionWebUserDto;
import top.malchinee.simplepan.entity.enums.ResponseCodeEnum;
import top.malchinee.simplepan.entity.vo.ResponseVO;
import top.malchinee.simplepan.exception.BusinessException;
import top.malchinee.simplepan.utils.StringTools;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(ABaseController.class);

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    protected <T> ResponseVO getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO getServerErrorResponseVO(T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        if(!StringTools.PathIsOk(filePath)) {
            return;
        }
        OutputStream out = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                return;
            }
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        }catch (Exception e) {
            logger.error("读取文件异常", e);
        }finally {
            if(out != null) {
                try{
                    out.close();
                }catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if(in != null) {
                try{
                    in.close();
                }catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }

    protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        return sessionWebUserDto;
    }
}
