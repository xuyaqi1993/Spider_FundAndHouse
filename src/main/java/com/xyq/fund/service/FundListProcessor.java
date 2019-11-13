/**  
 * @Title:  fundList.java   
 * @Package com.xyq.fund.service   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月25日 下午2:20:13   
 * @version V1.0 
 */ 
package com.xyq.fund.service;

import java.util.LinkedList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import com.xyq.fund.bean.Fund;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月25日
 */
public class FundListProcessor implements PageProcessor {
	
	private List<Fund> fundList = new LinkedList<Fund>();
	
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
			String companyName = page.getHtml().xpath("//div[@class='ttjj-panel']/div[@class='ttjj-panel-title']/p/text()").toString();
			List<Selectable> fundInfoList =  page.getHtml().xpath("//div[@id='kfsFundNetWrap']/table/tbody/tr").nodes();
			for (Selectable s : fundInfoList) {
				try {
					List<Selectable> fundInfos = s.xpath("//td").nodes();
					String id = fundInfos.get(0).xpath("//a[@class='code']/text()").toString();
					String name = fundInfos.get(0).xpath("//a[@class='name']/text()").toString();
					String currentPrice = fundInfos.get(4).xpath("//td/text()").toString();
//					String type = fundInfos.get(3).xpath("//td/text()").toString();
					String fee = ""; 
					try {
						fee = fundInfos.get(12).xpath("//td/a/text()").toString().replace("%","");
					} catch (Exception e) {
						
					}
					String status = fundInfos.get(11).xpath("//td/text()").toString();
					String amount = fundInfos.get(9).xpath("//td/text()").toString().replace("%","");
					Fund fund = new Fund();
					fund.setId(id);
					fund.setName(name);
					fund.setCurrentPrice(currentPrice);
//					fund.setType(type);
					fund.setFee(fee);
					fund.setStatus(status);
					fund.setCompany(companyName);
					fund.setAmount(amount);
					fundList.add(fund);
				} catch (Exception e) {
					
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Site getSite() {
		return site;
	}
	public List<Fund> getFundList() {
		return fundList;
	}

	
	public static void main(String[] args){
        Spider.create(new FundListProcessor()).addUrl("http://fund.eastmoney.com/Company/80041198.html").thread(1).run();
    }
}
