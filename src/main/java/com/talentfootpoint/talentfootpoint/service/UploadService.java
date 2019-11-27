package com.talentfootpoint.talentfootpoint.service;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.talentfootpoint.talentfootpoint.entity.Constants;
import com.talentfootpoint.talentfootpoint.exception.BaseException;
import com.talentfootpoint.talentfootpoint.util.ImageUtils;
import com.talentfootpoint.talentfootpoint.util.UniqueKeyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("uploadService")
public class UploadService {
	private static Logger logger = LoggerFactory.getLogger(UploadService.class);
	
	private static final String DEFAULT = "default";
	private static final Long DEFAULT_MAX_SIZE = 3145728L;//默认大小为3M
	private static final Map<String, String> extMap = new HashMap<String, String>();
	private static final Map<String, String> dirTypeMap = new HashMap<String,String>();

	private static String SCHOOLID = "";

	@Value("${file.directory}")
	private String savedir;
	@Value("${file.readpath}")
	private String readdir;
	
	/*@Autowired
	private EduInfoResourceMapper eduInfoResourceMapper;*/
	
	static {
		extMap.put(Constants.FileType.IMGAE, "gif,jpg,jpeg,png,bmp");
		extMap.put(Constants.FileType.SOUND, "mp3,aac");
		extMap.put(Constants.FileType.OFFICE, "doc,docx,xls,xlsx,ppt,pptx");
		dirTypeMap.put(Constants.UploadType.SCENE, "scene");
		dirTypeMap.put(Constants.UploadType.FOOD, "food");
	}

	/**
	 * 上传单个文件
	 * 
	 * @param order
	 * @param type
	 * @param dirType UploadType
	 * @param file
	 * @return
	 * @throws BaseException
	 * @throws IOException 
	 */
	public Map<String, String> fileUpload(Integer order, String dirType, MultipartFile file) throws BaseException, IOException{
		Map<String,String> pathMap = new HashMap<String, String>();
		
		
		if(file == null){
			throw new BaseException("文件参数不能为空！");
		}
		
		String type = null;
		
		Long maxSize = DEFAULT_MAX_SIZE;
		
		//判断文件大小限制
		if(file.getSize() > maxSize){
			throw new BaseException("单个文件不能超过"+getSizeStr(maxSize + ""));
		}
		

		String fileName = file.getOriginalFilename();
		fileName = processFileName(fileName);
		
		// 检查扩展名
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		
		type = getFileTypeByExt(fileExt);
		

		if(type == null){
			throw new BaseException("不能为空");
		}
		
		if (!Constants.FileType.IMGAE.equals(type) && !Constants.FileType.SOUND.equals(type) && !Constants.FileType.OFFICE.equals(type) && !Constants.FileType.OTHER.equals(type) ) {
			throw new BaseException("类型不支持");
		}
		
		
		String resPath = null;
		String relativeDir = null;
		
		if(order != null){
			fileName = ++order + fileName;// 防客户端传多个文件但文件名相同
		}
		
		relativeDir = generateUploadDir(type, dirType);
		resPath = saveUploadFile(type, fileName, relativeDir, file,true);
		
		resPath = resPath.replace("\\", "/");
		
		pathMap.put("id", UniqueKeyUtil.getKey());
		pathMap.put("url", resPath);
		pathMap.put("name", file.getOriginalFilename());
		pathMap.put("ext", fileExt);
		pathMap.put("size", getSizeStr(file.getSize() + ""));
		return pathMap;
	}
	

	/**
	 * 根据后缀名获取文件类型
	 * 
	 * @param fileExt
	 * @return
	 */
	private String getFileTypeByExt(String fileExt) {
		
		if (Arrays.<String> asList(extMap.get(Constants.FileType.IMGAE).split(",")).contains(fileExt)) {
			return Constants.FileType.IMGAE;
		}
		
		if (Arrays.<String> asList(extMap.get(Constants.FileType.SOUND).split(",")).contains(fileExt)) {
			return Constants.FileType.SOUND;
		}
		
		if (Arrays.<String> asList(extMap.get(Constants.FileType.OFFICE).split(",")).contains(fileExt)) {
			return Constants.FileType.OFFICE;
		}
		
		return Constants.FileType.OTHER;
		
	}

	/**
	 * 获取文件大小字符串
	 * 
	 * @param size
	 * @return
	 */
	private String getSizeStr(String size) {
		String result = null;
		NumberFormat nbf = NumberFormat.getInstance();
		nbf.setMinimumFractionDigits(2);
		double tNum = (double) Integer.parseInt(size);
		if (tNum > (1024 * 1024 * 0.6)) {
			result = String.format("%.2f", tNum / (1024 * 1024)) + "M";
		} else {
			result = String.format("%.2f", tNum / 1024) + "K";
		}

		return result;
	}

