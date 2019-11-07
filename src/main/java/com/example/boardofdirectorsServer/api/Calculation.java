package com.example.boardofdirectorsServer.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

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
	//	FileInputStream fis = new FileInputStream("/Users/junaidparacha/Downloads/ifrs.xlsx");
		OPCPackage pkg = OPCPackage.open(new File("/Users/junaidparacha/Downloads/ifrs.xlsx"));
	//	Workbook wb = new XSS(fis); //or new XSSFWorkbook("/somepath/test.xls")
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		for (Sheet sheet : wb) {
			HashMap<String, String> mapSheet = new HashMap<String, String>();
			
			for (Row r : sheet) {
		        for (Cell c : r) {
		        	System.out.println(c.getCellType());
		        	if (c.getCellType() == CellType.FORMULA) {
		        		try{
		        		evaluator.evaluateFormulaCell(c);
		        		}catch(Exception ex){
		        			System.out.println(ex);
		        		}
		                
		        		switch(c.getCachedFormulaResultType()) {
		                case NUMERIC:
		                    System.out.println("Last evaluated as: " + c.getNumericCellValue());
		                    mapSheet.put(""+c.getRowIndex()+"/"+c.getColumnIndex()+"", c.getNumericCellValue()+"");
		                    map.put(sheet.getSheetName(), mapSheet);
		                    break;
		                case STRING:
		                    System.out.println("Last evaluated as \"" + c.getRichStringCellValue() + "\"");
		                    mapSheet.put(""+c.getRowIndex()+"/"+c.getColumnIndex()+"", c.getRichStringCellValue()+"");
		                    map.put(sheet.getSheetName(), mapSheet);
		                    break;
						default:
							break;
		            }
		        		
		            }
		        }
		    }
		}
		
		System.out.println("RESULT>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(map);
	}
}
