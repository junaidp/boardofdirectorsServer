package com.example.boardofdirectorsServer.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
				return " Failure: Lease limit exceed.Please change the payment schedule to Add more Leases.";
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

	public String deleteSelectedUserData(int dataId) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(dataId));

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

	public UserData getUserDataByDataId(Integer dataId) {
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

	public ResponseEntity<Resource> getUserFileByDataId(Integer dataId) throws Exception {
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
			String fileName = userdata.getFileName();
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
		int goldAllowed = 100;

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

	public String getClassOfAsset() {
		System.out.println("in get all users");
		String jsonAssets = null;
		try {
			List<ClassOfAsset> classOfAssets = classOfAssetRepository.findAll();
			jsonAssets = gson.toJson(classOfAssets);
		} catch (Exception ex) {
			System.out.println(ex);
		}
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

	public int getAvaiablaeDataId() {
		Long total = userDataRepository.count();
		int count = total.intValue();
		return count + 1;

	}

	public String saveFileToSystem(MultipartFile file, String UPLOADED_FOLDER, String id) {
		// TODO Auto-generated method stub
		if (file.isEmpty()) {

			return "failure: please select a file";
		}

		try {
			Integer dataId = Integer.parseInt(id);
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + "leaseId" + id + file.getOriginalFilename());
			Files.write(path, bytes);

			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(dataId));

			UserData userdata = mongoOperation.findOne(query, UserData.class);
			userdata.setFileName("leaseId" + id + file.getOriginalFilename());
			userDataRepository.save(userdata);
			return ("success: You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
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