	/**
	 * 批量上传多个文件
	 * 
	 * @param type
	 * @param dirType
	 * @param files
	 * @return
	 * @throws BaseException
	 */
	public List<Map<String,String>> fileUpload(String schoolId, String dirType, MultipartFile[] files) throws BaseException {
		List<Map<String,String>> filePaths = new ArrayList<>();
		int order = 0;
		SCHOOLID = StringUtils.isBlank(schoolId) ? "" : schoolId;
		for(MultipartFile file : files){
			
			Map<String, String> path = null;
			try {
				path = fileUpload(++order, dirType, file);
			} catch (IOException e) {
				logger.error("保存失败"+ ":" + file.getOriginalFilename(),e);
			}
			if(path != null){
				filePaths.add(path);
				//savePathToDb(path);
			}
		}
		SCHOOLID = "";

		return filePaths;
	}
	

	private String saveUploadFile(String type, String fileName, String relativePath, MultipartFile file, boolean saveRaw) throws BaseException, IOException{
		StringBuffer fileRelative = new StringBuffer(readdir+relativePath);
		String realpath = "";
		
		realpath = savedir+relativePath;
		
		Date now = new Date();
		
		StringBuffer preFileName = new StringBuffer(getDateString(now, "yyyyMMddHHmmss")).append("-");
		
		StringBuffer floder = new StringBuffer(getDateString(now, "yyyyMMdd"));
		File savefile = new File(new File(realpath+floder.toString()), preFileName.toString()+fileName);
		
		/*try {*/
			if(Constants.FileType.IMGAE.equals(type)) {
				fileRelative.append(floder).append(File.separator).append(preFileName.toString() + fileName);//文件相对路径

				if (saveRaw) {//保存原图
					String newName = generateNewName(fileName, IMAGE_RAW);
					File rawSavefile = new File(new File(realpath + floder.toString()), preFileName.toString() + newName);
					FileUtils.copyInputStreamToFile(file.getInputStream(), rawSavefile);
				}

				String sName = generateNewName(fileName, IMAGE_S150);
				File savefileSmall = new File(new File(realpath + floder.toString()), preFileName.toString() + sName);
				FileOutputStream fosmall = FileUtils.openOutputStream(savefileSmall);
				ImageUtils.resizeImage(file.getInputStream(), fosmall, 150, "jpg");//图片压缩

				FileOutputStream fo = FileUtils.openOutputStream(savefile);
				ImageUtils.resizeImage(file.getInputStream(), fo, 480, "jpg");
			}
			//图片压缩
//			}else{
//				StringBuffer temp = new StringBuffer(fileRelative).append(floder).append(File.separator).append(preFileName.toString()+fileName);//文件相对路径
//				FileUtils.copyInputStreamToFile(file.getInputStream(), savefile);
//				
//				if(fileName.endsWith(".aac")||fileName.endsWith("amr")){
//					String nFileName= fileName.substring(0, fileName.indexOf("."))+".mp3";
//					
//					temp = new StringBuffer(fileRelative).append(floder).append(File.separator).append(preFileName.toString()+nFileName);//文件相对路径
//					
//					String source=realpath+floder+File.separator+preFileName.toString()+fileName;
//					String target=realpath+floder+File.separator+preFileName.toString()+nFileName;
//					MediaUtil.ToMp3(source,target);
//					
//					File newFile=new File(new File(realpath+floder), preFileName.toString()+nFileName);
//					if(savefile.exists()&&savefile.isFile()&&newFile.exists()){
//						savefile.delete();
//					}
//				}
//				
//				fileRelative = temp;
			
		/*}catch (IOException e) {
			e.printStackTrace();
			throw new BaseException(ExceptionMsg.FILE_SAVE_FAILED);
		}*/
		
		return fileRelative.toString();
	}
	
