package bing.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import bing.exception.BusinessException;
import bing.exception.BusinessExceptionCodes;
import bing.util.UUIDUtils;
import bing.web.api.RestResponse;

/**
 * 文件上传
 * 
 * @author IceWee
 */
@Controller
public class FileUploadController {

	private static final String AJAX_SINGLE_UPLOAD = "/ajax/upload/single";

	/**
	 * 文件路径
	 */
	@Value("${file.root}")
	private String root;

	/**
	 * ajax 单文件上传
	 * 
	 * @param dir 子目录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AJAX_SINGLE_UPLOAD + "/{dir}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public RestResponse<String> ajaxSingleUpload(@PathVariable(name = "dir", required = false) String dir, MultipartHttpServletRequest request) {
		RestResponse<String> response = new RestResponse<>();
		Iterator<String> filenames = request.getFileNames();
		MultipartFile multipartFile;
		if (filenames.hasNext()) {
			multipartFile = request.getFile(filenames.next());
			String filepath = genSaveFilepath(dir, multipartFile);
			Path path = Paths.get(filepath);
			try {
				Files.write(path, multipartFile.getBytes());
				response.setData(filepath);
			} catch (IOException e) {
				throw new BusinessException(BusinessExceptionCodes.UPLOAD_FAILED, e);
			}
		}
		return response;
	}

	/**
	 * 生成文件存储绝对路径
	 * 
	 * @param dir 存储相对目录
	 * @param multipartFile
	 * @return
	 */
	private String genSaveFilepath(String dir, MultipartFile multipartFile) {
		String filename = genSaveFilename(multipartFile);
		String directory = root + File.separator;
		if (StringUtils.isNotBlank(dir)) {
			directory = directory + dir + File.separator;
		}
		return directory + filename;
	}

	/**
	 * 生成文件存储路径
	 * 
	 * @param multipartFile
	 * @return
	 */
	private String genSaveFilename(MultipartFile multipartFile) {
		String uuid = UUIDUtils.uuid();
		return uuid + "-" + multipartFile.getOriginalFilename();
	}

}
