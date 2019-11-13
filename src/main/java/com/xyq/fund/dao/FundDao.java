/**  
 * @Title:  FundDao.java   
 * @Package com.xyq.fund.dao   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月25日 下午5:32:37   
 * @version V1.0 
 */ 
package com.xyq.fund.dao;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xyq.fund.bean.Company;
import com.xyq.fund.bean.Fund;
import com.xyq.utils.DBUtil;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月25日
 */
public class FundDao {
	public void insertFund(List<Fund> fundList, long taskId, String companyName) {
		String dSql = "delete from t_fund where taskid=? and company=?";
		String iSql = "insert into t_fund(id,name,code,currentprice,fee,status,type,risk,amount,manager,company,starttime,"
				+ "oneday,oneweek,onemonth,threemonth,sixmonth,oneyear,twoyear,threeyear,taskid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> paraList = new LinkedList<Object[]>();
		for (Fund fund : fundList) {
			paraList.add(new Object[]{fund.getId(),fund.getName(),fund.getCode(),fund.getCurrentPrice(),fund.getFee(),fund.getStatus(),
					fund.getType(),fund.getRisk(),fund.getAmount(),fund.getManager(),fund.getCompany(),fund.getStartTime(),fund.getOneDay(),fund.getOneWeek(),
					fund.getOneMonth(),fund.getThreeMonth(),fund.getSixMonth(),fund.getOneYear(),fund.getTwoYear(),fund.getThreeYear(),taskId});
		}
		Connection connection = null;
		try {
			connection = DBUtil.getConnection("local");
			connection.setAutoCommit(false);
			DBUtil.update(connection, dSql, new Object[]{taskId, companyName});
			DBUtil.batch(connection, iSql, paraList);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			DBUtil.close(connection);
		}
	}
		public void insertFundCompany(List<Company> CompanyList, long taskId) {
			String dSql = "delete from t_fund_company";
			String iSql = "insert into t_fund_company(id,name,url,class,scale,fundnum,managernum,taskid) values(?,?,?,?,?,?,?,?)";
			List<Object[]> paraList = new LinkedList<Object[]>();
			for (Company c : CompanyList) {
				paraList.add(new Object[]{c.getId(),c.getName(),c.getUrl(),c.getFundClass(),c.getScale(),c.getFundNum(),c.getManagerNum(),taskId});
			}
			Connection connection = null;
			try {
				connection = DBUtil.getConnection("local");
				connection.setAutoCommit(false);
				DBUtil.update(connection, dSql, null);
				DBUtil.batch(connection, iSql, paraList);
				connection.commit();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					connection.rollback();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} finally {
				DBUtil.close(connection);
			}
	}
	@SuppressWarnings("unchecked")
	public List<Company> getCompanyList(Long taskId) {
		List<Company> companyList = new LinkedList<Company>();
		String cSql = "select name,url from t_fund_company";
		String fSql = "select count(distinct company) as company_count from t_fund where taskid=?";
		Connection connection = null;
		try {
			connection = DBUtil.getConnection("local");
			List<?> list = DBUtil.query4HashMapList(connection, fSql, new Object[]{taskId});
			Map<String, String> count = (Map<String, String>) list.get(0);
			int companyCount = Integer.parseInt(count.get("company_count"));
			if (companyCount == 0) {
				return null;
			}
			List<?> list2 = DBUtil.query4HashMapList(connection, cSql, null);
			for (int i = companyCount - 1; i < list2.size(); i ++) {
				Map<String, String> map = (Map<String, String>) list2.get(i);
				String name = map.get("name");
				String url = map.get("url");
				Company company = new Company();
				company.setName(name);
				company.setUrl(url);
				companyList.add(company);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(connection);
		}
		return companyList;
	}
}
