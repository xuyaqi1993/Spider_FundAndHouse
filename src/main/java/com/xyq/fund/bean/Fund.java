/**  
 * @Title:  Found.java   
 * @Package com.xyq.fund.bean   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月25日 下午4:05:48   
 * @version V1.0 
 */ 
package com.xyq.fund.bean;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月25日
 */
public class Fund {
	private String id;
	private String name;
	private String code;
	private String currentPrice;
	private String fee;
	private String status;
	private String type;
	private String risk;
	private String amount;
	private String manager;
	private String startTime;
	private String company;
	private String oneDay; 
	private String oneWeek;
	private String oneMonth;
	private String threeMonth;
	private String sixMonth;
	private String oneYear;
	private String twoYear;
	private String threeYear;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getOneWeek() {
		return oneWeek;
	}
	public void setOneWeek(String oneWeek) {
		this.oneWeek = oneWeek;
	}
	public String getOneMonth() {
		return oneMonth;
	}
	public void setOneMonth(String oneMonth) {
		this.oneMonth = oneMonth;
	}
	public String getThreeMonth() {
		return threeMonth;
	}
	public void setThreeMonth(String threeMonth) {
		this.threeMonth = threeMonth;
	}
	public String getSixMonth() {
		return sixMonth;
	}
	public void setSixMonth(String sixMonth) {
		this.sixMonth = sixMonth;
	}
	public String getOneYear() {
		return oneYear;
	}
	public void setOneYear(String oneYear) {
		this.oneYear = oneYear;
	}
	public String getTwoYear() {
		return twoYear;
	}
	public void setTwoYear(String twoYear) {
		this.twoYear = twoYear;
	}
	public String getThreeYear() {
		return threeYear;
	}
	public void setThreeYear(String threeYear) {
		this.threeYear = threeYear;
	}
	@Override
    public String toString() {
        return "Found{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", fee='" + fee + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", manager=" + manager +
                '}';
    }
	public String getRisk() {
		return risk;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public String getOneDay() {
		return oneDay;
	}
	public void setOneDay(String oneDay) {
		this.oneDay = oneDay;
	}
}
