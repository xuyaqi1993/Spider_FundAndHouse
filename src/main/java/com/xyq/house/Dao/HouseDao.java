/**  
 * @Title:  HouseDao.java   
 * @Package com.xyq.lianjia.Dao   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月25日 上午9:13:26   
 * @version V1.0 
 */ 
package com.xyq.house.Dao;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import com.xyq.house.bean.House;
import com.xyq.utils.DBUtil;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月25日
 */
public class HouseDao {
	
	public void inseertHoue(List<House> houseList,String city, String region, long taskId) {
		String dSql = "delete from t_old_house where taskid=? and city=? and region=?";
		String iSql = "insert into t_old_house(id,title,url,city,region,street,community,floor,totalPrice,averagePrice,image,"
				+ "releaseDate,roomCount,towards,houseArea,decoration,taskid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		List<Object[]> paraList = new LinkedList<Object[]>();
		for (House house : houseList) {
			paraList.add(new Object[]{house.getId(),house.getTitle(),house.getUrl(),house.getCity(),house.getRegion(),house.getStreet(),
					house.getCommunity(),house.getFloor(),house.getTotalPrice(),house.getAveragePrice(),house.getImage(),
					house.getReleaseDate(),house.getRoomCount(),house.getTowards(),house.getHouseArea(),house.getDecoration(),
					taskId});
		}
		Connection connection = null;
		try {
			connection = DBUtil.getConnection("local");
			connection.setAutoCommit(false);
			DBUtil.update(connection, dSql, new Object[]{taskId, city, region});
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

}
