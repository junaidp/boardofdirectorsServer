package com.example.boardofdirectorsServer.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.ReportFilterEntity;
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@Component
public class ReportHelper {

	@Autowired
	MongoOperations mongoOperation;
	Gson gson = new Gson();

	public String getFilteredReportData(ReportFilterEntity reportFilterEntity) {
		try {
			String userDataJson;
			System.out.println("Getting report for  : userId:" + reportFilterEntity.getUserId() + ", companyid:"
					+ reportFilterEntity.getCompanyId());

			Query query = new Query();
			Criteria critOr = new Criteria();
			critOr.orOperator(Criteria.where("classOfAsset").is(reportFilterEntity.getClassOfAsset()),
					Criteria.where("lessorName").is(reportFilterEntity.getLessorName()),
					Criteria.where("userId").is(reportFilterEntity.getUserId()),
					Criteria.where("companyId").is(reportFilterEntity.getCompanyId()),
					Criteria.where("classOfAsset").is(reportFilterEntity.getClassOfAsset()),
					Criteria.where("leaseName").is(reportFilterEntity.getLeaseName()));

			query.addCriteria(critOr);

			List<UserData> userdata = mongoOperation.find(query, UserData.class);
			System.out.println(userdata);
			if (userdata == null)
				return null;
			userDataJson = gson.toJson(userdata);
			return userDataJson;

		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

}
