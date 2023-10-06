package top.malchinee.simplepan.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import top.malchinee.simplepan.annotation.GlobalInterceptor;
import top.malchinee.simplepan.annotation.VerifyParam;
import top.malchinee.simplepan.entity.dto.SessionWebUserDto;
import top.malchinee.simplepan.entity.dto.UploadResultDto;
import top.malchinee.simplepan.entity.enums.FileCategoryEnums;
import top.malchinee.simplepan.entity.enums.FileDelFlagEnums;
import top.malchinee.simplepan.entity.query.FileInfoQuery;
import top.malchinee.simplepan.entity.po.FileInfo;
import top.malchinee.simplepan.entity.vo.FileInfoVO;
import top.malchinee.simplepan.entity.vo.PaginationResultVO;
import top.malchinee.simplepan.entity.vo.ResponseVO;
import top.malchinee.simplepan.service.FileInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 文件信息 Controller
 */
@RestController("fileInfoController")
@RequestMapping("/file")
public class FileInfoController extends CommonFileController{

	private static final Logger logger = LoggerFactory.getLogger(FileInfoController.class);

	@Resource
	private FileInfoService fileInfoService;

	/**
	 * 根据条件分页查询
	 * @param session
	 * @param query
	 * @param category
	 * @return
	 */
	@RequestMapping("/loadDataList")
	@GlobalInterceptor
	public ResponseVO loadDataList(HttpSession session, FileInfoQuery query, String category) {
		FileCategoryEnums categoryEnums = FileCategoryEnums.getByCode(category);
		if(null != categoryEnums) {
			query.setFileCategory(categoryEnums.getCategory());
		}
		query.setUserId(getUserInfoFromSession(session).getUserId());
		query.setOrderBy("last_update_time desc");
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		logger.info("query:{}", JSON.toJSONString(query));
		PaginationResultVO resultVO = fileInfoService.findListByPage(query);
		return getSuccessResponseVO(convert2PaginationVO(resultVO, FileInfoVO.class));
	}

	@RequestMapping("/uploadFile")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO uploadFile(HttpSession session,
								 String fileId,
								 MultipartFile file,
								 @VerifyParam(required = true) String fileName,
								 @VerifyParam(required = true) String filePid,
								 @VerifyParam(required = true) String fileMd5,
								 @VerifyParam(required = true) Integer chunkIndex,
								 @VerifyParam(required = true) Integer chunks) {
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		UploadResultDto uploadResultDto = fileInfoService.uploadFile(webUserDto, fileId, file, fileName, filePid, fileMd5, chunkIndex, chunks);
		logger.info("filePid:{}", filePid);
		return getSuccessResponseVO(uploadResultDto);
	}

	@RequestMapping("/getImage/{imageFolder}/{imageName}")
	public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder, @PathVariable("imageName") String imageName) {
		super.getImage(response, imageFolder, imageName);
	}
}