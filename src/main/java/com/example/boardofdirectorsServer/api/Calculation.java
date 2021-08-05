package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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

	protected String json = "";

	/// ifrs.xls

	public Calculation() throws IOException, InvalidFormatException {

	}

	public String formatDate(Date date) {
		String formattedDate = "";
		System.out.println("Formtting date:" + date);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			formattedDate = sdf.format(date);
		} catch (Exception ex) {
			System.out.println("Exception in formatting date:" + date);

		}
		return formattedDate;
	}

	/*
	 * public String formatDate(LocalDateTime date ) { String formattedDate =
	 * ""; System.out.println("Formatting Local date:"+ date); try{
	 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); formattedDate
	 * = sdf.format(date); }catch(Exception ex) {
	 * System.out.println("Exception in formatting Local date:"+ date);
	 * 
	 * } return formattedDate; }
	 */

	public String entry(Entry entry) throws Exception {
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
			// printValues(sheetLease);

			LinkedHashMap<String, LinkedHashMap<String, String>> map = calculate(wb);
			System.out.println("calculation done ");
			json = gson.toJson(map);
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

	public String entryLease(Entry entry, int leaseType) throws Exception {
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
			json = gson.toJson(map);
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

	public String entryTEST(Entry entry, TYPES typeJournal, TYPES typeLease) throws Exception {

		try {

			OPCPackage pkg;
			System.out.println("opening file");
			InputStream file = getFileTEST();
			pkg = OPCPackage.open(file);
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			XSSFSheet sheet = wb.getSheetAt(1);
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

			for (Row r : sheet) {
				/// ONLY PUT COLUMN No in map id
				int row = r.getRowNum();

				if (r.getRowNum() >= 5) {

					System.out.println("In Row" + r.getRowNum());
					Cell c = r.getCell(0);

					evaluateCell(evaluator, c);

					System.out.println(c.getColumnIndex());
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						System.out.println(date.getYear());
						if (date.getYear() == entry.getYear()) {
							// if(date.getYear() ==
							// entry.getCommencementDate().getYear()+1900){ //
							// ADDED FOR getting data from commencemt date
							Row selectedRow = r;

							// evaluateCell(evaluator, selectedRow.getCell(5),
							// selectedRow.getCell(6), selectedRow.getCell(7),
							// selectedRow.getCell(8), selectedRow.getCell(9),
							// selectedRow.getCell(10), selectedRow.getCell(11),
							// selectedRow.getCell(12), selectedRow.getCell(13),
							// selectedRow.getCell(14), selectedRow.getCell(15),
							// selectedRow.getCell(16),
							// selectedRow.getCell(17));
							// map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
							// entry.getCommencementDate();

							// ADDED FOR getting data from commencemt date
							Cell monthCell = null;

							if (entry.getCommencementDate().getMonth() != 0
									&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)// ADDED
																										// FOR
																										// getting
																										// data
																										// from
																										// commencemt
																										// date
							{
								Row selectedRowAbove = sheet.getRow(r.getRowNum() - 1);
								monthCell = selectedRowAbove
										.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));

							} else {
								monthCell = selectedRow
										.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));
							}

							evaluateCell(evaluator, monthCell);

							if (entry.getYear() < entry.getCommencementDate().getYear() + 1900
									|| (entry.getYear() == entry.getCommencementDate().getYear() + 1900
											&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)) {
								double dr = monthCell.getNumericCellValue();
							} else {
								double dr1 = monthCell.getNumericCellValue();
							}
						}
					}
				}
			}

		} catch (Exception ex) {

		}
		return json;
	}

	public String entryJournal(Entry entry, TYPES typeJournal, TYPES typeLease) throws Exception {

		try {

			OPCPackage pkg;
			System.out.println("opening file");
			// InputStream file = getFileJournal();
			InputStream file = getFileJournalAssetDecomposition();
			System.out.println("back from return");
			pkg = OPCPackage.open(file);

			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			Sheet sheetLease = wb.getSheetAt(typeLease.getValue());

			updateValues(entry, sheetLease);

			System.out.println("updating");

			switch (typeJournal) {
			case JOURNAL_YEARLY:
				json = calculateJournal(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case JOURNAL_QUARTERLY:
				json = calculateJournalQuarterly(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case JOURNAL_MONTHLY:
				json = calculateJournalMonthly(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case RECOGNITION_YEARLY:
				json = calculateJournalYearlyDepreciation(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case RECOGNITION_MONTHLY:
				json = calculateJournalMonthlyDepreciation(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;
			case RECOGNITION_QUARTERLY:
				json = calculateJournalQuarterlyDepreciation(wb, entry, typeJournal.getValue(), typeLease.getValue());
				break;

			default:
				break;
			}

			System.out.println("calculation done ");
			// json = gson.toJson(map);
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
		ClassLoader classLoader = this.getClass().getClassLoader();

		// File file = new File(classLoader.getResource(fileName).getFile());
		InputStream file = classLoader.getResourceAsStream(fileName);
		// File is found
		// System.out.println("File Found : " + file.exists());

		// Read File Content
		//// String content = new String(Files.readAllBytes(file.toPath()));
		// System.out.println(content);
		return file;
	}

	protected InputStream getFileJournal() throws Exception {
		String fileName = "static/Journal.xlsx";
		System.out.println("opening file" + fileName);
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}

	protected InputStream getFileJournalAssetDecomposition() throws Exception {
		String fileName = "static/JournalEntriesAssetAndDerecognition.xlsx";
		System.out.println("opening file" + fileName);
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}

	public InputStream getFile(String fileToOpen) throws Exception {
		String fileName = "static/Firsttimeadoption.xlsx";
		System.out.println("opening file" + fileName);
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}

	protected InputStream getFileTEST() throws Exception {
		String fileName = "static/test.xlsx";
		System.out.println("opening file" + fileName);
		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println("here");
		InputStream file = classLoader.getResourceAsStream(fileName);
		System.out.println("returning file");
		return file;
	}

	protected void updateValues(Entry entry, Sheet sheetLease) {
		sheetLease.getRow(3).getCell(0).setCellValue(entry.getLeaseContractNo());
		sheetLease.getRow(3).getCell(1).setCellValue(getPreviousDate(entry.getCommencementDate()));
		sheetLease.getRow(3).getCell(2).setCellValue(entry.getPaymentsAt());
		sheetLease.getRow(3).getCell(3).setCellValue(entry.getAnnualDiscountRate());
		sheetLease.getRow(3).getCell(4).setCellValue(entry.getLeaseTerm());
		sheetLease.getRow(3).getCell(5).setCellValue(entry.getExpectedPeriod());
		sheetLease.getRow(3).getCell(6).setCellValue(entry.getLeasePayment());
		sheetLease.getRow(3).getCell(7).setCellValue(entry.getPaymentIntervals());
		sheetLease.getRow(3).getCell(8).setCellValue(entry.getInitialDirectCost());
		sheetLease.getRow(3).getCell(9).setCellValue(entry.getGuaranteedResidualValue());
		// sheetLease.getRow(3).getCell(10).setCellValue(entry.getLeaseContractNo());
		sheetLease.getRow(3).getCell(11).setCellValue(entry.getUsefulLifeOfTheAsset());
		sheetLease.getRow(3).getCell(12).setCellValue(entry.getEscalation());
		sheetLease.getRow(3).getCell(13).setCellValue(entry.getEscalationAfterEvery());
	}

	private Date getPreviousDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, -5);
		Date fiveHourBack = cal.getTime();
		return fiveHourBack;
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

	public LinkedHashMap<String, LinkedHashMap<String, String>> calculate(XSSFWorkbook wb)
			throws InvalidFormatException, IOException {

		// ClassPathResource res = new ClassPathResource("ifrs.xlsx");
		// File file = new File(res.getPath());

		// OPCPackage pkg = OPCPackage.open(new
		// File("/Users/junaidparacha/Downloads/ifrs.xlsx"));
		// OPCPackage pkg = OPCPackage.open(file);
		// OPCPackage pkg = OPCPackage.open(new
		// File("C:\\Users\\jparacha\\git\\boardofdirectorsServer\\src\\main\\resources\\static\\ifrs.xlsx"));
		// Workbook wb = new XSS(fis); //or new
		// XSSFWorkbook("/somepath/test.xls")

		// XSSFWorkbook wb = new XSSFWorkbook(pkg);
		System.out.println("going to update");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		System.out.println("154");
		LinkedHashMap<String, LinkedHashMap<String, String>> map = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("starting loop");
		for (Sheet sheet : wb) {
			LinkedHashMap<String, String> mapSheet = new LinkedHashMap<String, String>();
			System.out.println("In sheet" + sheet.getSheetName());
			for (Row r : sheet) {
				/// ONLY PUT COLUMN No in map id
				System.out.println("In Row" + r.getRowNum());
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try {
							evaluator.evaluateFormulaCell(c);
						} catch (Exception ex) {
							System.out.println("In Exception in loop" + ex);
							mapSheet.put("" + c.getRow().getRowNum() + "/" + c.getColumnIndex() + "", ":" + "");
							System.out.println("In error" + ex);
						}
						cellType = c.getCachedFormulaResultType();
					} else {
						cellType = c.getCellType();
					}
					putinMap(mapSheet, c, cellType);
				}
			}
			map.put(sheet.getSheetName(), mapSheet);

		}
		// System.out.println(map);
		System.out.println("returning map");
		return map;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> calculateLease(XSSFWorkbook wb, Entry entry,
			int leaseType) throws InvalidFormatException, IOException {

		System.out.println("calculating Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		// XSSFSheet sheet = wb.getSheet("New Lease Yearly");
		Sheet sheet = wb.getSheetAt(leaseType);

		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 16 && count < leaseTerms) {

				LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();

				System.out.println("In Row" + r.getRowNum());
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try {
							evaluator.evaluateFormulaCell(c);
						} catch (Exception ex) {
							System.out.println("In Exception in loop" + ex);
							mapRow.put(c.getColumnIndex() + "", ":" + "");
							System.out.println("In error" + ex);
						}
						cellType = c.getCachedFormulaResultType();
					} else {
						cellType = c.getCellType();
					}
					putinMap(mapRow, c, cellType);
				}
				mapSheet.put(row + 1 + "", mapRow);
				count++;
			}

		}

		System.out.println("returning Lease map");
		return mapSheet;
	}

	public String calculateJournalLeftSide(XSSFWorkbook wb, Entry entry, TYPES typeJournal)
			throws InvalidFormatException, IOException {

		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		// XSSFSheet sheet = wb.getSheet("Yearly Journal entry");
		XSSFSheet sheet = wb.getSheetAt(typeJournal.getValue());

		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		// int totalRows = sheet.getLastRowNum();
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();
			int startingRow = 5;
			if (typeJournal == TYPES.JOURNAL_MONTHLY)
				startingRow = 4;

			if (r.getRowNum() >= startingRow && count < leaseTerms) {

				LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();

				System.out.println("In Row" + r.getRowNum());
				for (Cell c : r) {
					CellType cellType = null;
					if (c.getCellType() == CellType.FORMULA) {
						try {
							evaluator.evaluateFormulaCell(c);
						} catch (Exception ex) {
							System.out.println("In Exception in loop" + ex);
							mapRow.put(c.getColumnIndex() + "", ":" + "");
							System.out.println("In error" + ex);
						}
						cellType = c.getCachedFormulaResultType();
					} else {
						cellType = c.getCellType();
					}
					putinMap(mapRow, c, cellType);
				}
				mapSheet.put(row + 1 + "", mapRow);
				count++;
			}

		}

		System.out.println("returning Journal map");
		Gson gson = new Gson();
		return gson.toJson(mapSheet);

	}

	/**
	 * @param wb
	 * @param entry
	 * @param journalType
	 * @param leaseType
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public String calculateJournal(XSSFWorkbook wb, Entry entry, int journalType, int leaseType)
			throws InvalidFormatException, IOException {

		double rightOfUseOfAsset = 0.0;
		System.out.println("calculating Journal Yearly");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);// .getSheet("Yearly
														// Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		for (Row r1 : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row1 = r1.getRowNum();

			if (r1.getRowNum() >= 16 && row1 < leaseTerms + 16) {

				if (r1.getRowNum() == 16) {
					Cell c1 = r1.getCell(16);
					evaluateCell(evaluator, c1);
					rightOfUseOfAsset = c1.getNumericCellValue();
					break;
				}
			}
		}

		// double rightOfUseOfAsset =
		// sheetLease.getRow(3).getCell(10).getNumericCellValue();

		Date startingDate = entry.getCommencementDate();
		Date deteEnding = addYearsToDate(entry.getCommencementDate(), entry.getUsefulLifeOfTheAsset());

		long totalDays = ChronoUnit.DAYS.between(startingDate.toInstant(), deteEnding.toInstant());
		totalDays = totalDays + 1;
		double right = rightOfUseOfAsset / totalDays;
		int noOfDaysInMonth = getMonthDays(entry.getYear(), entry.getMonth(), 4, startingDate, deteEnding);

		double finalRightOfUseAsset = right * noOfDaysInMonth;

		// LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new
		// LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		// int totalRows = sheet.getLastRowNum();

		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 5 && count <= leaseTerms) {
				count++;
				System.out.println("In Row" + r.getRowNum());
				Cell c = r.getCell(0);

				evaluateCell(evaluator, c);

				System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					System.out.println(date.getYear());
					if (date.getYear() == entry.getYear()) {
						// if(date.getYear() ==
						// entry.getCommencementDate().getYear()+1900){ // ADDED
						// FOR getting data from commencemt date
						Row selectedRow = r;

						// evaluateCell(evaluator, selectedRow.getCell(5),
						// selectedRow.getCell(6), selectedRow.getCell(7),
						// selectedRow.getCell(8), selectedRow.getCell(9),
						// selectedRow.getCell(10), selectedRow.getCell(11),
						// selectedRow.getCell(12), selectedRow.getCell(13),
						// selectedRow.getCell(14), selectedRow.getCell(15),
						// selectedRow.getCell(16), selectedRow.getCell(17));
						// map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
						// entry.getCommencementDate();

						// ADDED FOR getting data from commencemt date
						Cell monthCell = null;

						if (entry.getCommencementDate().getMonth() != 0
								&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)// ADDED
																									// FOR
																									// getting
																									// data
																									// from
																									// commencemt
																									// date
						{
							Row selectedRowAbove = sheet.getRow(r.getRowNum() - 1);
							monthCell = selectedRowAbove
									.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));

						} else {
							monthCell = selectedRow.getCell(getMonthCell(entry.getMonth(), sheet.getRow(4), evaluator));
						}

						evaluateCell(evaluator, monthCell);

						/*
						 * if (entry.getYear() <
						 * entry.getCommencementDate().getYear() + 1900 ||
						 * (entry.getYear() ==
						 * entry.getCommencementDate().getYear() + 1900 &&
						 * entry.getMonth() <
						 * entry.getCommencementDate().getMonth() + 1)) {
						 */
						if ((entry.getYear() < entry.getCommencementDate().getYear() + 1900)
								|| (entry.getYear() > deteEnding.getYear() + 1900)
								|| (entry.getYear() == entry.getCommencementDate().getYear() + 1900
										&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)
								|| (entry.getYear() == deteEnding.getYear() + 1900
										&& entry.getMonth() > entry.getCommencementDate().getMonth() + 1)) {
							map.put("dr", "0");
							map.put("rightOfUseOfAsset", "0");
						} else {
							map.put("dr", monthCell.getNumericCellValue() + "");
							map.put("rightOfUseOfAsset", finalRightOfUseAsset + "");
						}

						evaluateCell(evaluator, selectedRow.getCell(5), selectedRow.getCell(6), selectedRow.getCell(7),
								selectedRow.getCell(8), selectedRow.getCell(9), selectedRow.getCell(10),
								selectedRow.getCell(11), selectedRow.getCell(12), selectedRow.getCell(13),
								selectedRow.getCell(14), selectedRow.getCell(15), selectedRow.getCell(16));

						Row upRow = sheet.getRow(row - 1);
						if (upRow.getRowNum() <= 4) {
							map.put("total", "0");
							map.put("RepeatmonthAccrued", "0");
						} else {
							evaluateCell(evaluator, upRow.getCell(5), upRow.getCell(6), upRow.getCell(7),
									upRow.getCell(8), upRow.getCell(9), upRow.getCell(10), upRow.getCell(11),
									upRow.getCell(12), upRow.getCell(13), upRow.getCell(14), upRow.getCell(15),
									upRow.getCell(16));

							double total = upRow.getCell(5).getNumericCellValue()
									+ upRow.getCell(6).getNumericCellValue() + upRow.getCell(7).getNumericCellValue()
									+ upRow.getCell(8).getNumericCellValue() + upRow.getCell(9).getNumericCellValue()
									+ upRow.getCell(10).getNumericCellValue() + upRow.getCell(11).getNumericCellValue()
									+ upRow.getCell(12).getNumericCellValue() + upRow.getCell(13).getNumericCellValue()
									+ upRow.getCell(14).getNumericCellValue() + upRow.getCell(15).getNumericCellValue()
									+ upRow.getCell(16).getNumericCellValue();

							map.put("total", total + "");

						}
						if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {
							evaluateCell(evaluator, selectedRow.getCell(17));
							map.put("repeat", selectedRow.getCell(17).getNumericCellValue() + "");
						} else {
							System.out.println(upRow.getRowNum() + "uprow");
							if (upRow.getRowNum() == 4) {
								map.put("repeat", "");
							} else {
								evaluateCell(evaluator, upRow.getCell(17));
								map.put("repeat", upRow.getCell(17).getNumericCellValue() + "");

								Row upRow2 = sheet.getRow(upRow.getRowNum() - 1);

								if (upRow2.getRowNum() != 4) {
									evaluateCell(evaluator, upRow2.getCell(17));
									map.put("RepeatmonthAccrued", upRow2.getCell(17).getNumericCellValue() + "");
								} else {
									map.put("RepeatmonthAccrued", "0");

								}

							}
						}

						// Gson gson = new Gson();
						// return gson.toJson(map);
						break;

					}

				}

			}

			// mapSheet.put(row+1+"", mapRow);

		}

		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 16 && row < leaseTerms + 16) {

				System.out.println("In Row" + r.getRowNum());
				Cell c = r.getCell(2);

				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try {
						date = c.getLocalDateTimeCellValue();
					} catch (Exception ex) {
						System.out.println(ex);
					}
					if (date.getYear() == entry.getYear()) {
						Row selectedRow = r;
						// evaluateCell(evaluator, selectedRow.getCell(10),
						// selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge", selectedRow.getCell(10).getNumericCellValue() + "");
						map.put("payment", selectedRow.getCell(11).getNumericCellValue() + "");

						Gson gson = new Gson();
						return gson.toJson(map);
					}
				} else {
					Gson gson = new Gson();
					return gson.toJson(map);
				}
			}

		}
		Gson gson = new Gson();
		return gson.toJson(map);

	}

	public String calculateJournalMonthly(XSSFWorkbook wb, Entry entry, int journalType, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		double rightOfUseOfAsset = 0.0;
		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);// .getSheet("Yearly
														// Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		for (Row r1 : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row1 = r1.getRowNum();

			if (r1.getRowNum() >= 16 && row1 < leaseTerms + 16) {

				if (r1.getRowNum() == 16) {
					Cell c1 = r1.getCell(16);
					evaluateCell(evaluator, c1);
					rightOfUseOfAsset = c1.getNumericCellValue();
					break;
				}
			}
		}
		// double rightOfUseOfAsset =
		// sheetLease.getRow(3).getCell(10).getNumericCellValue();

		Date startingDate = entry.getCommencementDate();
		Date deteEnding = addYearsToDate(entry.getCommencementDate(), entry.getUsefulLifeOfTheAsset());

		long totalDays = ChronoUnit.DAYS.between(startingDate.toInstant(), deteEnding.toInstant());
		totalDays = totalDays + 1;
		double right = rightOfUseOfAsset / totalDays;
		int noOfDaysInMonth = getMonthDays(entry.getYear(), entry.getMonth(), 4, startingDate, deteEnding);

		double finalRightOfUseAsset = right * noOfDaysInMonth;

		// LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new
		// LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		// int totalRows = sheet.getLastRowNum();
		// int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRow = 5;
		if (journalType == TYPES.JOURNAL_MONTHLY.getValue())
			startingRow = 4;

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= startingRow) {
				count++;
				System.out.println("In Row" + r.getRowNum());
				Cell c = r.getCell(0);

				evaluateCell(evaluator, c);

				System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					// Date date = c.getDateCellValue();
					String text = (entry.getMonth() < 10 ? "0" : "") + entry.getMonth();
					int month = Integer.parseInt(text);
					if (date.getYear() == entry.getYear() && date.getMonth().getValue() == month) {
						Row selectedRow = r;
						// evaluateCell(evaluator, selectedRow.getCell(5),
						// selectedRow.getCell(6), selectedRow.getCell(7),
						// selectedRow.getCell(8), selectedRow.getCell(9),
						// selectedRow.getCell(10), selectedRow.getCell(11),
						// selectedRow.getCell(12), selectedRow.getCell(13),
						// selectedRow.getCell(14), selectedRow.getCell(15),
						// selectedRow.getCell(16), selectedRow.getCell(17));
						// map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
						entry.getCommencementDate();
						// Cell monthCell
						// =selectedRow.getCell(getMonthCell(entry.getMonth(),
						// sheet.getRow(4), evaluator));
						// hamza 2020 aug changed dr cell from clumn 7 to column
						// 10
						// and repeat from ccolumn 10 to column 8
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(7));
						evaluateCell(evaluator, selectedRow.getCell(8));

						if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {
							map.put("dr", selectedRow.getCell(10).getNumericCellValue() + "");
							map.put("repeat", selectedRow.getCell(8).getNumericCellValue() + "");

						} else {
							map.put("dr", selectedRow.getCell(7).getNumericCellValue() + "");
							map.put("repeat", selectedRow.getCell(10).getNumericCellValue() + "");

						}

						Row upRow = sheet.getRow(row - 1);
						if (upRow.getRowNum() == 3) {
							map.put("accuredLiabality", "0");
						} else {
							evaluateCell(evaluator, upRow.getCell(7));
							map.put("accuredLiabality", upRow.getCell(7).getNumericCellValue() + "");
						}

						map.put("total", "");

						evaluateCell(evaluator, selectedRow.getCell(8));
						map.put("financeCostRemaining", selectedRow.getCell(8).getNumericCellValue() + "");

						// Gson gson = new Gson();
						// return gson.toJson(map);
						break;

					}

				}

			}

		}
		if ((entry.getYear() < entry.getCommencementDate().getYear() + 1900)
				|| (entry.getYear() > deteEnding.getYear() + 1900)
				|| (entry.getYear() == entry.getCommencementDate().getYear() + 1900
						&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)
				|| (entry.getYear() == deteEnding.getYear() + 1900
						&& entry.getMonth() > entry.getCommencementDate().getMonth() + 1)) {
			map.put("rightOfUseOfAsset", "0");

		} else {
			map.put("rightOfUseOfAsset", finalRightOfUseAsset + "");

		}
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 16 && row < leaseTerms + 16) {

				System.out.println("In Row" + r.getRowNum());
				Cell c = r.getCell(2);
				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try {
						date = c.getLocalDateTimeCellValue();
					} catch (Exception ex) {
						System.out.println(ex);
					}
					if (date.getYear() == entry.getYear() && date.getMonth().getValue() == entry.getMonth()) {
						Row selectedRow = r;
						// evaluateCell(evaluator, selectedRow.getCell(10),
						// selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge", selectedRow.getCell(10).getNumericCellValue() + "");
						map.put("payment", selectedRow.getCell(11).getNumericCellValue() + "");

						Gson gson = new Gson();
						return gson.toJson(map);
					}
				}
			}

		}
		Gson gson = new Gson();
		return gson.toJson(map);

	}

	public String calculateJournalQuarterly(XSSFWorkbook wb, Entry entry, int journalType, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		double rightOfUseOfAsset = 0.0;
		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheetAt(journalType);// .getSheet("Yearly
														// Journal entry");
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		for (Row r1 : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row1 = r1.getRowNum();

			if (r1.getRowNum() >= 16 && row1 < leaseTerms + 16) {

				if (r1.getRowNum() == 16) {
					Cell c1 = r1.getCell(16);
					evaluateCell(evaluator, c1);
					rightOfUseOfAsset = c1.getNumericCellValue();
					break;
				}
			}
		}
		// double rightOfUseOfAsset =
		// sheetLease.getRow(3).getCell(10).getNumericCellValue();

		Date startingDate = entry.getCommencementDate();
		Date deteEnding = addYearsToDate(entry.getCommencementDate(), entry.getUsefulLifeOfTheAsset());

		long totalDays = ChronoUnit.DAYS.between(startingDate.toInstant(), deteEnding.toInstant());
		totalDays = totalDays + 1;
		double right = rightOfUseOfAsset / totalDays;
		int noOfDaysInMonth = getMonthDays(entry.getYear(), entry.getMonth(), 4, startingDate, deteEnding);

		double finalRightOfUseAsset = right * noOfDaysInMonth;
		// LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new
		// LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		// int totalRows = sheet.getLastRowNum();

		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRow = 5;
		if (journalType == TYPES.JOURNAL_MONTHLY.getValue())
			startingRow = 4;

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();
			if (row == 1210) {
				System.out.println("calcul");
			}
			System.out.println("calculateJournalQuarterly on row:" + row);
			if (r.getRowNum() >= startingRow) {
				count++;
				// System.out.println("In Row" +r.getRowNum());
				Cell c = r.getCell(0);
				Cell month1C = r.getCell(6);
				Cell month2C = r.getCell(8);
				Cell month3C = r.getCell(10);

				evaluateCell(evaluator, month1C, month2C, month3C);

				int month1 = getCellMonth(entry, month1C);
				int month2 = getCellMonth(entry, month2C);
				int month3 = getCellMonth(entry, month3C);

				evaluateCell(evaluator, c);

				// System.out.println(c.getColumnIndex());
				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = c.getLocalDateTimeCellValue();
					// Date date = c.getDateCellValue();

					String text = (entry.getMonth() < 10 ? "0" : "") + entry.getMonth();
					int month = Integer.parseInt(text);
					// if(date.getYear() == entry.getYear() &&
					// date.getMonth().getValue() == month){
					if ((date.getYear() == entry.getYear() && (month1 == month || month2 == month || month3 == month))
							|| (entry.getYear() - date.getYear() == 1 && month3 == 1 && month == 1)) { // ADDED
																										// CONDITION
																										// FOR
																										// GEtting
																										// Monthof
																										// Jan
																										// from
																										// previous
																										// Year.
						Row selectedRow = r;
						// evaluateCell(evaluator, selectedRow.getCell(5),
						// selectedRow.getCell(6), selectedRow.getCell(7),
						// selectedRow.getCell(8), selectedRow.getCell(9),
						// selectedRow.getCell(10), selectedRow.getCell(11),
						// selectedRow.getCell(12), selectedRow.getCell(13),
						// selectedRow.getCell(14), selectedRow.getCell(15),
						// selectedRow.getCell(16), selectedRow.getCell(17));
						// map.put("dr",selectedRow.getCell(5).getNumericCellValue()+"");
						// entry.getCommencementDate();
						// Cell monthCell
						// =selectedRow.getCell(getMonthCell(entry.getMonth(),
						// sheet.getRow(4), evaluator));

						evaluateCell(evaluator, selectedRow.getCell(7), selectedRow.getCell(9),
								selectedRow.getCell(11));
						double drG = selectedRow.getCell(7).getNumericCellValue();
						double drH = selectedRow.getCell(9).getNumericCellValue();
						double drI = selectedRow.getCell(11).getNumericCellValue();

						if (entry.getMonth() == date.getMonthValue() || entry.getMonth() == date.getMonthValue() + 3
								|| entry.getMonth() == date.getMonthValue() + 6
								|| entry.getMonth() == date.getMonthValue() + 9) {
							map.put("dr", drG + "");
						} else if (entry.getMonth() == date.getMonthValue() + 1
								|| entry.getMonth() == date.getMonthValue() + 4
								|| entry.getMonth() == date.getMonthValue() + 7
								|| entry.getMonth() == date.getMonthValue() + 10) {
							map.put("dr", drH + "");
						} else if (entry.getMonth() == date.getMonthValue() + 2
								|| entry.getMonth() == date.getMonthValue() + 5
								|| entry.getMonth() == date.getMonthValue() + 8) {
							map.put("dr", drI + "");
						}
						// ADDED CONDITION FOR GEtting Monthof Jan from previous
						// Year.
						else if (entry.getYear() - date.getYear() == 1 && month3 == 1 && month == 1) {
							map.put("dr", drI + "");
						}

						evaluateCell(evaluator, selectedRow.getCell(12));
						map.put("repeat", selectedRow.getCell(12).getNumericCellValue() + "");

						Row upRow = sheet.getRow(row - 1);
						if (upRow.getRowNum() == 4) {
							map.put("aboveColJ", "0");
							map.put("total", "0");
						} else {
							evaluateCell(evaluator, upRow.getCell(7), upRow.getCell(9), upRow.getCell(11));

							double drGUp = upRow.getCell(7).getNumericCellValue();
							double drHUp = upRow.getCell(9).getNumericCellValue();
							double drIUp = upRow.getCell(11).getNumericCellValue();

							double total = drGUp + drHUp + drIUp;
							map.put("total", total + "");

							evaluateCell(evaluator, upRow.getCell(12));
							map.put("aboveColJ", upRow.getCell(12).getNumericCellValue() + "");

							Row upRow2 = sheet.getRow(upRow.getRowNum() - 1);
							if (upRow2.getRowNum() == 4) {
								map.put("repeatmonthAccrued", "0");
							} else {
								evaluateCell(evaluator, upRow2.getCell(12));
								map.put("repeatmonthAccrued", upRow2.getCell(12).getNumericCellValue() + "");
							}
						}

						Cell cStartOfMonth = r.getCell(1);
						Cell cEndOfMonth = r.getCell(2);
						if (HSSFDateUtil.isCellDateFormatted(cStartOfMonth)
								&& HSSFDateUtil.isCellDateFormatted(cEndOfMonth)) {
							evaluateCell(evaluator, cStartOfMonth);
							evaluateCell(evaluator, cEndOfMonth);
							LocalDateTime dateStart = cStartOfMonth.getLocalDateTimeCellValue();
							LocalDateTime dateEnd = cEndOfMonth.getLocalDateTimeCellValue();
							// Date date = c.getDateCellValue();
							map.put("startDate", dateStart + "");
							map.put("endDate", dateEnd + "");
						}
						// evaluateCell(evaluator, selectedRow.getCell(8));
						// map.put("financeCostRemaining",
						// selectedRow.getCell(8).getNumericCellValue()+"");

						// Gson gson = new Gson();
						// return gson.toJson(map);
						break;

					}

				}

			}

		}

		if ((entry.getYear() < entry.getCommencementDate().getYear() + 1900)
				|| (entry.getYear() > deteEnding.getYear() + 1900)
				|| (entry.getYear() == entry.getCommencementDate().getYear() + 1900
						&& entry.getMonth() < entry.getCommencementDate().getMonth() + 1)
				|| (entry.getYear() == deteEnding.getYear() + 1900
						&& entry.getMonth() > entry.getCommencementDate().getMonth() + 1)) {
			map.put("rightOfUseOfAsset", "0");

		} else {
			map.put("rightOfUseOfAsset", finalRightOfUseAsset + "");

		}
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 16 && row < leaseTerms + 16) {

				System.out.println("In Row" + r.getRowNum());
				Cell c = r.getCell(2);
				evaluateCell(evaluator, c);

				if (HSSFDateUtil.isCellDateFormatted(c)) {
					LocalDateTime date = null;
					try {
						date = c.getLocalDateTimeCellValue();
					} catch (Exception ex) {
						System.out.println(ex);
					}
					if (date.getYear() == entry.getYear() && date.getMonth().getValue() == entry.getMonth()) {
						Row selectedRow = r;
						// evaluateCell(evaluator, selectedRow.getCell(10),
						// selectedRow.getCell(11));
						evaluateCell(evaluator, selectedRow.getCell(10));
						evaluateCell(evaluator, selectedRow.getCell(11));
						map.put("financeCharge", selectedRow.getCell(10).getNumericCellValue() + "");
						map.put("payment", selectedRow.getCell(11).getNumericCellValue() + "");

						Gson gson = new Gson();
						return gson.toJson(map);
					} else {
						map.put("financeCharge", "0");
						map.put("payment", "0");

					}
				}
			}

		}
		Gson gson = new Gson();
		return gson.toJson(map);

	}

	@SuppressWarnings("deprecationMonthly")
	public String calculateJournalMonthlyDepreciation(XSSFWorkbook wb, Entry entry, int depreciationType, int leaseType)
			throws InvalidFormatException, IOException, InvocationTargetException {
		System.out.println("calculating depreciation monthly");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		XSSFSheet sheetDerecognition = wb.getSheetAt(depreciationType);
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (Row r : sheetDerecognition) {
			if (r.getRowNum() == 3) { //
				r.getCell(1).setCellValue(entry.getPaymentToAdd());
				evaluateCell(evaluator, r.getCell(1));

			}
			if (r.getRowNum() == 4) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
			}
			if (r.getRowNum() == 22) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
				wb.setForceFormulaRecalculation(true);
				break;
			}
		}

		for (Row r : sheetDerecognition) {
			if ((r.getRowNum() > 1) && (r.getRowNum() <= 30)) { //

				evaluateCell(evaluator, r.getCell(1));

			}

		}
		evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(1));
		evaluateCell(evaluator, sheetDerecognition.getRow(1).getCell(1));
		double b2 = sheetDerecognition.getRow(1).getCell(1).getNumericCellValue();
		Date b3 = sheetDerecognition.getRow(2).getCell(1).getDateCellValue();
		Date b5 = sheetDerecognition.getRow(4).getCell(1).getDateCellValue();
		Date b6 = sheetDerecognition.getRow(5).getCell(1).getDateCellValue();
		Date b7 = sheetDerecognition.getRow(6).getCell(1).getDateCellValue();

		evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(2));

		// double b81 =
		// sheetDerecognition.getRow(7).getCell(2).getNumericCellValue();
		double b8 = sheetDerecognition.getRow(7).getCell(1).getNumericCellValue();
		double b9 = sheetDerecognition.getRow(8).getCell(1).getNumericCellValue();
		double b10 = sheetDerecognition.getRow(9).getCell(1).getNumericCellValue();
		double b11 = sheetDerecognition.getRow(10).getCell(1).getNumericCellValue();
		double b12 = sheetDerecognition.getRow(11).getCell(1).getNumericCellValue();
		double b13 = sheetDerecognition.getRow(12).getCell(1).getNumericCellValue();
		double b14 = sheetDerecognition.getRow(13).getCell(1).getNumericCellValue();
		double b15 = sheetDerecognition.getRow(14).getCell(1).getNumericCellValue();
		double b17 = sheetDerecognition.getRow(16).getCell(1).getNumericCellValue();
		double b16 = sheetDerecognition.getRow(15).getCell(1).getNumericCellValue();
		double b19 = sheetDerecognition.getRow(18).getCell(1).getNumericCellValue();
		double b20 = sheetDerecognition.getRow(19).getCell(1).getNumericCellValue();

		map.put("b3", b3 + "");
		map.put("b5", b5 + "");
		map.put("b6", b6 + "");
		map.put("b7", b7 + "");
		map.put("b8", b8 + "");
		map.put("b9", b9 + "");
		map.put("b10", b10 + "");
		map.put("b11", b11 + "");
		map.put("b12", b12 + "");
		map.put("b13", b13 + "");
		map.put("b14", b14 + "");
		map.put("b15", b15 + "");
		map.put("b19", b19 + "");

		double fcdr = 0;
		double accrued = 0;
		double ll = 0;
		double bank = 0;
		double fcdrTermination = 0;
		double accruedTermination = 0;
		double prepaidTermination = 0;
		double expenseTermination = 0;
		double llTermination = 0;
		double rouTermination = 0;
		double gainTermination = 0;
		double fbankTermination = 0;
		double prepaid = 0;

		if (entry.getPaymentIntervals().equalsIgnoreCase("Monthly")) {

			if (entry.getRecognitionOptions().equalsIgnoreCase("Apportioned")) {

				// for the final right side column
				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(6).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(5));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(5));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {

					fcdr = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					// fcdrTermination =
					// sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();

					accrued = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					// accruedTermination =
					// sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();

					ll = sheetDerecognition.getRow(6).getCell(4).getNumericCellValue();
					bank = sheetDerecognition.getRow(7).getCell(5).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(10).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(11).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(12).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(13).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(14).getCell(5).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(2).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}

			} else if (entry.getRecognitionOptions().equalsIgnoreCase("No Payment")) {

				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(8));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(15).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(16).getCell(9));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {
					expenseTermination = sheetDerecognition.getRow(10).getCell(8).getNumericCellValue();

					// fcdr =
					// sheetDerecognition.getRow(11).getCell(8).getNumericCellValue();
					// accrued =
					// sheetDerecognition.getRow(12).getCell(8).getNumericCellValue();
					fcdrTermination = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					accruedTermination = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(13).getCell(8).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(14).getCell(9).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(15).getCell(9).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(16).getCell(9).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(22).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}
			}
		}

		map.put("financeCostDr", fcdr + "");
		map.put("financeCostDrTer", fcdrTermination + "");
		map.put("AccruedDr", accrued + "");
		map.put("AccruedDrTer", accruedTermination + "");
		map.put("LeaseLiabilityDr", ll + "");
		map.put("BankCr", bank + "");

		map.put("expenseTermination", expenseTermination + "");
		map.put("llTermination", llTermination + "");
		map.put("rouTermination", rouTermination + "");
		map.put("gainTermination", gainTermination + "");
		map.put("bankTermination", fbankTermination + "");
		map.put("prepaid", prepaid + "");
		map.put("prepaidTer", prepaidTermination + "");

		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@SuppressWarnings("deprecationQuarterly")
	public String calculateJournalQuarterlyDepreciation(XSSFWorkbook wb, Entry entry, int depreciationType,
			int leaseType) throws InvalidFormatException, IOException, InvocationTargetException {

		System.out.println("calculating depreciation monthly");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		XSSFSheet sheetDerecognition = wb.getSheetAt(depreciationType);
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (Row r : sheetDerecognition) {
			if (r.getRowNum() == 3) { //
				r.getCell(1).setCellValue(entry.getPaymentToAdd());
				evaluateCell(evaluator, r.getCell(1));

			}
			if (r.getRowNum() == 4) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
			}
			if (r.getRowNum() == 22) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
				wb.setForceFormulaRecalculation(true);
				break;
			}
		}

		for (Row r : sheetDerecognition) {
			if ((r.getRowNum() > 1) && (r.getRowNum() <= 30)) { //

				evaluateCell(evaluator, r.getCell(1));

			}

		}
		evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(1));
		evaluateCell(evaluator, sheetDerecognition.getRow(1).getCell(1));
		// double b2 =
		// sheetDerecognition.getRow(1).getCell(1).getNumericCellValue();
		Date b3 = sheetDerecognition.getRow(2).getCell(1).getDateCellValue();
		Date b5 = sheetDerecognition.getRow(4).getCell(1).getDateCellValue();
		Date b6 = sheetDerecognition.getRow(5).getCell(1).getDateCellValue();
		Date b7 = sheetDerecognition.getRow(6).getCell(1).getDateCellValue();

		evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(1));

		// sheetDerecognition.getRow(7).getCell(1).getErrorCellValue();
		// sheetDerecognition.getRow(7).getCell(1).getNumericCellValue();
		// double b81 =
		// sheetDerecognition.getRow(7).getCell(2).getErrorCellValue();
		double b8 = sheetDerecognition.getRow(7).getCell(1).getNumericCellValue();
		double b9 = sheetDerecognition.getRow(8).getCell(1).getNumericCellValue();
		double b10 = sheetDerecognition.getRow(9).getCell(1).getNumericCellValue();
		double b11 = sheetDerecognition.getRow(10).getCell(1).getNumericCellValue();
		double b12 = sheetDerecognition.getRow(11).getCell(1).getNumericCellValue();
		double b13 = sheetDerecognition.getRow(12).getCell(1).getNumericCellValue();
		double b14 = sheetDerecognition.getRow(13).getCell(1).getNumericCellValue();
		double b15 = sheetDerecognition.getRow(14).getCell(1).getNumericCellValue();
		double b17 = sheetDerecognition.getRow(16).getCell(1).getNumericCellValue();
		double b16 = sheetDerecognition.getRow(15).getCell(1).getNumericCellValue();
		double b19 = sheetDerecognition.getRow(18).getCell(1).getNumericCellValue();
		double b20 = sheetDerecognition.getRow(19).getCell(1).getNumericCellValue();

		map.put("b3", b3 + "");
		map.put("b5", b5 + "");
		map.put("b6", b6 + "");
		map.put("b7", b7 + "");
		map.put("b8", b8 + "");
		map.put("b9", b9 + "");
		map.put("b10", b10 + "");
		map.put("b11", b11 + "");
		map.put("b12", b12 + "");
		map.put("b13", b13 + "");
		map.put("b14", b14 + "");
		map.put("b15", b15 + "");
		map.put("b19", b19 + "");

		double fcdr = 0;
		double accrued = 0;
		double ll = 0;
		double bank = 0;
		double fcdrTermination = 0;
		double accruedTermination = 0;
		double prepaidTermination = 0;
		double expenseTermination = 0;
		double llTermination = 0;
		double rouTermination = 0;
		double gainTermination = 0;
		double fbankTermination = 0;
		double prepaid = 0;

		if (entry.getPaymentIntervals().equalsIgnoreCase("Quarterly")) {

			if (entry.getRecognitionOptions().equalsIgnoreCase("Apportioned")) {

				// for the final right side column
				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(6).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(5));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(5));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {

					fcdr = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					// fcdrTermination =
					// sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();

					accrued = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					// accruedTermination =
					// sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();

					ll = sheetDerecognition.getRow(6).getCell(4).getNumericCellValue();
					bank = sheetDerecognition.getRow(7).getCell(5).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(10).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(11).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(12).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(13).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(14).getCell(5).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(2).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}

			} else if (entry.getRecognitionOptions().equalsIgnoreCase("No Payment")) {

				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(8));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(15).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(16).getCell(9));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {
					expenseTermination = sheetDerecognition.getRow(10).getCell(8).getNumericCellValue();

					// fcdr =
					// sheetDerecognition.getRow(11).getCell(8).getNumericCellValue();
					// accrued =
					// sheetDerecognition.getRow(12).getCell(8).getNumericCellValue();
					fcdrTermination = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					accruedTermination = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(13).getCell(8).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(14).getCell(9).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(15).getCell(9).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(16).getCell(9).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(22).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}
			}
		}

		map.put("financeCostDr", fcdr + "");
		map.put("financeCostDrTer", fcdrTermination + "");
		map.put("AccruedDr", accrued + "");
		map.put("AccruedDrTer", accruedTermination + "");
		map.put("LeaseLiabilityDr", ll + "");
		map.put("BankCr", bank + "");

		map.put("expenseTermination", expenseTermination + "");
		map.put("llTermination", llTermination + "");
		map.put("rouTermination", rouTermination + "");
		map.put("gainTermination", gainTermination + "");
		map.put("bankTermination", fbankTermination + "");
		map.put("prepaid", prepaid + "");
		map.put("prepaidTer", prepaidTermination + "");

		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@SuppressWarnings("deprecation")
	public String calculateJournalYearlyDepreciation(XSSFWorkbook wb, Entry entry, int depreciationType, int leaseType)
			throws InvalidFormatException, IOException, InvocationTargetException {
		System.out.println("calculating depreciation Yearly");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		XSSFSheet sheetDerecognition = wb.getSheetAt(depreciationType);
		XSSFSheet sheetLease = wb.getSheetAt(leaseType);
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		for (Row r : sheetDerecognition) {
			if (r.getRowNum() == 3) { //
				r.getCell(1).setCellValue(entry.getPaymentToAdd());
				evaluateCell(evaluator, r.getCell(1));

			}
			if (r.getRowNum() == 4) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
			}
			if (r.getRowNum() == 22) { //
				r.getCell(1).setCellValue(entry.getUserSelectedDate());
				evaluateCell(evaluator, r.getCell(1));
				wb.setForceFormulaRecalculation(true);
				break;
			}
		}

		/*
		 * for (Row r : sheetDerecognition) { if (r.getRowNum() == 3) { //
		 * r.getCell(1).setCellValue(entry.getPaymentToAdd());
		 * wb.setForceFormulaRecalculation(true); Cell form = r.getCell(1); //
		 * form.getDateCellValue(); evaluateCell(evaluator, form); break; }
		 * 
		 * }
		 * 
		 * for (Row r : sheetDerecognition) { if (r.getRowNum() == 4) { //
		 * r.getCell(1).setCellValue(entry.getUserSelectedDate());
		 * wb.setForceFormulaRecalculation(true); Cell form = r.getCell(1);
		 * form.getDateCellValue(); evaluateCell(evaluator, form); break; }
		 * 
		 * }
		 * 
		 * for (Row r : sheetDerecognition) { if (r.getRowNum() == 22) { //
		 * r.getCell(1).setCellValue(entry.getUserSelectedDate());
		 * wb.setForceFormulaRecalculation(true); Cell form = r.getCell(1);
		 * form.getDateCellValue(); evaluateCell(evaluator, form); break; }
		 * 
		 * }
		 */

		for (Row r : sheetDerecognition) {
			if ((r.getRowNum() > 2) && (r.getRowNum() <= 30)) { //

				evaluateCell(evaluator, r.getCell(1));

			}

		}

		Date b3 = sheetDerecognition.getRow(2).getCell(1).getDateCellValue();
		Date b5 = sheetDerecognition.getRow(4).getCell(1).getDateCellValue();
		Date b6 = sheetDerecognition.getRow(5).getCell(1).getDateCellValue();
		Date b7 = sheetDerecognition.getRow(6).getCell(1).getDateCellValue();
		double b8 = sheetDerecognition.getRow(7).getCell(1).getNumericCellValue();
		double b9 = sheetDerecognition.getRow(8).getCell(1).getNumericCellValue();
		double b10 = sheetDerecognition.getRow(9).getCell(1).getNumericCellValue();
		double b11 = sheetDerecognition.getRow(10).getCell(1).getNumericCellValue();
		double b12 = sheetDerecognition.getRow(11).getCell(1).getNumericCellValue();
		double b13 = sheetDerecognition.getRow(12).getCell(1).getNumericCellValue();
		double b14 = sheetDerecognition.getRow(13).getCell(1).getNumericCellValue();
		double b15 = sheetDerecognition.getRow(14).getCell(1).getNumericCellValue();
		double b17 = sheetDerecognition.getRow(16).getCell(1).getNumericCellValue();
		double b16 = sheetDerecognition.getRow(15).getCell(1).getNumericCellValue();
		double b19 = sheetDerecognition.getRow(18).getCell(1).getNumericCellValue();
		double b20 = sheetDerecognition.getRow(19).getCell(1).getNumericCellValue();

		map.put("b3", b3 + "");
		map.put("b5", b5 + "");
		map.put("b6", b6 + "");
		map.put("b7", b7 + "");
		map.put("b8", b8 + "");
		map.put("b9", b9 + "");
		map.put("b10", b10 + "");
		map.put("b11", b11 + "");
		map.put("b12", b12 + "");
		map.put("b13", b13 + "");
		map.put("b14", b14 + "");
		map.put("b15", b15 + "");
		map.put("b19", b19 + "");

		double fcdr = 0;
		double accrued = 0;
		double ll = 0;
		double bank = 0;
		double fcdrTermination = 0;
		double accruedTermination = 0;
		double prepaidTermination = 0;
		double expenseTermination = 0;
		double llTermination = 0;
		double rouTermination = 0;
		double gainTermination = 0;
		double fbankTermination = 0;
		double prepaid = 0;

		if (entry.getPaymentIntervals().equalsIgnoreCase("Yearly")) {

			if (entry.getRecognitionOptions().equalsIgnoreCase("Apportioned")) {

				// for the final right side column
				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(6).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(7).getCell(5));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(5));
				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(5));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {

					fcdr = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					// fcdrTermination =
					// sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();

					accrued = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					// accruedTermination =
					// sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();

					ll = sheetDerecognition.getRow(6).getCell(4).getNumericCellValue();
					bank = sheetDerecognition.getRow(7).getCell(5).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(10).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(11).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(12).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(13).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(14).getCell(5).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(2).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}

			} else if (entry.getRecognitionOptions().equalsIgnoreCase("No Payment")) {

				evaluateCell(evaluator, sheetDerecognition.getRow(4).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(5).getCell(4));
				evaluateCell(evaluator, sheetDerecognition.getRow(10).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(11).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(12).getCell(8));
				evaluateCell(evaluator, sheetDerecognition.getRow(13).getCell(8));
				// for final right column for termination

				evaluateCell(evaluator, sheetDerecognition.getRow(14).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(15).getCell(9));
				evaluateCell(evaluator, sheetDerecognition.getRow(16).getCell(9));

				if (entry.getPaymentsAt().equalsIgnoreCase("Ending")) {
					expenseTermination = sheetDerecognition.getRow(10).getCell(8).getNumericCellValue();

					// fcdr =
					// sheetDerecognition.getRow(11).getCell(8).getNumericCellValue();
					// accrued =
					// sheetDerecognition.getRow(12).getCell(8).getNumericCellValue();
					fcdrTermination = sheetDerecognition.getRow(4).getCell(4).getNumericCellValue();
					accruedTermination = sheetDerecognition.getRow(5).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(13).getCell(8).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(14).getCell(9).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(15).getCell(9).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(16).getCell(9).getNumericCellValue();

				} else if (entry.getPaymentsAt().equalsIgnoreCase("Beginning")) {

					evaluateCell(evaluator, sheetDerecognition.getRow(20).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(21).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(22).getCell(4));
					// for final right column for termination

					evaluateCell(evaluator, sheetDerecognition.getRow(26).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(27).getCell(4));
					evaluateCell(evaluator, sheetDerecognition.getRow(28).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(29).getCell(5));
					evaluateCell(evaluator, sheetDerecognition.getRow(30).getCell(5));

					fcdr = sheetDerecognition.getRow(20).getCell(4).getNumericCellValue();
					prepaid = sheetDerecognition.getRow(21).getCell(5).getNumericCellValue();
					ll = sheetDerecognition.getRow(22).getCell(4).getNumericCellValue();

					expenseTermination = sheetDerecognition.getRow(26).getCell(4).getNumericCellValue();
					llTermination = sheetDerecognition.getRow(27).getCell(4).getNumericCellValue();
					rouTermination = sheetDerecognition.getRow(28).getCell(5).getNumericCellValue();
					gainTermination = sheetDerecognition.getRow(29).getCell(5).getNumericCellValue();
					fbankTermination = sheetDerecognition.getRow(30).getCell(5).getNumericCellValue();

				}
			}
		}

		map.put("financeCostDr", fcdr + "");
		map.put("financeCostDrTer", fcdrTermination + "");
		map.put("AccruedDr", accrued + "");
		map.put("AccruedDrTer", accruedTermination + "");
		map.put("LeaseLiabilityDr", ll + "");
		map.put("BankCr", bank + "");

		map.put("expenseTermination", expenseTermination + "");
		map.put("llTermination", llTermination + "");
		map.put("rouTermination", rouTermination + "");
		map.put("gainTermination", gainTermination + "");
		map.put("bankTermination", fbankTermination + "");
		map.put("prepaid", prepaid + "");
		map.put("prepaidTer", prepaidTermination + "");

		Gson gson = new Gson();
		return gson.toJson(map);

	}

	/**
	 * @param wb
	 * @param entry
	 * @param journalType
	 * @param leaseType
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 *//*
		 * @SuppressWarnings("deprecation") public String
		 * calculateJournalYearlyDepreciationFTA(XSSFWorkbook wb, Entry entry,
		 * int journalType, int leaseType) throws InvalidFormatException,
		 * IOException, InvocationTargetException {
		 * 
		 * double rightOfUseOfAsset = 0.0;
		 * System.out.println("calculating Journal Yearly"); FormulaEvaluator
		 * evaluator = wb.getCreationHelper().createFormulaEvaluator();
		 * 
		 * System.out.println("starting loop"); XSSFSheet sheetRecognition =
		 * wb.getSheetAt(journalType);// .getSheet("Yearly // Journal ");entry
		 * XSSFSheet sheetLease = wb.getSheetAt(leaseType); int leaseTerms =
		 * entry.getLeaseTerm(); int count = 0; LinkedHashMap<String, String>
		 * map = new LinkedHashMap<String, String>(); int startingRowLease = 16;
		 * 
		 * for (Row r : sheetLease) { /// ONLY PUT COLUMN No in map id int row =
		 * r.getRowNum();
		 * 
		 * if (row >= startingRowLease) { evaluateCell(evaluator, r.getCell(1));
		 * // if (r.getCell(1).getNumericCellValue() ==
		 * sheetRetrospective.getRow(14).getCell(1) // .getNumericCellValue()) {
		 * // Cell c = r.getCell(3); // evaluateCell(evaluator, c); if
		 * (HSSFDateUtil.isCellDateFormatted(c)) { //LocalDateTime date =
		 * c.getLocalDateTimeCellValue();
		 * sheetRecognition.getRow(5).getCell(1).setCellValue(date); break; }
		 * //} } }
		 * 
		 * evaluateCell(evaluator, sheetRetrospective.getRow(15).getCell(1));
		 * map.put("leseLiabality",
		 * sheetRetrospective.getRow(15).getCell(1).getNumericCellValue() + "");
		 * 
		 * evaluateCell(evaluator, sheetRetrospective.getRow(28).getCell(1));
		 * map.put("RightToUse",
		 * sheetRetrospective.getRow(28).getCell(1).getNumericCellValue() + "");
		 * 
		 * evaluateCell(evaluator, sheetRetrospective.getRow(29).getCell(1));
		 * map.put("RetainedEarning",
		 * sheetRetrospective.getRow(29).getCell(1).getNumericCellValue() + "");
		 * 
		 * // FOR GETTING COMULATIVE DATA for (Row r : sheetLease) { /// ONLY
		 * PUT COLUMN No in map id int row = r.getRowNum();
		 * 
		 * if (row >= startingRowLease) { evaluateCell(evaluator, r.getCell(1));
		 * if (r.getCell(1).getNumericCellValue() ==
		 * sheetCumulative.getRow(14).getCell(1).getNumericCellValue()) { Cell c
		 * = r.getCell(3); evaluateCell(evaluator, c); if
		 * (HSSFDateUtil.isCellDateFormatted(c)) { LocalDateTime date =
		 * c.getLocalDateTimeCellValue();
		 * sheetCumulative.getRow(5).getCell(1).setCellValue(date); break; } } }
		 * }
		 * 
		 * evaluateCell(evaluator, sheetCumulative.getRow(15).getCell(1));
		 * map.put("leseLiabalityCumulative",
		 * sheetCumulative.getRow(15).getCell(1).getNumericCellValue() + "");
		 * 
		 * evaluateCell(evaluator, sheetCumulative.getRow(33).getCell(1));
		 * map.put("RightToUseCumulative",
		 * sheetCumulative.getRow(33).getCell(1).getNumericCellValue() + "");
		 * 
		 * evaluateCell(evaluator, sheetCumulative.getRow(34).getCell(1));
		 * map.put("RetainedEarningCumulative",
		 * sheetCumulative.getRow(34).getCell(1).getNumericCellValue() + "");
		 * 
		 * Gson gson = new Gson(); return gson.toJson(map); }
		 */

	private int getCellMonth(Entry entry, Cell c) {
		if (HSSFDateUtil.isCellDateFormatted(c)) {
			LocalDateTime date = c.getLocalDateTimeCellValue();
			int month = date.getMonthValue();
			return month;
		}
		return 0;
	}

	private int getMonthCell(int month, XSSFRow headingRow, FormulaEvaluator evaluator) {
		// String monthSelected = getMonth(month);
		String text = (month < 10 ? "0" : "") + month;
		month = Integer.parseInt(text);
		for (int i = 5; i < 17; i++) {
			Cell c = headingRow.getCell(i);
			evaluateCell(evaluator, c);

			Date date = c.getDateCellValue();
			if (date.getMonth() + 1 == month)
				// if(c.getDateCellValue() == monthSelected))
				return i;
		}

		return 0;
	}

	private String getMonth(int month) {
		switch (month) {
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
		for (Cell c : cells) {
			evaluateCell(evaluator, c);
		}

	}

	protected void evaluateInCell(FormulaEvaluator evaluator, Cell... cells) {
		for (Cell c : cells) {
			evaluateInCell(evaluator, c);
		}

	}

	protected CellType evaluateCell(FormulaEvaluator evaluator, Cell c) {
		CellType type = null;

		if (c != null && c.getCellType() == CellType.FORMULA) {
			try {
				type = evaluator.evaluateFormulaCell(c);
			} catch (Exception ex) {
				System.out.println("In Exception in loop" + ex);
			}
		}
		return type;
	}

	protected Cell evaluateInCell(FormulaEvaluator evaluator, Cell c) {
		Cell type = null;
		if (c.getCellType() == CellType.FORMULA) {
			try {
				type = evaluator.evaluateInCell(c);
			} catch (Exception ex) {
				System.out.println("In Exception in loop" + ex);
			}
		}
		return type;
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> calculateJournalRightSide(XSSFWorkbook wb, Entry entry)
			throws InvalidFormatException, IOException {

		System.out.println("calculating Journal");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		XSSFSheet sheet = wb.getSheet("Yearly Journal entry");

		LinkedHashMap<String, LinkedHashMap<String, String>> mapSheet = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		System.out.println("In sheet" + sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		int year = entry.getYear();
		// int year = 2019;

		// int yearRowNum = findRow(sheet, year);
		// int nextYearRowNum = findRow(sheet, year+1);
		// int lastRow = findRow(sheet, "Logic will be the same for next
		// periods");

		// if(nextYearRowNum == 0) nextYearRowNum = sheet.getLastRowNum(); else
		// nextYearRowNum = nextYearRowNum-1 ;
		// if(nextYearRowNum == 0) nextYearRowNum = lastRow; else nextYearRowNum
		// = nextYearRowNum-1 ;

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (r.getRowNum() >= 0 && r.getRowNum() < 100) {

				LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();

				System.out.println("In Row" + r.getRowNum());

				for (Cell c : r) {
					if (c.getColumnIndex() >= 19) {
						CellType cellType = null;
						if (c.getCellType() == CellType.FORMULA) {
							try {
								evaluator.evaluateFormulaCell(c);
							} catch (Exception ex) {
								System.out.println("In Exception in loop" + ex);
								mapRow.put(c.getColumnIndex() + "", ":" + "");
								System.out.println("In error" + ex);
							}
							cellType = c.getCachedFormulaResultType();
						} else {
							cellType = c.getCellType();
						}
						putinMap(mapRow, c, cellType);

					}
				}
				mapSheet.put(row + 1 + "", mapRow);
				count++;
			}

		}

		System.out.println("returning Lease map");
		return mapSheet;
	}

	private boolean isSameMonth(String month, Cell c, FormulaEvaluator evaluator) {
		evaluator.evaluateFormulaCell(c);
		String cellValue = c.getNumericCellValue() + "";
		if (cellValue.equals(month)) {
			return true;
		}
		return false;
	}

	private static int findRow(XSSFSheet sheet, int cellContent) {

		XSSFCell s = sheet.getRow(4).getCell(19);
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

		XSSFCell s = sheet.getRow(4).getCell(19);
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

	protected void putinMapDepreciation(LinkedHashMap<String, String> mapSheet, Cell c, CellType cellType, Entry entry,
			double rightOfAsset) {

		String cellLocation = c.getColumnIndex() + "";

		switch (cellType) {

		case NUMERIC:
			double finalRightOfUseAsset = 0;
			double finalClosingBalance = 0;
			if (HSSFDateUtil.isCellDateFormatted(c)) {
				int something = 0;
				int y = c.getColumnIndex();
				System.out.println(y);
				Date d = c.getDateCellValue();
				mapSheet.put(cellLocation, c.getColumnIndex() == 9 ? month(c.getDateCellValue().getMonth()) + ""
						: formatDate(c.getDateCellValue()) + "");
			} else {
				if (c.getColumnIndex() == 16) {
					mapSheet.put(cellLocation, rightOfAsset + "");

				} else if (c.getColumnIndex() == 17 || c.getColumnIndex() == 18) {

					// double rightOfAsset = c.getNumericCellValue();

					Date startingDate = entry.getCommencementDate();
					Date deteEnding = addYearsToDate(entry.getCommencementDate(), entry.getUsefulLifeOfTheAsset());
					Calendar calStart = Calendar.getInstance();
					calStart.setTime(startingDate);

					Calendar mycal = new GregorianCalendar(entry.getYear(), entry.getMonth() - 1, 30);

					int daysInMonth;

					long totalDays = ChronoUnit.DAYS.between(startingDate.toInstant(), deteEnding.toInstant());
					totalDays = totalDays + 1;
					double right = rightOfAsset / totalDays;
					int noOfDaysInMonth = getMonthDays(entry.getYear(), entry.getMonth(), 4, startingDate, deteEnding);

					if (mycal.getTime().getMonth() == calStart.getTime().getMonth()
							&& mycal.getTime().getYear() == calStart.getTime().getYear()) {
						int startDay = calStart.getTime().getDate();
						daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

					} else {
						daysInMonth = noOfDaysInMonth;
					}

					Date date = new GregorianCalendar(entry.getYear(), entry.getMonth() - 1, daysInMonth).getTime();

					long totalDaysForDepreciation = ChronoUnit.DAYS.between(startingDate.toInstant(), date.toInstant());

					totalDaysForDepreciation = totalDaysForDepreciation + 2;
					finalRightOfUseAsset = right * totalDaysForDepreciation;
					if (c.getColumnIndex() == 17) {
						if (totalDaysForDepreciation < 0) {
							mapSheet.put(cellLocation, 0 + "");
							mapSheet.put("16", 0 + "");
						} else {
							mapSheet.put(cellLocation, finalRightOfUseAsset + "");
						}
					} else {
						if (totalDaysForDepreciation < 0) {
							mapSheet.put("16", 0 + "");
							mapSheet.put(cellLocation, 0 + "");
						} else {
							finalClosingBalance = rightOfAsset - finalRightOfUseAsset;
							mapSheet.put(cellLocation, finalClosingBalance + "");
						}
					}

				}

				else {
					mapSheet.put(cellLocation, c.getNumericCellValue() + "");
				}

				if (finalClosingBalance < 0) {
					mapSheet.put("16", 0 + "");
					mapSheet.put("17", 0 + "");
					mapSheet.put("18", 0 + "");

				}
			}
			break;
		case STRING:
			mapSheet.put(cellLocation, c.getRichStringCellValue() + "");
			break;
		case BOOLEAN:
			mapSheet.put(cellLocation, c.getBooleanCellValue() + "");
		default:
			mapSheet.put(cellLocation, "-" + "");
			break;
		}
	}

	protected void putinMap(LinkedHashMap<String, String> mapSheet, Cell c, CellType cellType) {

		String cellLocation = c.getColumnIndex() + "";

		switch (cellType) {

		case NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(c)) {
				int something = 0;
				int y = c.getColumnIndex();
				System.out.println(y);
				Date d = c.getDateCellValue();
				mapSheet.put(cellLocation, c.getColumnIndex() == 9 ? month(c.getDateCellValue().getMonth()) + ""
						: formatDate(c.getDateCellValue()) + "");
			} else {
				c.getRow().getRowNum();
				mapSheet.put(cellLocation, c.getNumericCellValue() + "");
			}
			break;
		case STRING:
			mapSheet.put(cellLocation, c.getRichStringCellValue() + "");
			break;
		case BOOLEAN:
			mapSheet.put(cellLocation, c.getBooleanCellValue() + "");
		default:
			mapSheet.put(cellLocation, "-" + "");
			break;
		}
	}

	private String month(int num) {
		switch (num) {
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

	public Date addYearsToDate(Date date, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		Date yearsAhead = cal.getTime();
		return yearsAhead;
	}

	private Integer getMonthDays(int year, int month, int day, Date startingDate, Date deteEnding) {

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(startingDate);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(deteEnding);

		// Create a calendar object and set year and month
		Calendar mycal = new GregorianCalendar(year, month - 1, 1);
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (mycal.getTime().getMonth() == calStart.getTime().getMonth()
				&& mycal.getTime().getYear() == calStart.getTime().getYear()) {
			int startDay = calStart.getTime().getDate();
			startDay = startDay - 1;
			daysInMonth = daysInMonth - startDay;

		} else if (mycal.getTime().getMonth() == calEnd.getTime().getMonth()
				&& mycal.getTime().getYear() == calEnd.getTime().getYear()) {
			int endDay = calEnd.getTime().getDate();
			// endDay = endDay - 1;
			// daysInMonth = daysInMonth - endDay;
			daysInMonth = endDay;

		}

		// Get the number of days in that month
		// 28

		return daysInMonth;
	}

	private Date dateCellEvaluatorWithType(FormulaEvaluator evaluator, Date sheetUpdatedDate, Cell dateCell) {
		if (dateCell.getCellType() == CellType.FORMULA) {
			try {
				evaluator.evaluateFormulaCell(dateCell);
			} catch (Exception ex) {
				System.out.println("In Exception in loop" + ex);
				// mapRow.put(c.getColumnIndex() + "", ":" +
				// "");
				System.out.println("In error" + ex);
			}
			CellType cType = dateCell.getCachedFormulaResultType();

			switch (cType) {

			case NUMERIC:
				sheetUpdatedDate = dateCell.getDateCellValue();
				break;
			case STRING:
				sheetUpdatedDate = null;
				break;

			default:
				sheetUpdatedDate = null;
				break;
			}

		}
		return sheetUpdatedDate;
	}

}