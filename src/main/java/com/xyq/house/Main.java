/**  
 * @Title:  Main.java   
 * @Package com.xyq.lianjia   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月24日 下午6:35:53   
 * @version V1.0 
 */ 
package com.xyq.house;

import java.util.List;

import us.codecraft.webmagic.Spider;

import com.xyq.house.Dao.HouseDao;
import com.xyq.house.bean.House;
import com.xyq.house.bean.Region;
import com.xyq.house.service.HouseProcessor;
import com.xyq.house.service.RegionProcessor;
import com.xyq.utils.IDGenerator;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月24日
 */
public class Main {
	
	public static void main(String[] args) {
		String oldHouseHomeUrl = "https://zz.lianjia.com/";
		RegionProcessor regionProcessor = new RegionProcessor();
		Spider.create(regionProcessor).addUrl(oldHouseHomeUrl + "ershoufang/").thread(1).run();
		List<Region> regionList = regionProcessor.getRegionList();
		long taskId = IDGenerator.getTaskId();
		for (Region region : regionList) {
			String regioonHouoseUrl = oldHouseHomeUrl + region.getRegionUrl() + "pg1";
			System.out.println(regioonHouoseUrl);
			HouseProcessor houseProcessor = new HouseProcessor("郑州", region.getRegionName());
			Spider.create(houseProcessor).addUrl(regioonHouoseUrl).thread(3).run();
			List<House> houseList = houseProcessor.getHouseList();
			new HouseDao().inseertHoue(houseList, "郑州", region.getRegionName(), taskId);
		}
	}

}
