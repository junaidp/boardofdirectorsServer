package com.example.boardofdirectorsServer.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.helper.Utility;
import com.example.boardofdirectorsServer.model.ClassOfAsset;
import com.example.boardofdirectorsServer.model.ContactUsEntity;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@RequestMapping("/data")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@MultipartConfig(maxFileSize = 1024 * 1024 * 1024, maxRequestSize = 1024 * 1024 * 1024)

public class DataController {

	@Autowired
	DataHelper userData;
	@Autowired
	Utility utility;

	@PostMapping("/saveData")
	public String saveData(@RequestBody UserData dataEntity) {
		return userData.saveData(dataEntity);
	}

	@GetMapping("/getData")
	public String getData(@RequestParam String userId, @RequestParam String companyId) throws Exception {
		int userIdInt = Integer.parseInt(userId);
		int companyIdInt = Integer.parseInt(companyId);
		List<UserData> data;
		User userDetails = userData.getUserWithId(userId);

		if (userDetails.getCompanyId() == 0) {
			data = userData.getUserData(userIdInt);
		} else {
			data = userData.getCompanyData(companyIdInt);
		}

		System.out.println(userId);
		// List<UserData> data = userData.getUserData(userIdInt);
		Gson gson = new Gson();
		return gson.toJson(data);

	}

	@GetMapping("/getDataForFilters")
	public String getDataForFilters(@RequestParam String userId, @RequestParam String companyId,
			@RequestParam String filterName) throws Exception {
		int userIdInt = Integer.parseInt(userId);
		int companyIdInt = Integer.parseInt(companyId);
		String data = null;
		User userDetails = userData.getUserWithId(userId);

		if (userDetails.getCompanyId() == 0) {
			data = userData.getUserDataUniqueRow(userIdInt, filterName);
		} else {
			// ss data = userData.getCompanyData(companyIdInt);
			data = userData.getCompanyDataUniqueRow(companyIdInt, filterName);
		}

		System.out.println(userId);
		// List<UserData> data = userData.getUserData(userIdInt);
		return data;

	}

	@PostMapping("/sendContactUsEmail")
	public String singIn(@RequestBody ContactUsEntity contactUsEntity) throws Exception {
		String emailSentStatus = null;
		System.out.println(contactUsEntity.getEmailFrom());

		String message = "Mail received from " + contactUsEntity.getName() + " <br></br> <br></br>" + "Email :"
				+ contactUsEntity.getEmailFrom() + " <br></br> <br></br>" + "Message:  " + contactUsEntity.getMessage();
		// will send email to main admin
		emailSentStatus = utility.sendEmail(message, "hamzariaz1994@gmail.com", "junaidp@gmail.com",
				"Contact Us Email (" + contactUsEntity.getSubject() + ")");
		// emailSentStatus = "Success: email sent successfully.You will shortly
		// receive responce";
		return emailSentStatus;
	}

	@GetMapping("/getUserDataByDataId")
	public String getUserDataByDataId(@RequestParam String dataId) throws Exception {
		// int dataIdInt = Integer.parseInt(dataId);
		// System.out.println(dataIdInt);
		UserData userDataObject = userData.getUserDataByDataId(dataId);
		Gson gson = new Gson();
		return gson.toJson(userDataObject);

	}

	@RequestMapping(path = "/getUserFileByDataId", method = RequestMethod.GET)
	// @GetMapping("/getUserFileByDataId")
	public ResponseEntity<Resource> getUserFileByDataId(@RequestParam String dataId) throws Exception {
		// int dataIdInt = Integer.parseInt(dataId);
		// System.out.println(dataIdInt);

		// Gson gson = new Gson();
		// return gson.toJson(userDataObject);
		return userData.getUserFileByDataId(dataId);

	}

	@PostMapping("/getClassOfAsset")
	public String getClassOfAsset(@RequestBody ClassOfAsset classAssetEntity) throws Exception {
		System.out.println("getting class of assets");
		String classOfAssets = userData.getClassOfAsset(classAssetEntity);
		Gson gson = new Gson();
		return classOfAssets;

	}

	@PostMapping("/saveClassOfAsset")
	public String saveClassOfAsset(@RequestBody ClassOfAsset classAssetEntity) {
		ClassOfAsset c = new ClassOfAsset();
		c.setClassOfAsset("class of Assets");

		return userData.saveData(classAssetEntity);
	}

	@GetMapping("/deleteAllData")
	public String deleteAllData() {
		return userData.deleteAllData();
	}

	@GetMapping("/deleteSelectedLease")
	public String deleteSelectedLease(@RequestParam String leaseId) throws Exception {
		// int leaseIdInt = Integer.parseInt(leaseId);
		String msg = userData.deleteSelectedUserData(leaseId);
		String data = null;

		return msg;

	}

	@PostMapping("/saveUploadedFiles")
	public String saveUploadedFiles(@RequestBody UserData dataEntity) throws Exception {
		// int leaseIdInt = Integer.parseInt(leaseId);
		// System.out.println(dataEntity.getFileToUpload());
		String msg = "as";
		String data = null;

		return msg;

	}

	@RequestMapping(value = "/addAttachment", method = RequestMethod.POST)
	@ResponseBody
	public String addFollowUpAttachment(@RequestParam("file") MultipartFile file, @RequestParam("id") String id)
			throws Exception {
		System.out.println("test");
		String UPLOADED_FOLDER = "C:/Users/Joni/git/boardofdirectorsServer/src/uploads/";

		Path path = Paths.get(UPLOADED_FOLDER + "leaseId" + id + file.getOriginalFilename());
		userData.saveFileToSystem(file, UPLOADED_FOLDER, id);

		return "leaseId" + id + file.getOriginalFilename();
		// return ResponseEntity.ok(fileDownloadUri1);
	}

	@RequestMapping(value = "/bulkUploadLease", method = RequestMethod.POST)
	// @ResponseBody
	@ExceptionHandler(Exception.class)
	public String bulkUploadLease(@RequestParam("file") MultipartFile file, @RequestParam("id") String id)
			throws Exception {
		Gson gson = new Gson();
		try {
			String returnResponse = userData.saveBulkLease(file, id);

			// return gson.toJson(returnResponse);
			return returnResponse;
		} catch (Exception x) {
			// throw x;
			return gson.toJson("failure   :" + x.getLocalizedMessage());

		}
		// return "something ";
	}

}
