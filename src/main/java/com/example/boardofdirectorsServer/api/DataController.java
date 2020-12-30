package com.example.boardofdirectorsServer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.model.ClassOfAsset;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@RequestMapping("/data")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataController {

	@Autowired
	DataHelper userData;

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

	@GetMapping("/getUserDataByDataId")
	public String getUserDataByDataId(@RequestParam String dataId) throws Exception {
		int dataIdInt = Integer.parseInt(dataId);
		System.out.println(dataIdInt);
		UserData userDataObject = userData.getUserDataByDataId(dataIdInt);
		Gson gson = new Gson();
		return gson.toJson(userDataObject);

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

}
