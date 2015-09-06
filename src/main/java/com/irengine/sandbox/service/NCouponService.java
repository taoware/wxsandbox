package com.irengine.sandbox.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.domain.NCoupon;
import com.irengine.sandbox.web.rest.util.ExcelUtil;

@Service
@Transactional
public class NCouponService {

	public void createExcel(List<NCoupon> nCoupons, HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("提货码");
		//行宽
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		/*表格样式*/
		HSSFCellStyle normalStyle = workbook.createCellStyle();
		normalStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		normalStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		normalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		/*表格内容*/
		//第一行
		String[] fields=new String[]{"id","提货码","状态","创建时间","修改时间"};
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 400);
		for(int i=0;i<fields.length;i++){
			HSSFCell ce1_1 = row.createCell(i);
			ce1_1.setCellValue(fields[i]);
			ce1_1.setCellStyle(normalStyle);
		}
		int num=1;
		for(NCoupon nCoupon:nCoupons){
			HSSFRow rowN = sheet.createRow(num);
			for(int i=0;i<fields.length;i++){
				HSSFCell cen_i = rowN.createCell(i);
				switch (i) {
				case 1:
					cen_i.setCellValue(nCoupon.getId());
					break;
				case 2:
					cen_i.setCellValue(nCoupon.getCode());
					break;
				case 3:
					cen_i.setCellValue(nCoupon.getStatus().toString());
					break;
				case 4:
					cen_i.setCellValue(nCoupon.getCreatedTime().toString());
					break;
				case 5:
					cen_i.setCellValue(nCoupon.getModifedTime().toString());
					break;
				default:
					break;
				}
				cen_i.setCellStyle(normalStyle);
			}
			num++;
		}
		ExcelUtil.downloadExcel("提货码", workbook, response);
	}

}
