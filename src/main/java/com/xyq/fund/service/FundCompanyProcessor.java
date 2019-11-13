/**  
 * @Title:  FundCompanyProcessor.java   
 * @Package com.xyq.fund.service   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月27日 上午11:34:47   
 * @version V1.0 
 */ 
package com.xyq.fund.service;

import java.util.LinkedList;
import java.util.List;

import com.xyq.fund.bean.Company;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月27日
 */
public class FundCompanyProcessor implements PageProcessor {
	
	private List<Company> companyList = new LinkedList<Company>();
	
	private Site site = Site.me()
			.setRetryTimes(3)
			.setSleepTime(500)
			.addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
	
	@Override
	public void process(Page page) {
		try {
			System.out.println("=============process()================");
			List<Selectable> companyInfos =  page.getHtml().xpath("//div[@id='companyTable']/table/tbody/tr").nodes();
			for (Selectable company : companyInfos) {
				List<Selectable> info = company.xpath("//td").nodes();
				try {
					String name = info.get(1).xpath("//td/a/text()").toString();
					String url = info.get(1).xpath("//td/a/@href").toString();
					int fundClass = info.get(4).xpath("//td/div/label[@class='sprite sprite-star1']").nodes().size() + info.get(4).xpath("//td/div/label[@class='sprite sprite-star5']").nodes().size();
					String scale =  info.get(5).xpath("//td/@data-sortvalue").toString();
					String fundNum = info.get(6).xpath("//td/a/text()").toString();
					String managerNum = info.get(7).xpath("//td/a/text()").toString();
					String id = url.substring(9,url.indexOf("."));
					if (fundClass >= 4) {
						Company c = new Company();
						c.setId(id);
						c.setName(name);
						c.setUrl(url);
						c.setScale(scale);
						c.setFundClass(fundClass);
						c.setFundNum(fundNum);
						c.setManagerNum(managerNum);
						System.out.println(c.toString());
						companyList.add(c);
					}
				} catch (Exception ie) {
					ie.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Company> getComapanyList() {
		return companyList;
	}

	@Override
	public Site getSite() {
		return site;
	}

	
	public static void main(String[] args){
        Spider.create(new FundCompanyProcessor()).addUrl("http://fund.eastmoney.com/company/").thread(1).run();
    }
}