	/**
	 * 图片上传
	 * 
	 * @param uploadType
	 * @param files
	 * @param width
	 * @param height
	 * @return
	 * @throws BaseException
	 */
	public List<String> imageUpload(String uploadType, MultipartFile[] files, int width, int height) throws BaseException {

		List<String> res = new ArrayList<String>();
		int order = 0;
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			fileName = processFileName(fileName);
			// 检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			
			if (!Arrays.<String> asList(extMap.get(Constants.FileType.IMGAE).split(","))
					.contains(fileExt)) {
				throw new BaseException("类型不支持");
			}

			String resPath = null;
			String relativeDir = null;
			
			if(files.length>1){
				fileName = ++order + fileName;// 防客户端传多个文件但文件名相同
			}
			
			relativeDir = generateUploadDir(Constants.FileType.IMGAE, uploadType);
			resPath = saveImage(Constants.FileType.IMGAE,fileName, relativeDir, file,true,width,height);
			
			resPath = resPath.replace("\\", "/");
			
			res.add(resPath);
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param type 文件类型：image-图片，sound-语音，file-普通文件
	 * @param dirType 上传目录类型
	 * @return
	 * @throws BaseException 
	 */
	private String generateUploadDir(String type,String dirType) throws BaseException {
		String fileDirectory= StringUtils.isBlank(dirType) ? DEFAULT : "";
		
		//按学校分文件夹
		fileDirectory = SCHOOLID + File.separator + fileDirectory;
		
		if(!fileDirectory.startsWith(File.separator)){
			fileDirectory = File.separator+fileDirectory;
		}
		
		if(!fileDirectory.endsWith(File.separator)){
			fileDirectory = fileDirectory + File.separator;
		}
		
		//添加模块目录
		if(!StringUtils.isBlank(dirType)){
			String typeDir = dirTypeMap.get(dirType);
			if(StringUtils.isBlank(typeDir)){
				throw new BaseException("类型不支持");
			}
			if(typeDir.startsWith(File.separator)){
				typeDir = typeDir.substring(typeDir.indexOf(File.separator)+1);
			}
			fileDirectory = fileDirectory + typeDir;
		}
		
		if(Constants.FileType.IMGAE.equals(type)){
			fileDirectory += Constants.FileType.IMGAE;
		}else if(Constants.FileType.SOUND.equals(type)){
			fileDirectory += Constants.FileType.SOUND;
		}else if(Constants.FileType.OFFICE.equals(type)){
			fileDirectory += Constants.FileType.OFFICE;
		}else if(Constants.FileType.OTHER.equals(type)){
			fileDirectory += Constants.FileType.OTHER;
		}else{
			throw new BaseException("类型不支持");
		}
		
		fileDirectory += File.separator;
		
		return fileDirectory;
	}
	
	/**
	 * @see FileType
	 * @param type 文件类型：image-图片，sound-语音，file-普通文件,见 FileType
	 * @param files
	 * @param directory
	 * @return
	 * @throws BaseException
	 * @throws IOException 
	 */
	public List<String> fileUpload(String type, MultipartFile[] files,
			String directory) throws BaseException {

		if (type != Constants.FileType.IMGAE && type != Constants.FileType.SOUND && type != Constants.FileType.OTHER ) {
			throw new BaseException("类型不支持");
		}

		List<String> res = new ArrayList<String>();
		int order = 0;
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			fileName = processFileName(fileName);
			// 检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			if (type == Constants.FileType.IMGAE) {
				if (!Arrays.<String> asList(extMap.get(Constants.FileType.IMGAE).split(","))
						.contains(fileExt)) {
					throw new BaseException("类型不支持");
				}
			} else {
				if (!Arrays.<String> asList(extMap.get(Constants.FileType.SOUND).split(","))
						.contains(fileExt)) {
					throw new BaseException("类型不支持");
				}
			}

			String resPath = null;
			String relativeDir = null;
			
			if(files.length>1){
				fileName = ++order + fileName;// 防客户端传多个文件但文件名相同
			}
			
			relativeDir = generateRelativeDir(type, directory);
			resPath = saveFile(type, fileName, relativeDir, file,true);
			
			resPath = resPath.replace("\\", "/");
			
			res.add(resPath);
		}
		
