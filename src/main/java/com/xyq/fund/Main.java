/**  
 * @Title:  Main.java   
 * @Package com.xyq.lianjia   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月24日 下午6:35:53   
 * @version V1.0 
 */ 
package com.xyq.fund;

import java.util.List;

import us.codecraft.webmagic.Spider;

import com.xyq.fund.bean.Company;
import com.xyq.fund.bean.Fund;
import com.xyq.fund.dao.FundDao;
import com.xyq.fund.service.FundCompanyProcessor;
import com.xyq.fund.service.FundDetailProcessor;
import com.xyq.fund.service.FundListProcessor;
import com.xyq.utils.IDGenerator;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月24日
 */
public class Main {
	
	public static void main(String[] args) {
		String companyUrl = "http://fund.eastmoney.com/";
		long taskId = IDGenerator.getTaskId();
		
		List<Company> companyList = new FundDao().getCompanyList(taskId);
		if (companyList == null) {
			FundCompanyProcessor fundCompanyProcessor = new FundCompanyProcessor();
			Spider.create(fundCompanyProcessor).addUrl(companyUrl + "Company/").thread(1).run();
			companyList = fundCompanyProcessor.getComapanyList();
			new FundDao().insertFundCompany(companyList, taskId);
		}
		
		for (Company company : companyList) {
			FundListProcessor fundListProcessor = new FundListProcessor();
			Spider.create(fundListProcessor).addUrl(companyUrl + company.getUrl()).thread(2).run();
			List<Fund> fundList = fundListProcessor.getFundList();
			for (Fund fund : fundList) {
				String foundDetailUrl = companyUrl + fund.getId() + ".html";
//				System.out.println(foundDetailUrl);
				FundDetailProcessor foundDetailProcessor = new FundDetailProcessor(fund);
				Spider.create(foundDetailProcessor).addUrl(foundDetailUrl).thread(5).run();
			}
			new FundDao().insertFund(fundList, taskId, company.getName());
		}
		
	}

}
