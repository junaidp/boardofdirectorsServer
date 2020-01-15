package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;

public class CalculationFTA extends Calculation{
	
	
	public CalculationFTA() throws IOException, InvalidFormatException {
		super();
		// TODO Auto-generated constructor stub
	}


	public String entryFirstTimeAdoption(Entry entry, TYPESFTA outPutTab, TYPESFTA inputTab) throws Exception{

		try {


			OPCPackage pkg;
			System.out.println("opening file");
			InputStream file = getFile("Firsttimeadoption");
			System.out.println("back from return");
			pkg = OPCPackage.open(file);


			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			
		//	Sheet sheetOutPut = wb.getSheet("Retrospective Journalentry");
			Sheet sheet = wb.getSheetAt(inputTab.getValue());
			
			
			
			
			System.out.println(sheet.getSheetName());
			updateValues(entry, sheet);	
			System.out.println("updating");
		//	printValues(sheet);

			switch(outPutTab) {
			case RETROSPECTIVE:
				json = calculateFTA(wb, entry, outPutTab.getValue(), inputTab.getValue());
				break;
			
			
			default:
				break;
			}

			System.out.println("calculation done ");
			//	json =  gson.toJson(map);
			System.out.println("converted to json");
			wb.close();
			System.out.println("returning json");
			return json;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("In exception" + e);
			throw e;
		}

	}
	
	
	public String calculateFTA(XSSFWorkbook wb, Entry entry, int outputTab, int inputTab) throws InvalidFormatException, IOException {


		System.out.println("calculating First Time Adoption");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		Sheet sheet = wb.getSheet("Retrospective Journalentry");
		
		Sheet sheetLease = wb.getSheetAt(inputTab);
		//sheetLease.getRow(4).getCell(0)
		//LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRow = 16;
		
			
		for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(row>= startingRow && row<= leaseTerms+16)
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(0);

				evaluateCell(evaluator, c);

			
				
			}


		}
		CellValue cellValue = evaluator.evaluate(sheet.getRow(8).getCell(1));
		
		  if (cellValue.getCellType() == CellType.ERROR) {
			   System.out.println("error cell value-"+ FormulaError.forInt(cellValue.getErrorValue()).getString());
			  }
		
		evaluateCell(evaluator, sheet.getRow(8).getCell(1),sheet.getRow(9).getCell(1),sheet.getRow(10).getCell(1), sheet.getRow(11).getCell(1), sheet.getRow(12).getCell(1), sheet.getRow(13).getCell(1));
		
		  
		evaluateCell(evaluator, sheet.getRow(15).getCell(1));
		map.put("leseLiabality", sheet.getRow(15).getCell(1).getNumericCellValue()+"");
		
		evaluateCell(evaluator, sheet.getRow(28).getCell(1));
		map.put("RightToUse", sheet.getRow(28).getCell(1).getNumericCellValue()+"");
		
		evaluateCell(evaluator, sheet.getRow(29).getCell(1));
		map.put("RetainedEarning", sheet.getRow(29).getCell(1).getErrorCellValue()+"");

		
		Gson gson = new Gson(); 
		return gson.toJson(map);
		
	}
	

}
