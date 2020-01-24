package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.util.CellReference;
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
			Sheet sheet = wb.getSheet("Lease");
			
			
			
			
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
		Sheet sheetRetrospective = wb.getSheet("Retrospective Journalentry");
		
		Sheet sheetCumulative = wb.getSheet("Cumulative Catchup Journalentry");
		
		
		Sheet sheetLease = wb.getSheet("Lease");
		//sheetLease.getRow(4).getCell(0)
		//LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheetRetrospective.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRowLease = 16;
		int startingCatchUp = 4;
		
			
		/*for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(row>= startingRowLease)
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
					for (Cell c : r) {
						System.out.println("In cell" +c.getColumnIndex());
				evaluateCell(evaluator, c);

				}
				
			}


		}
		
		for (Row r : sheetCumulative) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(row>= startingCatchUp)
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
					for (Cell c : r) {
						System.out.println("In cell" +c.getColumnIndex());
				evaluateCell(evaluator, c);

				}
				
			}


		}
		
		for (Row r : sheetRetrospective) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(row>= startingCatchUp)
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
					for (Cell c : r) {
						System.out.println("In cell" +c.getColumnIndex());
				evaluateCell(evaluator, c);

				}
				
			}


		}
		
	//	evaluateCell(evaluator,sheet.getRow(4).getCell(1), sheet.getRow(5).getCell(1),sheet.getRow(6).getCell(1),sheet.getRow(7).getCell(1), sheet.getRow(8).getCell(1),sheet.getRow(9).getCell(1),sheet.getRow(10).getCell(1), sheet.getRow(11).getCell(1), sheet.getRow(12).getCell(1), sheet.getRow(13).getCell(1));
/*		evaluateCell(evaluator, sheetRetrospective.getRow(28).getCell(getCell("B", sheetRetrospective)));
		evaluateCell(evaluator, sheetRetrospective.getRow(15).getCell(getCell("B", sheetRetrospective)));
	//	 evaluator.evaluate(sheet.getRow(5).getCell(1));
		CellValue cellValue = evaluator.evaluate(sheetRetrospective.getRow(15).getCell(1));
		
		  if (cellValue.getCellType() == CellType.ERROR) {
			   System.out.println("error cell value-"+ FormulaError.forInt(cellValue.getErrorValue()).getString());
			  }
		
		*/
		
	/*	Calendar c = Calendar.getInstance();
		c.set(2017, 3, 3);
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, 2);
		c.set(Calendar.DAY_OF_MONTH, 3);
		Date date = c.getTime();
		
		sheetRetrospective.getRow(5).getCell(1).setCellValue(date);
		sheetRetrospective.getRow(6).getCell(1).setCellValue(date);
		*/
	//	evaluateInCell(evaluator, sheetLease.getRow(35).getCell(3));
		
	//	evaluateInCell(evaluator, sheetRetrospective.getRow(8).getCell(1));
		
	//	evaluateInCell(evaluator, sheetRetrospective.getRow(5).getCell(1), sheetRetrospective.getRow(6).getCell(1));
		  
//		evaluateInCell(evaluator, sheetRetrospective.getRow(5).getCell(1));
//		map.put("leseLiabality", sheetRetrospective.getRow(5).getCell(1).getDateCellValue()+"");
		
	//	evaluateInCell(evaluator, sheetRetrospective.getRow(28).getCell(1));
		map.put("RightToUse", sheetRetrospective.getRow(28).getCell(1).getNumericCellValue()+"");
		
	//	evaluateInCell(evaluator, sheetRetrospective.getRow(29).getCell(1));
		map.put("RetainedEarning", sheetRetrospective.getRow(29).getCell(1).getNumericCellValue()+"");
		
		map.put("leseLiabality", sheetRetrospective.getRow(15).getCell(1).getNumericCellValue()+"");

		
		Gson gson = new Gson(); 
		return gson.toJson(map);
		
	}
	
	public int getCell(String cellNo, Sheet sheet)
	{
		int cell = CellReference.convertColStringToIndex("B");
		return cell;
		
	}
	

}
