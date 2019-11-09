package com.example.boardofdirectorsServer.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;



public class Calculation {

	private String json="";

	///ifrs.xls

	public Calculation() throws IOException, InvalidFormatException
	{

	}

	public String entry(Entry entry) throws Exception{
		Gson gson;
		try {
			 gson = new Gson(); 
		json =  gson.toJson(entry);
		OPCPackage pkg;
		json = json+"2";
		
			File file = getFile();
			json = json+"3";
			//pkg = OPCPackage.open(new File("/Users/junaidparacha/Downloads/ifrs.xlsx"));
			pkg = OPCPackage.open(file);
			json = json+"4";
			
			json = json+"5";
			 json = gson.toJson(entry);
			 json = json+"6";
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheetLease = wb.getSheetAt(0);
			System.out.println(sheetLease.getRow(3).getCell(0).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(1).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(2).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(3).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(4).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(5).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(6).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(7).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(8).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(9).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(10).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(11).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(12).getCellType());
			System.out.println(sheetLease.getRow(3).getCell(13).getCellType());

			printValues(sheetLease);
			updateValues(entry, sheetLease);	
			System.out.println("updating");
			printValues(sheetLease);

			LinkedHashMap<String, LinkedHashMap<String, String>> map = calculate(wb);
			json = json+"1"+map;
					
			wb.close();
			return json;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			json = json +e;
			throw e;
		}

	}

	private File getFile() throws Exception {
		String fileName = "static/ifrs.xlsx";
        ClassLoader classLoader = new Calculation().getClass().getClassLoader();
 
        File file = new File(classLoader.getResource(fileName).getFile());
         
        //File is found
        System.out.println("File Found : " + file.exists());
         
        //Read File Content
        String content = new String(Files.readAllBytes(file.toPath()));
        System.out.println(content);
        return file;
	}

	private void updateValues(Entry entry, Sheet sheetLease) {
		sheetLease.getRow(3).getCell(0).setCellValue(entry.getLeaseContractNo());	
		sheetLease.getRow(3).getCell(1).setCellValue(entry.getCommencementDate());	
		sheetLease.getRow(3).getCell(2).setCellValue(entry.getPaymentsAt());	
		sheetLease.getRow(3).getCell(3).setCellValue(entry.getAnnualDiscountRate());	
		sheetLease.getRow(3).getCell(4).setCellValue(entry.getLeaseTerm());	
		sheetLease.getRow(3).getCell(5).setCellValue(entry.getExpectedPeriod());	
		sheetLease.getRow(3).getCell(6).setCellValue(entry.getLeasePayment());	
		sheetLease.getRow(3).getCell(7).setCellValue(entry.getPaymentIntervals());	
		sheetLease.getRow(3).getCell(8).setCellValue(entry.getInitialDirectCost());	
		sheetLease.getRow(3).getCell(9).setCellValue(entry.getGuaranteedResidualValue());	
		//sheetLease.getRow(3).getCell(10).setCellValue(entry.getLeaseContractNo());	
		sheetLease.getRow(3).getCell(11).setCellValue(entry.getUsefulLifeOfTheAsset());	
		sheetLease.getRow(3).getCell(12).setCellValue(entry.getEscalation());	
		sheetLease.getRow(3).getCell(13).setCellValue(entry.getEscalationAfterEvery());
	}

	private void printValues(Sheet sheetLease) {
		System.out.println(sheetLease.getRow(3).getCell(0).getStringCellValue());
		System.out.println(sheetLease.getRow(3).getCell(1).getDateCellValue());
		System.out.println(sheetLease.getRow(3).getCell(2).getStringCellValue());
		System.out.println(sheetLease.getRow(3).getCell(3).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(4).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(5).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(6).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(7).getStringCellValue());
		System.out.println(sheetLease.getRow(3).getCell(8).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(9).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(10).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(11).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(12).getNumericCellValue());
		System.out.println(sheetLease.getRow(3).getCell(13).getNumericCellValue());
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> calculate(XSSFWorkbook wb) throws InvalidFormatException, IOException {

		//	ClassPathResource res = new ClassPathResource("ifrs.xlsx");
		//File file = new File(res.getPath());


		//OPCPackage pkg = OPCPackage.open(new File("/Users/junaidparacha/Downloads/ifrs.xlsx"));
		//OPCPackage pkg = OPCPackage.open(file);
		//OPCPackage pkg = OPCPackage.open(new File("C:\\Users\\jparacha\\git\\boardofdirectorsServer\\src\\main\\resources\\static\\ifrs.xlsx"));
		//	Workbook wb = new XSS(fis); //or new XSSFWorkbook("/somepath/test.xls")


		//XSSFWorkbook wb = new XSSFWorkbook(pkg);

		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		LinkedHashMap<String, LinkedHashMap<String, String>> map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		for (Sheet sheet : wb) {
			LinkedHashMap<String, String> mapSheet = new LinkedHashMap<String, String>();

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
		System.out.println(map);
		return map;
	}

	private void putinMap(LinkedHashMap<String, LinkedHashMap<String, String>> map, Sheet sheet, LinkedHashMap<String, String> mapSheet,
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
