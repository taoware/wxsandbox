package com.irengine.sandbox.web.rest.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil {

	public static void downloadExcel(String title, HSSFWorkbook workbook,
			HttpServletResponse response) throws IOException {
		/* 生成excel */
		OutputStream out = response.getOutputStream();
		response.reset();
		response.setContentType("application/x-msdownload");
		String pName = title;
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(pName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
		try {
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
}
