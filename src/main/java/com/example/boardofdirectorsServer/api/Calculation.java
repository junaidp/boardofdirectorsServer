package com.example.boardofdirectorsServer.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class Calculation {


	///ifrs.xls

	public Calculation() throws IOException, InvalidFormatException
	{
		calculate();
	}

	public HashMap<String, HashMap<String, String>> calculate() throws InvalidFormatException, IOException {
		OPCPackage pkg = OPCPackage.open(new File("/Users/junaidparacha/Downloads/ifrs.xlsx"));
		//OPCPackage pkg = OPCPackage.open(new File("C:\\Users\\jparacha\\git\\boardofdirectorsServer\\src\\main\\resources\\static\\ifrs.xlsx"));
		//	Workbook wb = new XSS(fis); //or new XSSFWorkbook("/somepath/test.xls")
		XSSFWorkbook wb = new XSSFWorkbook(pkg);

		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		for (Sheet sheet : wb) {
			HashMap<String, String> mapSheet = new HashMap<String, String>();

			for (Row r : sheet) {
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try{
							evaluator.evaluateFormulaCell(c);
						}catch(Exception ex){
							mapSheet.put(""+c.getRow().getRowNum()+"/"+c.getColumnIndex()+"", ":"+"");
							System.out.println(ex);
						}
						cellType = c.getCachedFormulaResultType();
						}
					else
					{
						cellType = c.getCellType();
					}
					putinMap(map, sheet, mapSheet, c, cellType);
				}
			}
			map.put(sheet.getSheetName(), mapSheet);
			
		}

		return map;
	}

	private void putinMap(HashMap<String, HashMap<String, String>> map, Sheet sheet, HashMap<String, String> mapSheet,
			Cell c, CellType cellType) {
		
		int rowNum = c.getRow().getRowNum();
		String cellLocation = rowNum+1 +"/"+c.getColumnIndex();
		
		switch(cellType) {
		
		
		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(c)) {
				mapSheet.put(""+cellLocation+"", c.getColumnIndex()==9?  month(c.getDateCellValue().getMonth())+"": c.getDateCellValue()+"");
			}
			else {
				mapSheet.put(""+cellLocation+"", c.getNumericCellValue()+"");
			}
			break;
		case STRING:
			mapSheet.put(""+cellLocation+"", c.getRichStringCellValue()+"");
			break;
		case BOOLEAN:
			mapSheet.put(""+cellLocation+"", c.getBooleanCellValue()+"");
		default:
			mapSheet.put(""+cellLocation+"", "-"+"");
			break;
		}
	}

	private String month(int num)
	{
		switch(num)
		{
		case 0:
			return "Jan";
		case 1:
			return "Feb";
		case 2:
			return "Mar";
		case 3:
			return "Apr";
		case 4:
			return "May";
		case 5:
			return "Jun";
		case 6:
			return "Jul";
		case 7:
			return "Aug";
		case 8:
			return "Sep";
		case 9:
			return "Oct";
		case 10:
			return "Nov";
		case 11:
			return "Dec";
		}
		return null;
		
	}
	}
