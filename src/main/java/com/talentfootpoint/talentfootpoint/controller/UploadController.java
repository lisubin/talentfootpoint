package com.talentfootpoint.talentfootpoint.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.talentfootpoint.talentfootpoint.exception.BaseException;
import com.talentfootpoint.talentfootpoint.service.UploadService;
import com.talentfootpoint.talentfootpoint.util.JsonResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Scope("prototype")
@Controller
@Service
public class UploadController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@Autowired
	private UploadService uploadService;

	/** 统一的编码格式*/
	private static final String encode = "UTF-8";
	private static final String IMAGE_TYPE = "image";
	private static final String SOUND_TYPE = "sound";
	private static final String HOMEWORK_RELATIVE_DIR = File.separator+"homework"+File.separator;
	private static final String FILE_RELATIVE_DIR = File.separator+"file"+File.separator;
	private static final Map<String, String> extMap = new HashMap<String, String>();
	
	static{
		extMap.put(IMAGE_TYPE, "gif,jpg,jpeg,png,bmp");
		extMap.put(SOUND_TYPE, "mp3,aac");
	}

	/**
	 * web端统一的文件上传接口
	 * 2018-03-22
	 * @param file image, office , other
	 * @param uploadType  上传文件对应系统模块类型
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "file/fileUpload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public JsonResult fileUpload (@RequestParam(value = "file", required = false) MultipartFile[] files, String uploadType, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		try {

			List<Map<String,String>> pathList = uploadService.fileUpload("talentfootpoint", uploadType, files);
			jsonResult.setResult(pathList);
		} catch (BaseException e) {
			jsonResult.setStatus(JsonResult.FAILED_CODE);
			jsonResult.setMsg(e.getLocalizedMessage());
		}
		return jsonResult;
	}

}
