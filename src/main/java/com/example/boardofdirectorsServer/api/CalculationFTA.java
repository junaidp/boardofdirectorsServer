package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
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
			case CUMMULATIVE:
				json = calculateFTA(wb, entry, outPutTab.getValue(), inputTab.getValue());
				break;
			case LEASE:
				json = calculateFTALease(wb, entry, outPutTab.getValue());
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

	public String calculateFTALease(XSSFWorkbook wb, Entry entry, int leaseType) throws InvalidFormatException, IOException {


		System.out.println("calculating FTA Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = wb.getSheetAt(leaseType);

		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;

		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 16 && count < leaseTerms)
			{

				LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();

				System.out.println("In Row" +r.getRowNum());
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try{
							evaluator.evaluateFormulaCell(c);
						}catch(Exception ex){
							System.out.println("In Exception in loop" + ex);
							mapRow.put(c.getColumnIndex()+"", ":"+"");
							System.out.println("In error"+ ex);
						}
						cellType = c.getCachedFormulaResultType();
					}
					else
					{
						cellType = c.getCellType();
					}
					putinMap(mapRow, c, cellType);
				}
				mapSheet.put(row+1+"", mapRow);
				count ++;
			}

		}


		System.out.println("returning FTA  map");
		Gson gson = new Gson(); 
		return gson.toJson(mapSheet);

	}


	public String calculateFTA(XSSFWorkbook wb, Entry entry, int outputTab, int inputTab) throws InvalidFormatException, IOException {


		System.out.println("calculating First Time Adoption");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		Sheet sheetRetrospective = wb.getSheet("Retrospective Journalentry");

		Sheet sheetCumulative = wb.getSheet("Cumulative Catchup Journalentry");


		Sheet sheetLease = wb.getSheet("Lease");
		System.out.println("In sheet" +sheetRetrospective.getSheetName());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRowLease = 16;
	

		for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(row>= startingRowLease)
			{
				evaluateCell(evaluator, r.getCell(1));
				if(r.getCell(1).getNumericCellValue() == sheetRetrospective.getRow(14).getCell(1).getNumericCellValue())
				{
					Cell c = r.getCell(3);
					evaluateCell(evaluator, c);
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						System.out.println("VALUE:"+sheetRetrospective.getRow(5).getCell(1).getDateCellValue());
						sheetRetrospective.getRow(5).getCell(1).setCellValue(date);
						System.out.println("VALUE:"+sheetRetrospective.getRow(5).getCell(1).getDateCellValue());
						
						break;
					}
				}
			}
		}
	
		evaluateCell(evaluator, sheetRetrospective.getRow(15).getCell(1));
		map.put("leseLiabality", sheetRetrospective.getRow(15).getCell(1).getNumericCellValue()+"");

		evaluateCell(evaluator, sheetRetrospective.getRow(28).getCell(1));
		map.put("RightToUse", sheetRetrospective.getRow(28).getCell(1).getNumericCellValue()+"");

		evaluateCell(evaluator, sheetRetrospective.getRow(29).getCell(1));
		map.put("RetainedEarning", sheetRetrospective.getRow(29).getCell(1).getNumericCellValue()+"");

		Gson gson = new Gson(); 
		return gson.toJson(map);

	}

	public int getCell(String cellNo, Sheet sheet)
	{
		int cell = CellReference.convertColStringToIndex("B");
		return cell;

	}

}
