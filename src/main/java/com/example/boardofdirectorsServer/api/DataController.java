package com.example.boardofdirectorsServer.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
		int dataIdInt = Integer.parseInt(dataId);
		System.out.println(dataIdInt);
		UserData userDataObject = userData.getUserDataByDataId(dataIdInt);
		Gson gson = new Gson();
		return gson.toJson(userDataObject);

	}

	@RequestMapping(path = "/getUserFileByDataId", method = RequestMethod.GET)
	// @GetMapping("/getUserFileByDataId")
	public ResponseEntity<Resource> getUserFileByDataId(@RequestParam String dataId) throws Exception {
		int dataIdInt = Integer.parseInt(dataId);
		System.out.println(dataIdInt);

		// Gson gson = new Gson();
		// return gson.toJson(userDataObject);
		return userData.getUserFileByDataId(dataIdInt);

	}

	@GetMapping("/getClassOfAsset")
	public String getClassOfAsset() throws Exception {
		System.out.println("getting class of assets");
		String classOfAssets = userData.getClassOfAsset();
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
		int leaseIdInt = Integer.parseInt(leaseId);
		String msg = userData.deleteSelectedUserData(leaseIdInt);
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
		/*
		 * String fileDownloadUri1 =
		 * ServletUriComponentsBuilder.fromCurrentContextPath()
		 * .path("C:/Users/Joni/git/boardofdirectorsServer/src/uploads/")
		 * .path("leaseId" + id + file.getOriginalFilename()).toUriString();
		 * String fileDownloadUri =
		 * ServletUriComponentsBuilder.fromCurrentContextPath().path(
		 * UPLOADED_FOLDER) .path(path + "").toUriString() + ""; /// return
		 * fileDownloadUri; Gson gson = new Gson();
		 */
		// return gson.toJson(fileDownloadUri1);

		return "leaseId" + id + file.getOriginalFilename();
		// return ResponseEntity.ok(fileDownloadUri1);
	}

	/*
	 * @GetMapping(value = "/projects/file/download/{filename}/{projectId}")
	 * public void getResource(@PathVariable String filename, @PathVariable Long
	 * projectId,HttpServletResponse response) throws IOException {
	 * 
	 * String fileLocation=//a location that I set, removed logic to make this
	 * shorter
	 * 
	 * File downloadFile= new File(fileLocation);
	 * 
	 * byte[] isr = Files.readAllBytes(downloadFile.toPath());
	 * ByteArrayOutputStream out = new ByteArrayOutputStream(isr.length);
	 * out.write(isr, 0, isr.length);
	 * 
	 * response.setContentType("application/pdf"); // Use 'inline' for preview
	 * and 'attachement' for download in browser.
	 * response.addHeader("Content-Disposition", "inline; filename=" +
	 * fileName);
	 * 
	 * OutputStream os; try { os = httpServletResponse.getOutputStream();
	 * out.writeTo(os); os.flush(); os.close(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * HttpHeaders respHeaders = new HttpHeaders();
	 * respHeaders.setContentLength(isr.length); respHeaders.setContentType(new
	 * MediaType("text", "json"));
	 * respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0"
	 * ); respHeaders.set(HttpHeaders.CONTENT_DISPOSITION,
	 * "attachment; filename=" + fileName); return new
	 * ResponseEntity<byte[]>(isr, respHeaders, HttpStatus.OK); }
	 */

}
