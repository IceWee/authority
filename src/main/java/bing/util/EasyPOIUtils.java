package bing.util;

import java.util.Collection;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;

/**
 * EasyPOI工具类
 * 
 * @author IceWee
 */
public class EasyPOIUtils {

	public static final String DEFAULT_SHEET = "sheet";

	/**
	 * 导出Excel
	 * 
	 * @param clazz
	 * @param rows
	 * @return
	 */
	public static Workbook exportExcel(Class<?> clazz, Collection<?> rows) {
		return exportExcel(DEFAULT_SHEET, clazz, rows);
	}

	/**
	 * 导出Excel
	 * 
	 * @param sheetName
	 * @param clazz
	 * @param rows
	 * @return
	 */
	public static Workbook exportExcel(String sheetName, Class<?> clazz, Collection<?> rows) {
		ExportParams params = new ExportParams();
		params.setSheetName(sheetName);
		return ExcelExportUtil.exportExcel(params, clazz, rows);
	}

}
