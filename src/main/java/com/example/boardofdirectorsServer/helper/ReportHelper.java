package com.example.boardofdirectorsServer.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.ReportFilterEntity;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@Component
public class ReportHelper {

	@Autowired
	MongoOperations mongoOperation;
	@Autowired
	DataHelper userData;
	Gson gson = new Gson();

	public String getFilteredReportData(ReportFilterEntity reportFilterEntity) {
		String userDataJson = null;
		User userDetails = userData.getUserWithId(reportFilterEntity.getUserId() + "");
		try {
			if (userDetails.getCompanyId() == 0) {
				userDataJson = getUserDataByUser(reportFilterEntity, userDataJson);
			} else {
				userDataJson = getUserDataByCompany(reportFilterEntity, userDataJson);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userDataJson;
	}

	private String getUserDataByUser(ReportFilterEntity reportFilterEntity, String userDataJson) throws Exception {
		try {

			System.out.println("Getting report for  : userId:" + reportFilterEntity.getUserId() + ", companyid:"
					+ reportFilterEntity.getCompanyId());

			Query query = new Query();
			Criteria critOr = new Criteria();
			// Criteria critAnd = new Criteria();
			// critAnd =
			// critAnd.and("userId").is(reportFilterEntity.getUserId());

			if ((reportFilterEntity.getLeaseName().equals("All")) && (reportFilterEntity.getLessorName().equals("All"))
					&& (reportFilterEntity.getClassOfAsset().equals("All"))) {

				query.addCriteria(Criteria.where("userId").is(reportFilterEntity.getUserId()));

			} else {
				// critOr.orOperator(Criteria.where("classOfAsset").is(reportFilterEntity.getClassOfAsset()),
				// Criteria.where("lessorName").is(reportFilterEntity.getLessorName()),
				// //
				// Criteria.where("location").is(reportFilterEntity.getLocation()),
				// //
				// Criteria.where("commencementDate").is(reportFilterEntity.getDate()),
				// //
				// Criteria.where("companyId").is(reportFilterEntity.getCompanyId()),
				// Criteria.where("leaseName").is(reportFilterEntity.getLeaseName()));
				// critOr.andOperator(Criteria.where("lessorName").is(reportFilterEntity.getLessorName()));
				// critOr.andOperator(Criteria.where("leaseName").is(reportFilterEntity.getLeaseName()));

				Criteria criteria = new Criteria();
				criteria = criteria.and("userId").is(reportFilterEntity.getUserId());
				if (!reportFilterEntity.getLeaseName().equals("All")) {
					criteria = criteria.and("leaseName").is(reportFilterEntity.getLeaseName());
				}
				if (!reportFilterEntity.getLessorName().equals("All")) {
					criteria = criteria.and("lessorName").is(reportFilterEntity.getLessorName());
				}
				if (!reportFilterEntity.getClassOfAsset().equals("All")) {
					criteria = criteria.and("classOfAsset").is(reportFilterEntity.getClassOfAsset());
				}
				// else {
				// criteria = criteria.and("expiryDateTime").lte(new Date());
				// }

				// critOr.andOperator(Criteria.where("userId").is(reportFilterEntity.getUserId()));
				query.addCriteria(criteria);

			}

			// critAnd.orOperator(critOr);
			// query.addCriteria(critAnd);

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

	private String getUserDataByCompany(ReportFilterEntity reportFilterEntity, String userDataJson) throws Exception {
		try {

			System.out.println("Getting report for  : companyId:" + reportFilterEntity.getCompanyId() + ", userId:"
					+ reportFilterEntity.getUserId());

			Query query = new Query();
			Criteria critOr = new Criteria();
			// Criteria critAnd = new Criteria();
			// critAnd =
			// critAnd.and("userId").is(reportFilterEntity.getUserId());

			if ((reportFilterEntity.getLeaseName().equals("All")) && (reportFilterEntity.getLessorName().equals("All"))
					&& (reportFilterEntity.getClassOfAsset().equals("All"))) {

				query.addCriteria(Criteria.where("companyId").is(reportFilterEntity.getCompanyId()));

			} else {
				Criteria criteria = new Criteria();
				criteria = criteria.and("companyId").is(reportFilterEntity.getCompanyId());
				if (!reportFilterEntity.getLeaseName().equals("All")) {
					criteria = criteria.and("leaseName").is(reportFilterEntity.getLeaseName());
				}
				if (!reportFilterEntity.getLessorName().equals("All")) {
					criteria = criteria.and("lessorName").is(reportFilterEntity.getLessorName());
				}
				if (!reportFilterEntity.getClassOfAsset().equals("All")) {
					criteria = criteria.and("classOfAsset").is(reportFilterEntity.getClassOfAsset());
				}

				query.addCriteria(criteria);
				// critOr.orOperator(Criteria.where("classOfAsset").is(reportFilterEntity.getClassOfAsset()),
				// Criteria.where("lessorName").is(reportFilterEntity.getLessorName()),
				// Criteria.where("location").is(reportFilterEntity.getLocation()),
				// Criteria.where("commencementDate").is(reportFilterEntity.getDate()),
				// //
				// Criteria.where("companyId").is(reportFilterEntity.getCompanyId()),
				// Criteria.where("leaseName").is(reportFilterEntity.getLeaseName()));
				// critOr.andOperator(Criteria.where("companyId").is(reportFilterEntity.getCompanyId()));
				// //
				// critOr.andOperator(Criteria.where("userId").is(reportFilterEntity.getUserId()));
				//
				// query.addCriteria(critOr);
			}
			// critAnd.orOperator(critOr);
			// query.addCriteria(critAnd);

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
