package com.example.boardofdirectorsServer.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.boardofdirectorsServer.model.ClassOfAsset;
import com.example.boardofdirectorsServer.model.Company;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.example.boardofdirectorsServer.repository.ClassOfAssetRepository;
import com.example.boardofdirectorsServer.repository.UserDataRepository;
import com.google.gson.Gson;

@Component
public class DataHelper {

	@Autowired
	UserDataRepository userDataRepository;
	@Autowired
	ClassOfAssetRepository classOfAssetRepository;
	@Autowired
	MongoOperations mongoOperation;
	@Autowired
	MongoTemplate mongoTemplate;
	Gson gson = new Gson();

	public String saveData(UserData data) {
		try {

			if (data.getUserId() == 0) {
				return "Failure: Only users can add Leases";
			}

			User user = getUserWithId(data.getUserId() + "");
			Boolean allow = allowSave(user, data);
			if (allow) {
				data.setId(getAvaiablaeDataId());
				userDataRepository.save(data);

				Gson gson = new Gson();
				return gson.toJson(data);
				// return "Success :user's data saved";
			} else {
				return "Failure: Lease limit exceed.Please change the payment schedule to Add more Leases.";
			}
		} catch (Exception ex) {
			throw ex;

		}
	}

	public String deleteAllData() {
		try {
			userDataRepository.deleteAll();
			return "user's data deleted";
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String deleteSelectedUserData(String leaseId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(leaseId));

			UserData userdata = mongoOperation.findOne(query, UserData.class);
			mongoOperation.remove(userdata);
			// mongoOperation.remove(query, UserData.class);
			// userDataRepository.deleteAll();
			return "user's data deleted";
		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<UserData> getUserData(int userId) {
		try {
			System.out.println("{ userId : '" + userId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");

			List<UserData> userdata = mongoOperation.find(query, UserData.class);

			System.out.println(userdata);
			if (userdata == null)
				return null;
			return userdata;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public String getUserDataUniqueRow(int userId, String columnName) {
		try {
			System.out.println("{ userId : '" + userId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));
			System.out.println("ff");
			List<UserData> userdata = mongoOperation.findDistinct(query, columnName, "userData", UserData.class);
			Gson gson = new Gson();
			return gson.toJson(userdata);

		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public UserData getUserDataByDataId(String dataId) {
		try {
			System.out.println("{ dataId : '" + dataId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(dataId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			UserData userdata = mongoOperation.findOne(query, UserData.class);
			// List<UserData> userdata = mongoOperation.find(query,
			// UserData.class);
			System.out.println(userdata);
			if (userdata == null)
				return null;
			return userdata;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public ResponseEntity<Resource> getUserFileByDataId(String dataId) throws Exception {
		try {
			System.out.println("{ dataId : '" + dataId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(dataId));
			System.out.println("ff");
			UserData userdata = mongoOperation.findOne(query, UserData.class);
			System.out.println(userdata);

			String UPLOADED_FOLDER = "C:/Users/Joni/git/boardofdirectorsServer/src/uploads/";

			File directoryPath = new File(UPLOADED_FOLDER);
			String fileName = "leaseId" + dataId + userdata.getFileName();
			FilenameFilter textFilefilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.startsWith(fileName)) {
						return true;
					} else {
						return false;
					}
				}
			};
			// List of all the text files
			// File filesList[] = directoryPath.listFiles(textFilefilter);
			File filesList[] = directoryPath.listFiles();
			System.out.println("List of the text files in the specified directory:");
			File userFile = null;
			for (File file : filesList) {
				if (file.getName().equalsIgnoreCase(fileName)) {
					userFile = file;
				}
				System.out.println("File name: " + file.getName());
				System.out.println("File path: " + file.getAbsolutePath());
				System.out.println("Size :" + file.getTotalSpace());
				System.out.println(" ");
			}

			return downloadFile(userFile);
		}
		// // return json;
		catch (

		Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public ResponseEntity<Resource> getFileByPath(String filePath) throws Exception {
		try {

			// String UPLOADED_FOLDER =
			// "C:/Users/Joni/git/boardofdirectorsServer/src/bulktemplate.xlsx/";
			String UPLOADED_FOLDER = filePath;
			File templateExcel = new File(UPLOADED_FOLDER);
			return downloadFile(templateExcel);
		}
		// // return json;
		catch (

		Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	private ResponseEntity<Resource> downloadFile(File userFile) throws FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		// InputStreamResource resource = new InputStreamResource(new
		// FileInputStream(userFile));

		Path path = Paths.get(userFile.getAbsolutePath());
		ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getName() + "\"")
				.contentLength(userFile.length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

	}

	public List<UserData> getCompanyData(int companyId) {
		try {
			System.out.println("{ companyId : '" + companyId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("companyId").is(companyId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			List<UserData> companyData = mongoOperation.find(query, UserData.class);
			System.out.println(companyData);
			if (companyData == null)
				return null;
			return companyData;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public List<ClassOfAsset> getUserClassOfAssets(int userId) {
		try {
			System.out.println("{ userId : '" + userId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));

			System.out.println("ff");

			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			// List<ClassOfAsset> userClassOfAsset = mongoOperation.find(query,
			// ClassOfAsset.class);

			List<ClassOfAsset> userClassOfAsset = mongoOperation.findDistinct(query, "classOfAsset", "classOfAsset",
					ClassOfAsset.class);
			System.out.println(userClassOfAsset);
			if (userClassOfAsset == null)
				return null;
			return userClassOfAsset;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public List<ClassOfAsset> getCompanyClassOfAsset(int companyId) {
		try {
			System.out.println("{  inside getCompanyClassOfASeet companyId : '" + companyId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("companyId").is(companyId));

			System.out.println("ff");
			List<ClassOfAsset> companyClassOfAssets = mongoOperation.findDistinct(query, "classOfAsset", "classOfAsset",
					ClassOfAsset.class);

			System.out.println(companyClassOfAssets);
			if (companyClassOfAssets == null)
				return null;
			return companyClassOfAssets;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public String getCompanyDataUniqueRow(int companyId, String columnName) {
		try {
			System.out.println("{ companyId : '" + companyId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("companyId").is(companyId));
			System.out.println("ff");
			List<UserData> userdata = mongoOperation.findDistinct(query, columnName, "userData", UserData.class);
			Gson gson = new Gson();
			return gson.toJson(userdata);

		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	/*
	 * public User getUser(int userId) { try {
	 * 
	 * System.out.println("{ userId : '" + userId + "'}");
	 * System.out.println("{ Mongooperation: '" + mongoOperation + "'}"); Query
	 * query = new Query();
	 * query.addCriteria(Criteria.where("userId").is(userId)); // BasicQuery
	 * query1 = new BasicQuery("{ name : '"+name+"'} , { // password:
	 * '"+password+"'}"); System.out.println("ff"); User userData = (User)
	 * mongoOperation.find(query, User.class); System.out.println(userData); if
	 * (userData == null) return null; return userData; // String json =
	 * gson.toJson(userdata); // return json; } catch (Exception ex) {
	 * System.out.println("Error is :" + ex.getMessage()); throw ex; } }
	 */

	public User getUserWithId(String userId) {
		String userJson;
		User user;
		try {
			Query query = new Query();
			int id = Integer.parseInt(userId);
			query.addCriteria(Criteria.where("userId").is(id));
			user = mongoOperation.findOne(query, User.class);
			userJson = gson.toJson(user);
		} catch (Exception ex) {
			throw ex;
		}
		return user;
	}

	public Company getCompanyWithId(String userId) {
		String userJson;
		Company company;
		try {
			Query query = new Query();
			int id = Integer.parseInt(userId);
			query.addCriteria(Criteria.where("companyId").is(id));
			company = mongoOperation.findOne(query, Company.class);
			userJson = gson.toJson(company);
		} catch (Exception ex) {
			throw ex;
		}
		return company;
	}

	private boolean allowSave(User user, UserData data) {
		List<UserData> userdata;
		if (user.getCompanyId() == 0) {
			userdata = getUserData(user.getUserId());
		} else {
			userdata = getCompanyData(user.getCompanyId());
		}

		// String user = getUserWithId(data.getUserId() + "");
		int userCounts = userdata.size();
		int trialAllowed = 1;
		int bronzeAllowed = 10;
		int silverAllowed = 30;
		int goldAllowed = 500;

		if (user.getPaymentSchedule().equals("trial") && userCounts < trialAllowed) {
			return true;

		} else if (user.getPaymentSchedule().equals("bronze") && userCounts < bronzeAllowed) {
			return true;

		} else if (user.getPaymentSchedule().equals("silver") && userCounts < silverAllowed) {
			return true;
		} else if (user.getPaymentSchedule().equals("gold")) {
			return true;

		}
		return false;
	}

	public String getClassOfAsset(ClassOfAsset classAssetEntity) {
		System.out.println("in get all users");
		String jsonAssets = null;
		List<ClassOfAsset> listClassOfAsset = null;

		if (classAssetEntity.getCompanyId() == 0) {
			listClassOfAsset = getUserClassOfAssets(classAssetEntity.getUserId());
		} else {
			listClassOfAsset = getCompanyClassOfAsset(classAssetEntity.getCompanyId());
		}
		// try {
		// List<ClassOfAsset> classOfAssets = classOfAssetRepository.findAll();
		// jsonAssets = gson.toJson(classOfAssets);
		// } catch (Exception ex) {
		// System.out.println(ex);
		// }
		jsonAssets = gson.toJson(listClassOfAsset);
		return jsonAssets;
	}

	public String saveData(ClassOfAsset data) {
		try {

			classOfAssetRepository.save(data);
			return "Success:data Class of Asset saved";

		} catch (Exception ex) {
			throw ex;
		}
	}

	public String getAvaiablaeDataId() {
		Long total = userDataRepository.count();
		int count = total.intValue();
		// return count + 1;
		return UUID.randomUUID() + "";

	}

	public String saveFileToSystem(MultipartFile file, String UPLOADED_FOLDER, String id) {
		// TODO Auto-generated method stub
		if (file.isEmpty()) {

			return "failure: please select a file";
		}

		try {
			// Integer dataId = Integer.parseInt(id);
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + "leaseId" + id + file.getOriginalFilename());
			Files.write(path, bytes);

			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));

			UserData userdata = mongoOperation.findOne(query, UserData.class);
			userdata.setFileName(file.getOriginalFilename());
			userDataRepository.save(userdata);
			return ("success: You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	public String saveBulkLease(MultipartFile file, String userIdString) throws IOException {
		// TODO Auto-generated method stub
		try {
			if (file.isEmpty()) {

				return "failure: please select a file";
			}
			String returnResult = "";
			Integer userId = 0;
			Integer companyId = 0;
			ArrayList<UserData> listUserData = null;
			User user = getUserWithId(userIdString);
			if (user != null) {
				userId = user.getUserId();
				companyId = user.getCompanyId();
			}

			listUserData = new ArrayList<UserData>();
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);

			XSSFRow row;
			XSSFCell cell;
			HashMap<String, String> mapError = new HashMap<String, String>();

			Iterator rows = sheet.rowIterator();

			while (rows.hasNext()) {
				UserData userData = new UserData();

				row = (XSSFRow) rows.next();
				// XSSFCell srNo = row.getCell((short) 0);
				XSSFCell leaseContractNo = row.getCell((short) 0);
				/*
				 * if (row.getRowNum() > 0 && (commencementDate == null ||
				 * commencementDate.getCellType() == CellType.BLANK)) {
				 * mapError.put(row.getRowNum() + "",
				 * "commencement date column must not be  null"); }
				 */

				if (row.getRowNum() > 0 && (leaseContractNo == null || leaseContractNo.getCellType() == CellType.BLANK))
					break;

				if (row.getRowNum() > 0) {
					System.out.println(row.getRowNum());
					Iterator cells = row.cellIterator();

					if (leaseContractNo.getCellType() == CellType.NUMERIC) {
						userData.setLeaseContractNo(leaseContractNo.getNumericCellValue() + "");
					} else if (leaseContractNo.getCellType() == CellType.STRING) {
						userData.setLeaseContractNo(leaseContractNo.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + leaseContractNo.getColumnIndex(),
								" leaseContractNo Must be Numeric");
					}

					XSSFCell lessorName = row.getCell((short) 1);

					/*
					 * if (lessorName.getCellType() == CellType.NUMERIC) {
					 * userData.setLessorName(lessorName.getNumericCellValue() +
					 * ""); } else
					 */
					if (lessorName.getCellType() == CellType.STRING) {
						userData.setLessorName(lessorName.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + lessorName.getColumnIndex(),
								"lessorName Must be String");
					}

					XSSFCell classOfAsset = row.getCell((short) 2);

					/*
					 * if (classOfAsset.getCellType() == CellType.NUMERIC) {
					 * userData.setClassOfAsset(classOfAsset.getNumericCellValue
					 * () + ""); } else
					 */
					if (classOfAsset.getCellType() == CellType.STRING) {
						userData.setClassOfAsset(classOfAsset.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + classOfAsset.getColumnIndex(),
								"classOfAsset Must be String");
					}

					XSSFCell assetCode = row.getCell((short) 3);

					if (assetCode.getCellType() == CellType.NUMERIC) {
						userData.setAssetCode(assetCode.getNumericCellValue() + "");
					} else

					if (assetCode.getCellType() == CellType.STRING) {
						userData.setAssetCode(assetCode.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + assetCode.getColumnIndex(),
								"assetCode Must be String");
					}

					XSSFCell assetDescription = row.getCell((short) 4);

					/*
					 * if (assetDescription.getCellType() == CellType.NUMERIC) {
					 * userData.setAssetDescription(assetDescription.
					 * getNumericCellValue() + ""); } else
					 */

					if (assetDescription.getCellType() == CellType.STRING) {
						userData.setAssetDescription(assetDescription.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + assetDescription.getColumnIndex(),
								"assetDescription Must be String");
					}

					XSSFCell location = row.getCell((short) 5);

					/*
					 * if (locaton.getCellType() == CellType.NUMERIC) {
					 * userData.setLocation(locaton.getNumericCellValue() + "");
					 * } else
					 */
					if (location.getCellType() == CellType.STRING) {
						userData.setLocation(location.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + location.getColumnIndex(),
								"Location Must be String");
					}

					XSSFCell commencementDate = row.getCell((short) 6);
					if (commencementDate.getCellType() == CellType.NUMERIC) {
						userData.setCommencementDate(commencementDate.getDateCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + commencementDate.getColumnIndex(),
								"CommencementDate Must be in a valid format");

					}

					XSSFCell paymentsAt = row.getCell((short) 7);

					if (paymentsAt.getCellType() == CellType.STRING) {
						userData.setPaymentsAt(paymentsAt.getStringCellValue() + "");
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + paymentsAt.getColumnIndex(),
								"PaymentsAt must be String");
					}
					/*
					 * else if (paymentsAt.getCellType() == CellType.STRING) {
					 * userData.setPaymentsAt(paymentsAt.getStringCellValue() +
					 * ""); }
					 */

					XSSFCell annualDiscountRate = row.getCell((short) 8);

					if (annualDiscountRate.getCellType() == CellType.NUMERIC) {
						userData.setAnnualDiscountRate((float) annualDiscountRate.getNumericCellValue());
					} else {
						mapError.put(
								"Row No#  " + row.getRowNum() + "Column No#  " + annualDiscountRate.getColumnIndex(),
								"annualDiscountRate must be Numeric");
					}

					XSSFCell leaseTermPerod = row.getCell((short) 9);

					if (leaseTermPerod.getCellType() == CellType.NUMERIC) {
						userData.setLeaseTerm((int) leaseTermPerod.getNumericCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + leaseTermPerod.getColumnIndex(),
								"leaseTermPeriod must be Numeric");
					}

					XSSFCell leasePayment = row.getCell((short) 10);

					if (leasePayment.getCellType() == CellType.NUMERIC) {
						userData.setLeasePayment(leasePayment.getNumericCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + leasePayment.getColumnIndex(),
								"LeasePayment must be Numeric");
					}

					XSSFCell paymentInterval = row.getCell((short) 11);

					if (paymentInterval.getCellType() == CellType.STRING) {
						userData.setPaymentIntervals(paymentInterval.getStringCellValue() + "");
					}
					/*
					 * else if (paymentInterval.getCellType() ==
					 * CellType.STRING) {
					 * userData.setPaymentIntervals(paymentInterval.
					 * getStringCellValue() + ""); }
					 */
					else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + paymentInterval.getColumnIndex(),
								"paymentInterval must be String");
					}

					XSSFCell initilDirectCost = row.getCell((short) 12);

					if (initilDirectCost.getCellType() == CellType.NUMERIC) {
						userData.setInitialDirectCost((int) initilDirectCost.getNumericCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + initilDirectCost.getColumnIndex(),
								"InitialDirectCost must be Numeric");
					}

					XSSFCell guarenteResidual = row.getCell((short) 13);

					if (guarenteResidual.getCellType() == CellType.NUMERIC) {
						userData.setGuaranteedResidualValue(guarenteResidual.getNumericCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + guarenteResidual.getColumnIndex(),
								"Residual Value must be Numeric");
					}

					XSSFCell useFullLifeAsset = row.getCell((short) 14);

					if (useFullLifeAsset.getCellType() == CellType.NUMERIC) {
						userData.setUsefulLifeOfTheAsset((int) useFullLifeAsset.getNumericCellValue());
					} else {
						mapError.put("Row No#  " + row.getRowNum() + "Column No#  " + useFullLifeAsset.getColumnIndex(),
								"UsefullAsset must be Numeric");
					}

					XSSFCell escalatonPercentage = row.getCell((short) 15);

					if (escalatonPercentage.getCellType() == CellType.NUMERIC) {
						userData.setEscalation((float) escalatonPercentage.getNumericCellValue());
					} else {
						mapError.put(
								"Row No#  " + row.getRowNum() + "Column No#  " + escalatonPercentage.getColumnIndex(),
								"Escalation% must be Numeric");
					}

					XSSFCell escalatonAfterYear = row.getCell((short) 16);

					if (escalatonAfterYear.getCellType() == CellType.NUMERIC) {
						userData.setEscalationAfterEvery((int) escalatonAfterYear.getNumericCellValue());
					} else {
						mapError.put(
								"Row No#  " + row.getRowNum() + "Column No#  " + escalatonAfterYear.getColumnIndex(),
								"EscalationYear must be Numeric");
					}
					userData.setId(getAvaiablaeDataId());
					userData.setUserId(userId);
					userData.setCompanyId(companyId);

					listUserData.add(userData);
				}

			}
			if (mapError.isEmpty())
				userDataRepository.saveAll(listUserData);
			else {
				returnResult = gson.toJson(mapError);
				return returnResult;
			}

			return returnResult;
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	public String deleteUserLeases(String userId) {
		int userIdInt = Integer.parseInt(userId);
		Query query = new Query();
		List<UserData> userDataList;
		query.addCriteria(Criteria.where("userId").is(userIdInt));
		userDataList = getUserData(userIdInt);
		if (!userDataList.isEmpty()) {
			for (UserData userData : userDataList) {
				mongoOperation.remove(userData);
			}

		}
		return "user,s Data Removed";

	}

}
// anaother code for distinct values get
/*
 * List<String> categoryList = new ArrayList<>(); MongoCollection
 * mongoCollection = mongoTemplate.getCollection("userData"); DistinctIterable
 * distinctIterable = mongoCollection.dis.distinct(columnName, String.class);
 * MongoCursor cursor = distinctIterable.iterator(); while (cursor.hasNext()) {
 * String category = (String) cursor.next(); categoryList.add(category); }
 */