		return res;
	}
	
	public List<String> imageUpload(MultipartFile[] files,
                                    String directory, int width, int height) throws BaseException {

		List<String> res = new ArrayList<String>();
		int order = 0;
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			fileName = processFileName(fileName);
			// 检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			
			if (!Arrays.<String> asList(extMap.get(Constants.FileType.IMGAE).split(","))
					.contains(fileExt)) {
				throw new BaseException("类型不支持");
			}

			String resPath = null;
			String relativeDir = null;
			
			if(files.length>1){
				fileName = ++order + fileName;// 防客户端传多个文件但文件名相同
			}
			
			relativeDir = generateRelativeDir(Constants.FileType.IMGAE, directory);
			resPath = saveImage(Constants.FileType.IMGAE,fileName, relativeDir, file,true,width,height);
			
			resPath = resPath.replace("\\", "/");
			
			res.add(resPath);
		}
		
		return res;
	}
	
	/**
	 * 文件名处理不允许中文文件名
	 * @param fileName
	 * @return
	 */
	private String processFileName(String fileName){
		String regex = "[a-zA-Z0-9.]*";
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(fileName);
		if(!matcher.matches()){
			String postfix=fileName.substring(fileName.lastIndexOf("."));
			fileName = RandomUtils.nextLong(new Random())+postfix;
		}
		return fileName;
	}

	/**
	 * 
	 * @param type 文件类型：image-图片，sound-语音，file-普通文件
	 * @param directory 目录
	 * @return
	 */
	private String generateRelativeDir(String type,String directory) {
		String fileDirectory = directory;
		if(StringUtils.isBlank(fileDirectory)){
			fileDirectory = DEFAULT;
		}
		
		if(!fileDirectory.startsWith(File.separator)){
			fileDirectory = File.separator+fileDirectory;
		}
		
		if(!fileDirectory.endsWith(File.separator)){
			fileDirectory = fileDirectory + File.separator;
		}
		
		if(Constants.FileType.IMGAE.equals(type)){
			fileDirectory += Constants.FileType.IMGAE;
		}else if(Constants.FileType.SOUND.equals(type)){
			fileDirectory += Constants.FileType.SOUND;
		}else{
			fileDirectory += Constants.FileType.OTHER;
		}
		
		fileDirectory += File.separator;
		
		return fileDirectory;
	}
	
	/**
	 * 
	 * @param type 文件类型：image-图片，sound-语音，file-普通文件
	 * @param fileName 文件名称
	 * @param relativePath	相对路径
	 * @param file	原文件
	 * @param saveRaw 是否保存原图
	 * @return
	 * @throws BaseException
	 * @throws IOException 
	 */
	private String saveFile(String type, String fileName, String relativePath, MultipartFile file, boolean saveRaw) throws BaseException{
		StringBuffer fileRelative = new StringBuffer(readdir+relativePath);
		String realpath = "";
		
		realpath = savedir+relativePath;
		
		Date now = new Date();
		
		StringBuffer preFileName = new StringBuffer(getDateString(now, "yyyyMMddHHmmss")).append("-");
		
		StringBuffer floder = new StringBuffer(getDateString(now, "yyyyMMdd"));
		File savefile = new File(new File(realpath+floder.toString()), preFileName.toString()+fileName);
		
		try {
			if(Constants.FileType.IMGAE.equals(type)){
				fileRelative.append(floder).append(File.separator).append(preFileName.toString()+fileName);//文件相对路径
				
				if(saveRaw){//保存原图
					String newName = generateNewName(fileName,IMAGE_RAW);
					File rawSavefile = new File(new File(realpath+floder.toString()), preFileName.toString()+newName);
					FileUtils.copyInputStreamToFile(file.getInputStream(), rawSavefile);
				}
				
				String sName = generateNewName(fileName,IMAGE_S150);
				File savefileSmall = new File(new File(realpath+floder.toString()), preFileName.toString()+sName);
				FileOutputStream fosmall = FileUtils.openOutputStream(savefileSmall);
				ImageUtils.resizeImage(file.getInputStream(), fosmall, 150, "jpg");//图片压缩
				
				FileOutputStream fo = FileUtils.openOutputStream(savefile);
				ImageUtils.resizeImage(file.getInputStream(), fo, 640, "jpg");//图片压缩
			}
			
		}catch (IOException e) {
			e.printStackTrace();
			throw new BaseException("保存失败");
		}
		
		return fileRelative.toString();
	}
	
	private String saveImage(String type, String fileName, String relativePath, MultipartFile file, boolean saveRaw, int width, int height) throws BaseException{
		StringBuffer fileRelative = new StringBuffer(readdir+relativePath);
		String realpath = "";
		
		fileName += fileName;
		realpath = savedir+relativePath;
		
		Date now = new Date();
		
		StringBuffer preFileName = new StringBuffer(getDateString(now, "yyyyMMddHHmmss")).append("-");
		
		StringBuffer fName = new StringBuffer(preFileName);
		StringBuffer floder = new StringBuffer(getDateString(now, "yyyyMMdd"));
		
		fileRelative.append(floder).append(File.separator).append(fName.append(fileName));//文件相对路径
		try {
			if(saveRaw){//保存原图
				String newName = generateNewName(fileName,IMAGE_RAW);
				File rawSavefile = new File(new File(realpath+floder.toString()), preFileName.toString()+newName);
				FileUtils.copyInputStreamToFile(file.getInputStream(), rawSavefile);
			}
			
			ImageUtils.scaleImageWithParams(file.getInputStream(), realpath+floder+ File.separator +fName, width, height, false, "jpg");//图片压缩
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileRelative.toString();
	}
	
	private String getDateString(Date date,String pattern){
		DateFormat df = new SimpleDateFormat(pattern);
		String res = df.format(date);
		return res;
	}
	
	/**
	 * 生成原文件名称
	 * @param fileName
	 * @return
	 */
	private String generateNewName(String fileName,String addition){
		int p = fileName.lastIndexOf(".");
		String n = fileName.substring(0, p);
		String fix = fileName.substring(p, fileName.length());
		return n+addition+fix;
	}
	
	private static final String IMAGE_RAW = "-raw";
	private static final String IMAGE_SMALL = "-small";
	private static final String IMAGE_S150 = "-s150";
}
