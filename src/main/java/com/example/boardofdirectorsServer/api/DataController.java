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
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@RequestMapping("/data")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataController {

	@Autowired
	DataHelper userData;

	@PostMapping("/saveData")
	public String saveUser(@RequestBody UserData userEntity) {
		return userData.saveData(userEntity);
	}

	@GetMapping("/getData")
	public String getData(@RequestParam String userId) throws Exception {
		int userIdInt = Integer.parseInt(userId);
		System.out.println(userId);
		List<UserData> s = userData.getUserData(userIdInt);
		Gson gson = new Gson();
		return gson.toJson(s);

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

}
