package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
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

public class CalculationFTA extends Calculation {

	public CalculationFTA() throws IOException, InvalidFormatException {
		super();
		// TODO Auto-generated constructor stub
	}

	public String entryFirstTimeAdoption(Entry entry, TYPESFTA outPutTab, TYPESFTA inputTab) throws Exception {

		try {

			OPCPackage pkg;
			System.out.println("opening file");
			InputStream file = getFile("Firsttimeadoption");
			System.out.println("back from return");
			pkg = OPCPackage.open(file);

			XSSFWorkbook wb = new XSSFWorkbook(pkg);

			// Sheet sheetOutPut = wb.getSheet("Retrospective Journalentry");
			Sheet sheet = wb.getSheet("Lease");

			System.out.println(sheet.getSheetName());
			updateValues(entry, sheet);
			System.out.println("updating");
			// printValues(sheet);

			switch (outPutTab) {
			case RETROSPECTIVE:
			case CUMMULATIVE:
				json = calculateFTA(wb, entry, outPutTab.getValue(), inputTab.getValue());
				break;
			case LEASE:
				json = calculateFTALease(wb, entry, outPutTab.getValue());
				break;

			case SCHEDULELEASEREPORT:
				json = calculateScheduleLeaseReport1(wb, entry, outPutTab.getValue());
				break;

			case LEASELIABILITYREPORT:
				json = calculateLeaseLiabilityReport(wb, entry, outPutTab.getValue());
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

	public String calculateFTALease(XSSFWorkbook wb, Entry entry, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating FTA Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
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
				mapRow.put("lessorName", entry.getLessorName());
				mapRow.put("location", entry.getLocation());
				mapRow.put("leaseContractNo", entry.getLeaseContractNo());
				mapRow.put("assetCode", entry.getAssetCode());
				mapSheet.put(row + 1 + "", mapRow);
				count++;
			}

		}

		System.out.println("returning FTA  map");
		Gson gson = new Gson();
		return gson.toJson(mapSheet);

	}

	public String calculateScheduleLeaseReport1(XSSFWorkbook wb, Entry entry, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating FTA Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = wb.getSheetAt(leaseType);

		System.out.println("In sheet" + sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		double right = 0;
		LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();
		double someting;
		for (Row r : sheet) { /// ONLY PUT COLUMN No in map id

			// && r.getCell(15).getDateCellValue().getYear() == entry.getYear()
			if (r.getRowNum() >= 16 && count < leaseTerms) {
				if (r.getRowNum() == 16) {

					// right = r.getCell(16).getNumericCellValue();

					for (Cell c : r) {
						CellType cellType = null;
						if (c.getColumnIndex() == 16) {
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
							right = r.getCell(16).getNumericCellValue();
							break;

						}
					}
				}

				/// subtracted 5 from year because it was giving data of 5 rows
				/// // below
				// to the selected year int s = 0; s = 1;

				if (r.getCell(15).getDateCellValue().getYear() + 1900 == entry.getYear() - 5) {

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
						putinMapDepreciation(mapRow, c, cellType, entry, right);
					}

					mapRow.put("lessorName", entry.getLessorName());
					mapRow.put("location", entry.getLocation());
					mapRow.put("leaseContractNo", entry.getLeaseContractNo());
					mapRow.put("assetCode", entry.getAssetCode());
					mapRow.put("dataId", entry.getDataId());

					count++;
					break;
				}
			}

		}

		System.out.println("returning FTA  map");
		Gson gson = new Gson();
		return gson.toJson(mapRow);

	}

	public String calculateScheduleLeaseReport(XSSFWorkbook wb, Entry entry, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating FTA Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = wb.getSheetAt(leaseType);

		System.out.println("In sheet" + sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		double rightValue = 0;
		LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();
		double someting;
		for (Row r : sheet) {
			int row = r.getRowNum();
			/// ONLY PUT COLUMN No in map id

			// && r.getCell(15).getDateCellValue().getYear() == entry.getYear()
			if (r.getRowNum() >= 16 && count < leaseTerms) {

				System.out.println(r.getRowNum() + "");
				Date sheetUpdatedDate = null;
				Date sheetUpdatedDateDownCell = null;
				Row downRow = sheet.getRow(row + 1);
				Cell dateCellDown = downRow.getCell(8);
				// Row rowDown = r.getRowNum()+1;
				Cell dateCell = r.getCell(8);
				double rowPaymentValue = 0;
				Cell rowEndPayment = r.getCell(4);
				Cell rightCell = r.getCell(16);

				rowPaymentValue = cellEvaluatorWithType(evaluator, rowPaymentValue, rowEndPayment);

				if (r.getRowNum() == 16) {

					for (Cell c : r) {
						CellType cellType = null;
						if (c.getColumnIndex() == 16) {

							rightValue = cellEvaluatorWithType(evaluator, rightValue, rightCell);
							break;
						}
					}
				}

				if (rowEndPayment != null && rowPaymentValue > 0) {
					sheetUpdatedDate = dateCellEvaluatorWithType(evaluator, sheetUpdatedDate, dateCell);
					sheetUpdatedDateDownCell = dateCellEvaluatorWithType(evaluator, sheetUpdatedDateDownCell,
							dateCellDown);
					Boolean conditionCheck = false;
					if (entry.getPaymentIntervals().equalsIgnoreCase("Yearly")) {
						if (sheetUpdatedDateDownCell == null) {
							conditionCheck = (sheetUpdatedDate != null
									&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
									&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
						} else if (sheetUpdatedDateDownCell != null) {
							conditionCheck = checkDateComesBetweenTwoDates(entry.getYear(), entry.getMonth() - 1, 1,
									sheetUpdatedDate, sheetUpdatedDateDownCell);
						}
						// conditionCheck = (sheetUpdatedDate != null
						// && sheetUpdatedDate.getYear() + 1900 ==
						// entry.getYear());
					} else if (entry.getPaymentIntervals().equalsIgnoreCase("Monthly")) {
						conditionCheck = (sheetUpdatedDate != null
								&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
								&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
					} else if (entry.getPaymentIntervals().equalsIgnoreCase("Quarterly")) {
						if (sheetUpdatedDateDownCell == null) {
							conditionCheck = (sheetUpdatedDate != null
									&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
									&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
						} else if (sheetUpdatedDateDownCell != null) {
							conditionCheck = checkDateComesBetweenTwoDates(entry.getYear(), entry.getMonth() - 1, 1,
									sheetUpdatedDate, sheetUpdatedDateDownCell);
						}
					}
					// subtracted 5 from year because it was giving data of 5
					// rows
					// below to the selected year

					if (conditionCheck) {

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
							putinMapDepreciation(mapRow, c, cellType, entry, rightValue);
						}

						mapRow.put("lessorName", entry.getLessorName());
						mapRow.put("location", entry.getLocation());
						mapRow.put("leaseContractNo", entry.getLeaseContractNo());
						mapRow.put("assetCode", entry.getAssetCode());
						mapRow.put("dataId", entry.getDataId());

						break;
					}

					else {
						mapRow.put("lessorName", entry.getLessorName());
						mapRow.put("location", entry.getLocation());
						mapRow.put("leaseContractNo", entry.getLeaseContractNo());
						mapRow.put("assetCode", entry.getAssetCode());
						mapRow.put("dataId", entry.getDataId());
						mapRow.put("16", 0 + "");
						mapRow.put("17", 0 + "");
						mapRow.put("18", 0 + "");
					}
					// count++;

				}
				count++;
			}

		}

		System.out.println("returning FTA  map");
		Gson gson = new Gson();
		return gson.toJson(mapRow);

	}

	public String calculateLeaseLiabilityReport(XSSFWorkbook wb, Entry entry, int leaseType)
			throws InvalidFormatException, IOException {

		System.out.println("calculating FTA Lease");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = wb.getSheetAt(leaseType);

		System.out.println("In sheet" + sheet.getSheetName());
		int leaseTerms = entry.getLeaseTerm();
		int count = 0;
		double right = 0;
		LinkedHashMap<String, String> mapRow = new LinkedHashMap<String, String>();
		double rightValue = 0;
		Date commencementDate = entry.getCommencementDate();

		for (Row r : sheet) {
			/// ONLY PUT COLUMN No in map id

			// && r.getCell(15).getDateCellValue().getYear() == entry.getYear()
			int row = r.getRowNum();
			System.out.println(r.getRowNum() + "");
			if (r.getRowNum() >= 16 && count < leaseTerms) {

				Date sheetUpdatedDate = null;
				Date sheetUpdatedDateDownCell = null;
				Row downRow = sheet.getRow(row + 1);
				Cell dateCellDown = downRow.getCell(8);
				// Row rowDown = r.getRowNum()+1;
				Cell dateCell = r.getCell(8);
				double rowPaymentValue = 0;
				Cell rowEndPayment = r.getCell(4);

				rowPaymentValue = cellEvaluatorWithType(evaluator, rowPaymentValue, rowEndPayment);

				if (rowEndPayment != null && rowPaymentValue > 0) {

					sheetUpdatedDate = dateCellEvaluatorWithType(evaluator, sheetUpdatedDate, dateCell);
					sheetUpdatedDateDownCell = dateCellEvaluatorWithType(evaluator, sheetUpdatedDateDownCell,
							dateCellDown);

					Boolean conditionCheck = false;

					if (r.getRowNum() == 16 && entry.getPaymentsAt().equalsIgnoreCase("Ending")) {

						rightValue = cellEvaluatorWithType(evaluator, rightValue, r.getCell(9));
						conditionCheck = checkDateComesBetweenTwoDates(entry.getYear(), entry.getMonth() - 1, 1,
								commencementDate, sheetUpdatedDate);

						if (conditionCheck) {
							mapRow.put("12", rightValue + "");
							mapRow.put("lessorName", entry.getLessorName());
							mapRow.put("location", entry.getLocation());
							mapRow.put("leaseContractNo", entry.getLeaseContractNo());
							mapRow.put("assetCode", entry.getAssetCode());
							mapRow.put("dataId", entry.getDataId());
							break;
						}

					}
					if (entry.getPaymentIntervals().equalsIgnoreCase("Yearly")) {

						if (sheetUpdatedDateDownCell == null) {
							conditionCheck = (sheetUpdatedDate != null
									&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
									&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
						}

						else if (sheetUpdatedDateDownCell != null) {
							conditionCheck = checkDateComesBetweenTwoDates(entry.getYear(), entry.getMonth() - 1, 1,
									sheetUpdatedDate, sheetUpdatedDateDownCell);
						}

					} else if (entry.getPaymentIntervals().equalsIgnoreCase("Monthly")) {
						conditionCheck = (sheetUpdatedDate != null
								&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
								&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
					} else if (entry.getPaymentIntervals().equalsIgnoreCase("Quarterly")) {

						if (sheetUpdatedDateDownCell == null) {
							conditionCheck = (sheetUpdatedDate != null
									&& sheetUpdatedDate.getYear() + 1900 == entry.getYear()
									&& sheetUpdatedDate.getMonth() == entry.getMonth() - 1);
						} else if (sheetUpdatedDateDownCell != null) {
							conditionCheck = checkDateComesBetweenTwoDates(entry.getYear(), entry.getMonth() - 1, 1,
									sheetUpdatedDate, sheetUpdatedDateDownCell);
						}
					}
					if (conditionCheck) {

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

						mapRow.put("lessorName", entry.getLessorName());
						mapRow.put("location", entry.getLocation());
						mapRow.put("leaseContractNo", entry.getLeaseContractNo());
						mapRow.put("assetCode", entry.getAssetCode());
						mapRow.put("dataId", entry.getDataId());

						break;
					} else {
						mapRow.put("12", "0");
						mapRow.put("lessorName", entry.getLessorName());
						mapRow.put("location", entry.getLocation());
						mapRow.put("leaseContractNo", entry.getLeaseContractNo());
						mapRow.put("assetCode", entry.getAssetCode());
						mapRow.put("dataId", entry.getDataId());
					}

				}
				count++;

			}
		}
		// }

		System.out.println("returning FTA  map");
		Gson gson = new Gson();
		return gson.toJson(mapRow);

	}

	private Boolean checkDateComesBetweenTwoDates(int year, int month, int date, Date sheetUpdatedDate,
			Date sheetUpdatedDateDownCell) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(sheetUpdatedDate);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(sheetUpdatedDateDownCell);

		Calendar cc1 = Calendar.getInstance();
		// cc.set(year, month - 1, 1);

		cc1.set(year, month, 30);
		cc1.getTime();

		return cc1.getTime().compareTo(calStart.getTime()) >= 0 && cc1.getTime().compareTo(calEnd.getTime()) <= 0;
		// return cc1.getTime().after(calStart.getTime()) ||
		// cc1.before(calEnd.getTime());
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

	private double cellEvaluatorWithType(FormulaEvaluator evaluator, double rowPaymentValue, Cell rowEndPayment) {
		if (rowEndPayment.getCellType() == CellType.FORMULA) {
			try {
				evaluator.evaluateFormulaCell(rowEndPayment);
			} catch (Exception ex) {
				System.out.println("In Exception in loop" + ex);
			}
			CellType cTypePayment = rowEndPayment.getCachedFormulaResultType();

			switch (cTypePayment) {

			case NUMERIC:
				rowPaymentValue = rowEndPayment.getNumericCellValue();
				break;
			case STRING:
				rowPaymentValue = 0;
				break;

			default:
				rowPaymentValue = 0;
			}
		}
		return rowPaymentValue;
	}

	public String calculateFTA(XSSFWorkbook wb, Entry entry, int outputTab, int inputTab)
			throws InvalidFormatException, IOException {

		System.out.println("calculating First Time Adoption");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		Sheet sheetRetrospective = wb.getSheet("Retrospective Journalentry");

		Sheet sheetCumulative = wb.getSheet("Cumulative Catchup Journalentry");

		Sheet sheetLease = wb.getSheet("Lease");
		System.out.println("In sheet" + sheetRetrospective.getSheetName());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRowLease = 16;

		// FOR GETTING RESTROSPECTIVE DATA
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (row >= startingRowLease) {
				evaluateCell(evaluator, r.getCell(1));
				if (r.getCell(1).getNumericCellValue() == sheetRetrospective.getRow(14).getCell(1)
						.getNumericCellValue()) {
					Cell c = r.getCell(3);
					evaluateCell(evaluator, c);
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						sheetRetrospective.getRow(5).getCell(1).setCellValue(date);
						break;
					}
				}
			}
		}

		evaluateCell(evaluator, sheetRetrospective.getRow(7).getCell(1));
		sheetRetrospective.getRow(7).getCell(1).getErrorCellValue(); // = 42

		evaluateCell(evaluator, sheetRetrospective.getRow(15).getCell(1));
		map.put("leseLiabality", sheetRetrospective.getRow(15).getCell(1).getStringCellValue() + "");

		evaluateCell(evaluator, sheetRetrospective.getRow(28).getCell(1));
		map.put("RightToUse", sheetRetrospective.getRow(28).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetRetrospective.getRow(29).getCell(1));
		map.put("RetainedEarning", sheetRetrospective.getRow(29).getCell(1).getNumericCellValue() + "");

		// FOR GETTING COMULATIVE DATA
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (row >= startingRowLease) {
				evaluateCell(evaluator, r.getCell(1));
				if (r.getCell(1).getNumericCellValue() == sheetCumulative.getRow(14).getCell(1).getNumericCellValue()) {
					Cell c = r.getCell(3);
					evaluateCell(evaluator, c);
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						sheetCumulative.getRow(5).getCell(1).setCellValue(date);
						break;
					}
				}
			}
		}

		evaluateCell(evaluator, sheetCumulative.getRow(15).getCell(1));
		map.put("leseLiabalityCumulative", sheetCumulative.getRow(15).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetCumulative.getRow(33).getCell(1));
		map.put("RightToUseCumulative", sheetCumulative.getRow(33).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetCumulative.getRow(34).getCell(1));
		map.put("RetainedEarningCumulative", sheetCumulative.getRow(34).getCell(1).getNumericCellValue() + "");

		Gson gson = new Gson();
		return gson.toJson(map);

	}

	public String calculateFTADerognition(XSSFWorkbook wb, Entry entry, int outputTab, int inputTab)
			throws InvalidFormatException, IOException {

		System.out.println("calculating First Time Adoption");
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		System.out.println("starting loop");
		Sheet sheetRetrospective = wb.getSheet("Retrospective Journalentry");

		Sheet sheetCumulative = wb.getSheet("Cumulative Catchup Journalentry");

		Sheet sheetLease = wb.getSheet("Lease");
		System.out.println("In sheet" + sheetRetrospective.getSheetName());
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		int startingRowLease = 16;

		// FOR GETTING RESTROSPECTIVE DATA
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (row >= startingRowLease) {
				evaluateCell(evaluator, r.getCell(1));
				if (r.getCell(1).getNumericCellValue() == sheetRetrospective.getRow(14).getCell(1)
						.getNumericCellValue()) {
					Cell c = r.getCell(3);
					evaluateCell(evaluator, c);
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						sheetRetrospective.getRow(5).getCell(1).setCellValue(date);
						break;
					}
				}
			}
		}

		evaluateCell(evaluator, sheetRetrospective.getRow(15).getCell(1));
		map.put("leseLiabality", sheetRetrospective.getRow(15).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetRetrospective.getRow(28).getCell(1));
		map.put("RightToUse", sheetRetrospective.getRow(28).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetRetrospective.getRow(29).getCell(1));
		map.put("RetainedEarning", sheetRetrospective.getRow(29).getCell(1).getNumericCellValue() + "");

		// FOR GETTING COMULATIVE DATA
		for (Row r : sheetLease) {
			/// ONLY PUT COLUMN No in map id
			int row = r.getRowNum();

			if (row >= startingRowLease) {
				evaluateCell(evaluator, r.getCell(1));
				if (r.getCell(1).getNumericCellValue() == sheetCumulative.getRow(14).getCell(1).getNumericCellValue()) {
					Cell c = r.getCell(3);
					evaluateCell(evaluator, c);
					if (HSSFDateUtil.isCellDateFormatted(c)) {
						LocalDateTime date = c.getLocalDateTimeCellValue();
						sheetCumulative.getRow(5).getCell(1).setCellValue(date);
						break;
					}
				}
			}
		}

		evaluateCell(evaluator, sheetCumulative.getRow(15).getCell(1));
		map.put("leseLiabalityCumulative", sheetCumulative.getRow(15).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetCumulative.getRow(33).getCell(1));
		map.put("RightToUseCumulative", sheetCumulative.getRow(33).getCell(1).getNumericCellValue() + "");

		evaluateCell(evaluator, sheetCumulative.getRow(34).getCell(1));
		map.put("RetainedEarningCumulative", sheetCumulative.getRow(34).getCell(1).getNumericCellValue() + "");

		Gson gson = new Gson();
		return gson.toJson(map);

	}

	public int getCell(String cellNo, Sheet sheet) {
		// CellReference
		int cell = org.apache.poi.ss.util.CellReference.convertColStringToIndex("B");
		return cell;

	}

}
