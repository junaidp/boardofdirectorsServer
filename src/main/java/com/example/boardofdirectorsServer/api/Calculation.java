package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;



public class Calculation {

	protected String json="";

	///ifrs.xls

	public Calculation() throws IOException, InvalidFormatException
	{

	}

	public String entry(Entry entry) throws Exception{
		Gson gson;
		try {
			gson = new Gson(); 

			OPCPackage pkg;
			InputStream file = getFileLease();
			pkg = OPCPackage.open(file);

			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheetLease = wb.getSheetAt(0);

			updateValues(entry, sheetLease);	
			System.out.println("updating");
			//	printValues(sheetLease);

			LinkedHashMap<String, LinkedHashMap<String, String>> map = calculate(wb);
			System.out.println("calculation done ");
			json =  gson.toJson(map);
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

	public String entryLease(Entry entry, int leaseType) throws Exception{
		Gson gson;
		try {
			gson = new Gson(); 

			OPCPackage pkg;
			InputStream file = getFileLease();
			pkg = OPCPackage.open(file);

			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheetLease = wb.getSheetAt(leaseType);

			updateValues(entry, sheetLease);	
			System.out.println("updating");

			LinkedHashMap<String, LinkedHashMap<String, String>> map = calculateLease(wb, entry, leaseType);
			System.out.println("calculation done ");
			json =  gson.toJson(map);
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

	public String entryJournal(Entry entry, TYPES typeJournal, TYPES typeLease) throws Exception{

		try {


			OPCPackage pkg;
			System.out.println("opening file");
			InputStream file = getFileJournal();
			System.out.println("back from return");
			pkg = OPCPackage.open(file);


			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheetLease = wb.getSheetAt(typeLease.getValue());

			updateValues(entry, sheetLease);	
			System.out.println("updating");


			switch(typeJournal) {
			case JOURNAL_YEARLY:
				json = calculateJournal(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case JOURNAL_QUARTERLY:
				json = calculateJournalQuarterly(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case JOURNAL_MONTHLY:
				json = calculateJournalMonthly(wb, entry, typeJournal.getValue(), typeLease.getValue());
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

	private void printTypes(Sheet sheetLease) {
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
	}

	private InputStream getFileLease() throws Exception {
		String fileName = "static/Journal.xlsx";
		ClassLoader classLoader =  this.getClass().getClassLoader();

		// File file = new File(classLoader.getResource(fileName).getFile());
		InputStream file = classLoader.getResourceAsStream(fileName);
		//File is found
		// System.out.println("File Found : " + file.exists());

		//Read File Content
		////  String content = new String(Files.readAllBytes(file.toPath()));
		// System.out.println(content);
		return file;
	}

	protected InputStream getFileJournal() throws Exception {
		String fileName = "static/Journal.xlsx";
		System.out.println("opening file"+ fileName);
		ClassLoader classLoader =  this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}
	
	public InputStream getFile(String fileToOpen) throws Exception {
		String fileName = "static/Firsttimeadoption.xlsx";
		System.out.println("opening file"+ fileName);
		ClassLoader classLoader =  this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}

	protected void updateValues(Entry entry, Sheet sheetLease) {
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

	protected void printValues(Sheet sheetLease) {
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
		System.out.println("going to update");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		System.out.println("154");
		LinkedHashMap<String, LinkedHashMap<String, String>> map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("starting loop");
		for (Sheet sheet : wb) {
			LinkedHashMap<String, String> mapSheet = new LinkedHashMap<String, String>();
			System.out.println("In sheet" +sheet.getSheetName());
			for (Row r : sheet) {
				///ONLY PUT COLUMN No in map id
				System.out.println("In Row" +r.getRowNum());
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try{
							evaluator.evaluateFormulaCell(c);
						}catch(Exception ex){
							System.out.println("In Exception in loop" + ex);
							mapSheet.put(""+c.getRow().getRowNum()+"/"+c.getColumnIndex()+"", ":"+"");
							System.out.println("In error" + ex);
						}
						cellType = c.getCachedFormulaResultType();
					}
					else
					{
						cellType = c.getCellType();
					}
					putinMap(mapSheet, c, cellType);
				}
			}
			map.put(sheet.getSheetName(), mapSheet);

		}
		//System.out.println(map);
		System.out.println("returning map");
		return map;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> calculateLease(XSSFWorkbook wb, Entry entry, int leaseType) throws InvalidFormatException, IOException {


		System.out.println("calculating Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		//	XSSFSheet sheet = wb.getSheet("New Lease Yearly");
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
							System.out.println("In error" + ex);
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


		System.out.println("returning Lease map");
		return mapSheet;
	}

	public String calculateJournalLeftSide(XSSFWorkbook wb, Entry entry, TYPES typeJournal) throws InvalidFormatException, IOException {


		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		//XSSFSheet sheet = wb.getSheet("Yearly Journal entry");
		XSSFSheet sheet = wb.getSheetAt(typeJournal.getValue());


		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;

		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();
			int startingRow = 5;
			if(typeJournal == TYPES.JOURNAL_MONTHLY)startingRow = 4;

			if(r.getRowNum()>= startingRow && count < leaseTerms)
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
							System.out.println("In error" + ex);
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


		System.out.println("returning Journal map");
		Gson gson = new Gson(); 
		return  gson.toJson(mapSheet);

	}

	public String calculateJournal(XSSFWorkbook wb, Entry entry, int journalType, int leaseType) throws InvalidFormatException, IOException {


		System.out.println("calculating Journal Yearly");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);//.getSheet("Yearly Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);

		//LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 5 && count < leaseTerms)
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(0);

				evaluateCell(evaluator, c);

				System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					if(date.getYear() == entry.getYear()){
						Row selectedRow = r;
						//evaluateCell(evaluator, selectedRow.getCell(5), selectedRow.getCell(6), selectedRow.getCell(7), selectedRow.getCell(8), selectedRow.getCell(9), selectedRow.getCell(10), selectedRow.getCell(11), selectedRow.getCell(12), selectedRow.getCell(13), selectedRow.getCell(14), selectedRow.getCell(15), selectedRow.getCell(16), selectedRow.getCell(17));
						//map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
					//	entry.getCommencementDate();
						Cell monthCell =selectedRow.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));
						evaluateCell(evaluator, monthCell);
						map.put("dr", monthCell.getNumericCellValue()+"");
						
						evaluateCell(evaluator, selectedRow.getCell(5), selectedRow.getCell(6), selectedRow.getCell(7), selectedRow.getCell(8), selectedRow.getCell(9), selectedRow.getCell(10), selectedRow.getCell(11), selectedRow.getCell(12), selectedRow.getCell(13), selectedRow.getCell(14), selectedRow.getCell(15), selectedRow.getCell(16));
						
						Row upRow = sheet.getRow(row-1);
						if(upRow.getRowNum() == 4)
						{
							map.put("total", "0");
							map.put("RepeatmonthAccrued", "0");
						}
						else{
							evaluateCell(evaluator, upRow.getCell(5), upRow.getCell(6), upRow.getCell(7), upRow.getCell(8), upRow.getCell(9), upRow.getCell(10), upRow.getCell(11), upRow.getCell(12), upRow.getCell(13), upRow.getCell(14), upRow.getCell(15), upRow.getCell(16));
							
						double total = upRow.getCell(5).getNumericCellValue()+
								upRow.getCell(6).getNumericCellValue()+
								upRow.getCell(7).getNumericCellValue()+
								upRow.getCell(8).getNumericCellValue()+
								upRow.getCell(9).getNumericCellValue()+
								upRow.getCell(10).getNumericCellValue()+
								upRow.getCell(11).getNumericCellValue()+
								upRow.getCell(12).getNumericCellValue()+
								upRow.getCell(13).getNumericCellValue()+
								upRow.getCell(14).getNumericCellValue()+
								upRow.getCell(15).getNumericCellValue()+
								upRow.getCell(16).getNumericCellValue();

						map.put("total", total+"");
						
						
							
						}
						if(entry.getPaymentsAt().equalsIgnoreCase("Beginning"))
						{
							evaluateCell(evaluator, selectedRow.getCell(17));
							map.put("repeat", selectedRow.getCell(17).getNumericCellValue()+"");
						}
						else
						{
							
							if(upRow.getRowNum() == 4)
							{
								map.put("repeat", "");
							}
							else {
							evaluateCell(evaluator, upRow.getCell(17));
							map.put("repeat", upRow.getCell(17).getNumericCellValue()+"");
							
							Row upRow2 = sheet.getRow(upRow.getRowNum()-1);
							
							if(upRow2.getRowNum() != 4)
							{
							evaluateCell(evaluator, upRow2.getCell(17));
							map.put("RepeatmonthAccrued", upRow2.getCell(17).getNumericCellValue()+"");
							}
							else{
								map.put("RepeatmonthAccrued", "0");
									
							}
							
							}
						}

						//Gson gson = new Gson(); 
						//	return  gson.toJson(map);
						break;


					}

				}
				
			}



			//mapSheet.put(row+1+"", mapRow);
			


		}
		
		for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 16 && row < leaseTerms+16)
			{

				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(2);
				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try{
						date = c.getLocalDateTimeCellValue();
					}
					catch(Exception ex)
					{
						System.out.println(ex);
					}
					if(date.getYear() == entry.getYear()){
						Row selectedRow = r;
					//	evaluateCell(evaluator, selectedRow.getCell(10), selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge",selectedRow.getCell(10).getNumericCellValue()+"");
						map.put("payment",selectedRow.getCell(11).getNumericCellValue()+"");

						Gson gson = new Gson(); 
						return gson.toJson(map);
					}
				}
				else
				{
					Gson gson = new Gson(); 
					return gson.toJson(map);
				}
			}


		}
		Gson gson = new Gson(); 
		return gson.toJson(map);
		
	}
	
	
	public String calculateJournalMonthly(XSSFWorkbook wb, Entry entry, int journalType, int leaseType) throws InvalidFormatException, IOException {


		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);//.getSheet("Yearly Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);

		//LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRow = 5;
		if(journalType == TYPES.JOURNAL_MONTHLY.getValue())startingRow = 4;

		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= startingRow )
			{
				count ++;
				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(0);

				evaluateCell(evaluator, c);

				System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					//Date date = c.getDateCellValue();
					String text = (entry.getMonth() < 10 ? "0" : "") + entry.getMonth();
					int month = Integer.parseInt(text);
					if(date.getYear() == entry.getYear() && date.getMonth().getValue() == month){
						Row selectedRow = r;
						//evaluateCell(evaluator, selectedRow.getCell(5), selectedRow.getCell(6), selectedRow.getCell(7), selectedRow.getCell(8), selectedRow.getCell(9), selectedRow.getCell(10), selectedRow.getCell(11), selectedRow.getCell(12), selectedRow.getCell(13), selectedRow.getCell(14), selectedRow.getCell(15), selectedRow.getCell(16), selectedRow.getCell(17));
						//map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
						entry.getCommencementDate();
					//	Cell monthCell =selectedRow.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));
						evaluateCell(evaluator, selectedRow.getCell(7));
						map.put("dr", selectedRow.getCell(7).getNumericCellValue()+"");
						
						Row upRow = sheet.getRow(row-1);
						if(upRow.getRowNum() == 3)
						{
							map.put("accuredLiabality", "0");
						}
						else {
						evaluateCell(evaluator, upRow.getCell(7));
						map.put("accuredLiabality", upRow.getCell(7).getNumericCellValue()+"");
						}
						
						map.put("total", "");
						evaluateCell(evaluator, selectedRow.getCell(10));
						map.put("repeat", selectedRow.getCell(10).getNumericCellValue()+"");
						
						evaluateCell(evaluator, selectedRow.getCell(8));
						map.put("financeCostRemaining", selectedRow.getCell(8).getNumericCellValue()+"");
							
						//Gson gson = new Gson(); 
						//	return  gson.toJson(map);
						break;


					}

				}
				
			}


		}
		
		for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 16 && row < leaseTerms+16)
			{

				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(2);
				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try{
						date = c.getLocalDateTimeCellValue();
					}
					catch(Exception ex)
					{
						System.out.println(ex);
					}
					if(date.getYear() == entry.getYear() && date.getMonth().getValue() == entry.getMonth()){
						Row selectedRow = r;
					//	evaluateCell(evaluator, selectedRow.getCell(10), selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge",selectedRow.getCell(10).getNumericCellValue()+"");
						map.put("payment",selectedRow.getCell(11).getNumericCellValue()+"");

						Gson gson = new Gson(); 
						return gson.toJson(map);
					}
				}
			}


		}
		Gson gson = new Gson(); 
		return gson.toJson(map);
		
	}
	
	public String calculateJournalQuarterly(XSSFWorkbook wb, Entry entry, int journalType, int leaseType) throws InvalidFormatException, IOException {


		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);//.getSheet("Yearly Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);

		//LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		//int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRow = 5;
		if(journalType == TYPES.JOURNAL_MONTHLY.getValue())startingRow = 4;

		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= startingRow )
			{
				count ++;
				//System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(0);
				Cell month1C = r.getCell(6);
				Cell month2C = r.getCell(8);
				Cell month3C = r.getCell(10);
				
				evaluateCell(evaluator, month1C,month2C,month3C);
				
				int month1 = getCellMonth(entry, month1C);
				int month2 = getCellMonth(entry, month2C);
				int month3 = getCellMonth(entry, month3C);

				evaluateCell(evaluator, c);

				System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					//Date date = c.getDateCellValue();
					
					String text = (entry.getMonth() < 10 ? "0" : "") + entry.getMonth();
					int month = Integer.parseInt(text);
				//	if(date.getYear() == entry.getYear() && date.getMonth().getValue() == month){
					if(date.getYear() == entry.getYear() && (month1 == month || month2 == month || month3 == month) ){
						Row selectedRow = r;
						//evaluateCell(evaluator, selectedRow.getCell(5), selectedRow.getCell(6), selectedRow.getCell(7), selectedRow.getCell(8), selectedRow.getCell(9), selectedRow.getCell(10), selectedRow.getCell(11), selectedRow.getCell(12), selectedRow.getCell(13), selectedRow.getCell(14), selectedRow.getCell(15), selectedRow.getCell(16), selectedRow.getCell(17));
						//map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
						entry.getCommencementDate();
					//	Cell monthCell =selectedRow.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));
						
						evaluateCell(evaluator, selectedRow.getCell(7), selectedRow.getCell(9), selectedRow.getCell(11));
						double drG=selectedRow.getCell(7).getNumericCellValue();
						double drH=selectedRow.getCell(9).getNumericCellValue();
						double drI=selectedRow.getCell(11).getNumericCellValue();
						
						
				
						
						if(entry.getMonth() == date.getMonthValue()  || entry.getMonth() == date.getMonthValue()+3 || entry.getMonth() == date.getMonthValue()+6 || entry.getMonth() == date.getMonthValue()+9)
						{
							map.put("dr", drG+"");
						}
						if(entry.getMonth() == date.getMonthValue()+1 || entry.getMonth() == date.getMonthValue()+4 || entry.getMonth() == date.getMonthValue()+7 || entry.getMonth() == date.getMonthValue()+10)
						{
							map.put("dr", drH+"");
						}
						if(entry.getMonth() == date.getMonthValue()+2 || entry.getMonth() == date.getMonthValue()+5 || entry.getMonth() == date.getMonthValue()+8 )
						{
							map.put("dr", drI+"");
						}

						
						evaluateCell(evaluator, selectedRow.getCell(12));
						map.put("repeat", selectedRow.getCell(12).getNumericCellValue()+"");
				
						
						Row upRow = sheet.getRow(row-1);
						if(upRow.getRowNum() == 4)
						{
							map.put("aboveColJ", "0");
							map.put("total", "0");
						}
						else {
							evaluateCell(evaluator, upRow.getCell(7), upRow.getCell(9), upRow.getCell(11));
							
							double drGUp=upRow.getCell(7).getNumericCellValue();
							double drHUp=upRow.getCell(9).getNumericCellValue();
							double drIUp=upRow.getCell(11).getNumericCellValue();
						
							double total =  drGUp+drHUp+drIUp;
							map.put("total", total+"");
							
							
						evaluateCell(evaluator, upRow.getCell(12));
						map.put("aboveColJ", upRow.getCell(12).getNumericCellValue()+"");
						
						Row upRow2 = sheet.getRow(upRow.getRowNum()-1);
						if(upRow2.getRowNum() == 4)
						{
							map.put("repeatmonthAccrued","0");
						}
						else{
						evaluateCell(evaluator, upRow2.getCell(12));
						map.put("repeatmonthAccrued", upRow2.getCell(12).getNumericCellValue()+"");
						}
						}
						
						
						Cell cStartOfMonth = r.getCell(1);
						Cell cEndOfMonth = r.getCell(2);
						if (HSSFDateUtil.isCellDateFormatted(cStartOfMonth) && HSSFDateUtil.isCellDateFormatted(cEndOfMonth)) {
							LocalDateTime dateStart = cStartOfMonth.getLocalDateTimeCellValue();
							LocalDateTime dateEnd = cEndOfMonth.getLocalDateTimeCellValue();
							//Date date = c.getDateCellValue();
							map.put("startDate", dateStart+"");
							map.put("endDate", dateEnd+"");
						}
					//	evaluateCell(evaluator, selectedRow.getCell(8));
					//	map.put("financeCostRemaining", selectedRow.getCell(8).getNumericCellValue()+"");
							
						//Gson gson = new Gson(); 
						//	return  gson.toJson(map);
						break;


					}

				}
				
			}


		}
		
		for (Row r : sheetLease) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 16 && row < leaseTerms+16)
			{

				System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(2);
				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try{
						date = c.getLocalDateTimeCellValue();
					}
					catch(Exception ex)
					{
						System.out.println(ex);
					}
					if(date.getYear() == entry.getYear() && date.getMonth().getValue() == entry.getMonth()){
						Row selectedRow = r;
					//	evaluateCell(evaluator, selectedRow.getCell(10), selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge",selectedRow.getCell(10).getNumericCellValue()+"");
						map.put("payment",selectedRow.getCell(11).getNumericCellValue()+"");

						Gson gson = new Gson(); 
						return gson.toJson(map);
					}
					else
					{
						map.put("financeCharge","0");
						map.put("payment","0");
						
					}
				}
			}


		}
		Gson gson = new Gson(); 
		return gson.toJson(map);
		
	}

	private int getCellMonth(Entry entry, Cell c) {
		if (HSSFDateUtil.isCellDateFormatted(c)) {
			LocalDateTime date = c.getLocalDateTimeCellValue();
		int month = date.getMonthValue();
		return month;
		}
		return 0;
	}

	private int getMonthCell(int month, XSSFRow headingRow, FormulaEvaluator evaluator) {
		//String monthSelected = getMonth(month);
		String text = (month < 10 ? "0" : "") + month;
		month = Integer.parseInt(text);
		for(int i =5; i< 17; i++)
		{
			Cell c = headingRow.getCell(i);
			evaluateCell(evaluator, c);
			
			Date date = c.getDateCellValue();
			if(date.getMonth()+1 == month)
			//if(c.getDateCellValue() == monthSelected))
				return i;
		}
		
		return 0;
	}

	private String getMonth(int month) {
		switch(month){
		case 0: 
			return "January";
		case 1: 
			return "February";
		case 2: 
			return "March";
		case 3: 
			return "April";
		case 4: 
			return "May";
		case 5: 
			return "June";
		case 6: 
			return "July";
		case 7: 
			return "August";
		case 8: 
			return "September";
		case 9: 
			return "October";
		case 10: 
			return "Novemeber";
		case 11: 
			return "December";
		}
		return "";
	}

	protected void evaluateCell(FormulaEvaluator evaluator, Cell... cells) {
		for(Cell c : cells)
		{
			evaluateCell(evaluator , c);
		}
		
	}
	
	protected void evaluateInCell(FormulaEvaluator evaluator, Cell... cells) {
		for(Cell c : cells)
		{
			evaluateInCell(evaluator , c);
		}
		
	}

	protected CellType evaluateCell(FormulaEvaluator evaluator, Cell c) {
		CellType type = null;
		if (c.getCellType() == CellType.FORMULA) {
			try{
				 type = evaluator.evaluateFormulaCell(c);
			}catch(Exception ex){
				System.out.println("In Exception in loop" + ex);
			}
		}
		return type;
	}
	
	protected Cell evaluateInCell(FormulaEvaluator evaluator, Cell c) {
		Cell type = null;
		if (c.getCellType() == CellType.FORMULA) {
			try{
				 type = evaluator.evaluateInCell(c);
			}catch(Exception ex){
				System.out.println("In Exception in loop" + ex);
			}
		}
		return type;
	}


	public LinkedHashMap<String, LinkedHashMap<String, String>> calculateJournalRightSide(XSSFWorkbook wb, Entry entry) throws InvalidFormatException, IOException {


		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheet("Yearly Journal entry");

		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" +sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		int year = entry.getYear();
		//int year = 2019;


		//	int yearRowNum = findRow(sheet, year);
		//	int nextYearRowNum = findRow(sheet, year+1);
		//	int lastRow = findRow(sheet, "Logic will be the same for next periods");

		//if(nextYearRowNum == 0) nextYearRowNum = sheet.getLastRowNum(); else nextYearRowNum = nextYearRowNum-1 ;
		//	if(nextYearRowNum == 0) nextYearRowNum = lastRow; else nextYearRowNum = nextYearRowNum-1 ;


		for (Row r : sheet) {
			///ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if(r.getRowNum()>= 0 && r.getRowNum()< 100)
			{

				LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();

				System.out.println("In Row" +r.getRowNum());

				for (Cell c : r) {
					if(c.getColumnIndex() >=19)
					{
						CellType cellType = null;
						if (c.getCellType() == CellType.FORMULA) {
							try{
								evaluator.evaluateFormulaCell(c);
							}catch(Exception ex){
								System.out.println("In Exception in loop" + ex);
								mapRow.put(c.getColumnIndex()+"", ":"+"");
								System.out.println("In error" + ex);
							}
							cellType = c.getCachedFormulaResultType();
						}
						else
						{
							cellType = c.getCellType();
						}
						putinMap(mapRow, c, cellType);

					}
				}
				mapSheet.put(row+1+"", mapRow);
				count ++;
			}

		}


		System.out.println("returning Lease map");
		return mapSheet;
	}

	private boolean isSameMonth(String month, Cell c, FormulaEvaluator evaluator)
	{
		evaluator.evaluateFormulaCell(c);
		String cellValue = c.getNumericCellValue()+"";
		if(cellValue.equals(month)) {
			return true;
		}
		return false;
	}

	private static int findRow(XSSFSheet sheet, int cellContent) {

		XSSFCell s = 	sheet.getRow(4).getCell(19);
		if (s.getCellType() == CellType.NUMERIC) {
			if (s.getNumericCellValue() == cellContent) {
				System.out.println("dd");
			} 
		}
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == CellType.NUMERIC) {
					if (cell.getNumericCellValue() == cellContent) {
						return row.getRowNum();  
					}
				}
			}
		}               
		return 0;
	}

	private static int findRow(XSSFSheet sheet, String cellContent) {

		XSSFCell s = 	sheet.getRow(4).getCell(19);
		if (s.getCellType() == CellType.STRING) {
			if (s.getStringCellValue().equals(cellContent)) {
				System.out.println("end row content");
			} 
		}
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (s.getCellType() == CellType.STRING) {
					if (s.getStringCellValue().equals(cellContent)) {
						return row.getRowNum();  
					}
				}
			}
		}               
		return 0;
	}

	private void putinMap(LinkedHashMap<String, String> mapSheet,
			Cell c, CellType cellType) {


		String cellLocation =  c.getColumnIndex()+"";

		switch(cellType) {


		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(c)) {
				mapSheet.put(cellLocation, c.getColumnIndex()==9?  month(c.getDateCellValue().getMonth())+"": c.getDateCellValue()+"");
			}
			else {
				c.getRow().getRowNum();
				mapSheet.put(cellLocation, c.getNumericCellValue()+"");
			}
			break;
		case STRING:
			mapSheet.put(cellLocation, c.getRichStringCellValue()+"");
			break;
		case BOOLEAN:
			mapSheet.put(cellLocation, c.getBooleanCellValue()+"");
		default:
			mapSheet.put(cellLocation, "-"+"");
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