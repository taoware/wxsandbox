package com.irengine.sandbox.web.rest.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtil {

	private static String DIRECTORY_UPLOAD = "/uploaded/";

	private static Logger logger = LoggerFactory
			.getLogger(UploadFileUtil.class);

	private static String getWebDirectory(HttpServletRequest request) {
		return request.getSession().getServletContext()
				.getRealPath(DIRECTORY_UPLOAD);
	}

	public static Map<String, String> uploadAndExtractFile(MultipartFile file,
			HttpServletRequest request) throws IOException {
		/* 返回值设置 */
		Map<String, String> map = new HashMap<String, String>();
		map.put("msg", "");
		map.put("name", "");
		String uploadDirectoryName = getWebDirectory(request);
		/* 删除该目录下的文件 */
		File deleteFile = new File(uploadDirectoryName);
		if (deleteFile.isDirectory()) {
			File[] deleteFiles = deleteFile.listFiles();
			if (deleteFiles.length > 0) {
				for (int i = 0; i < deleteFiles.length; i++) {
					deleteFiles[i].delete();
				}
			}
		}
		/* 上传文件 */
		if (!file.isEmpty()) {
			/*
			 * 判断文件后缀名是否为zip,(未完成)
			 */
			File uploadDirectory = new File(uploadDirectoryName);
			FileUtils.forceMkdir(uploadDirectory);
			logger.debug("-----------fileName:" + file.getOriginalFilename());
			File uploadFile = new File(uploadDirectoryName + "/"
					+ file.getOriginalFilename());
			FileUtils.writeByteArrayToFile(uploadFile, file.getBytes());
			String name = unZipFiles(uploadFile);
			map.put("name", name);
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	private static String unZipFiles(File zipFile) throws IOException {
		// 得到压缩文件所在目录
		String path = zipFile.getAbsolutePath();
		logger.debug("---------path:" + path);
		path = path.substring(0, path.lastIndexOf("/"));
		@SuppressWarnings("resource")
		ZipFile zip = new ZipFile(zipFile);
		String name = "" + System.currentTimeMillis();
		Enumeration entries1 = zip.entries();
		ZipEntry entry1 = (ZipEntry) entries1.nextElement();
		// String name = entry1.getName().substring(0,
		// entry1.getName().length()-1);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			zipEntryName = name
					+ zipEntryName.substring(zipEntryName.indexOf("/"));
			InputStream in = zip.getInputStream(entry);
			// outPath输出目录
			String outPath = "";
			if (zipEntryName.indexOf(".html") != -1) {
				outPath = path.substring(0,
						path.lastIndexOf("/") < 0 ? path.lastIndexOf("\\")
								: path.lastIndexOf("/"))
						+ "/WEB-INF/classes/views/" + zipEntryName;
			} else {
				outPath = path.substring(0,
						path.lastIndexOf("/") < 0 ? path.lastIndexOf("\\")
								: path.lastIndexOf("/"))
						+ "/WEB-INF/classes/static/web/" + zipEntryName;
			}
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		return name;
	}

}
