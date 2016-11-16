package com.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import com.app.constants.Constant;
import com.app.constants.ResourceConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;


public class FileUploadUtil {
	private static final Logger logger=LogManager.getLogger(FileUploadUtil.class);

	private static final String[] FILE_EXTS = { "JPG", "PNG", "GIF" };

	private static final byte[][] FILE_MAGS = new byte[][] { new byte[] { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0 }, // JPG
			new byte[] { (byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47 }, // PNG
			new byte[] { (byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38 } // GIF
	};

	/**
	 * 使用springmvc组件处理文件上传
	 * @param files
	 */
	public static String fileUpload(MultipartFile file,String path) {
		try {
			String fileName=file.getOriginalFilename();
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			fileName=UUIDUtil.getUUID()+"."+fileType;
			 // 文件保存路径  
            String filePath =path+fileName; 
            // 转存文件  
            file.transferTo(new File(filePath));
            return fileName;
		} catch (Exception e) {
			logger.error("文件上传失败----", e);
		}
		return null;
	}

	/**
	 * 字节流保存上传图片
	 * @param is
	 * @param path
	 * @return
	 */
	public static boolean byteUpload(byte[] data, String path) {
		boolean flag = false;
		File file = new File(path);
		try {
			OutputStream os = new FileOutputStream(file);
			os.write(data);
			os.close();
			flag = true;
		} catch (Exception e) {
			logger.error("上传图片失败 ----", e);
		}
		return flag;
	}

	/**
	 * 通过文件流判断文件类型
	 * @param contents file contents
	 * @return file format, null if unsupported.
	 */
	public static String getFileType(byte[] contents) {
		for (int i = 0; i < FILE_MAGS.length; i++) {
			byte[] mag = FILE_MAGS[i];
			if (contents.length >= mag.length) {
				if (Arrays.equals(Arrays.copyOf(contents, mag.length), mag)) {
					return FILE_EXTS[i];
				}
			}
		}
		return null;
	}
	/**
	 * 验证上传文件类型
	 * @param fileType
	 * @return
	 */
	public static boolean validateFileType(String fileType){
		String allowFileType = Constant.ALLOW_FILE_TYPE;
		String[] split = allowFileType.split(",");
		int status=0;
		for (String type:split) {
			if (type.equalsIgnoreCase(fileType)){
				status=1;
			}
		}
		if (status==1){
			return true;
		}
		return false;
	}
}
