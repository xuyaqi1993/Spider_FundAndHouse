/**  
 * @Title:  Company.java   
 * @Package com.xyq.fund.bean   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月27日 下午1:55:03   
 * @version V1.0 
 */ 
package com.xyq.fund.bean;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月27日
 */
public class Company {
	private String id;
	private String name;
	private String url;
	private int fundClass;
	private String scale;
	private String fundNum;
	private String managerNum;
	public String getFundNum() {
		return fundNum;
	}
	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}
	public String getManagerNum() {
		return managerNum;
	}
	public void setManagerNum(String managerNum) {
		this.managerNum = managerNum;
	}
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getFundClass() {
		return fundClass;
	}
	public void setFundClass(int fundClass) {
		this.fundClass = fundClass;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	@Override
    public String toString() {
        return "FoundCompany{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", fundClass='" + fundClass + '\'' +
                ", scale='" + scale + '\'' +
                ", fundNum='" + fundNum + '\'' +
                ", managerNum='" + managerNum + '\'' +
                '}';
    }
}
