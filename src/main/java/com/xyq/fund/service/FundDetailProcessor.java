/**  
 * @Title:  FoundDetail.java   
 * @Package com.xyq.fund.service   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月25日 下午2:20:25   
 * @version V1.0 
 */ 
package com.xyq.fund.service;

import java.util.List;

import com.xyq.fund.bean.Fund;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月25日
 */
public class FundDetailProcessor implements PageProcessor {
	private Fund fund;
	public FundDetailProcessor(){
		
	}
	
	public FundDetailProcessor(Fund fund){
		this.fund = fund;
	}

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
			List<Selectable> foundInfos =  page.getHtml().xpath("//div[@class='infoOfFund']/table/tbody/tr/td").nodes();
			String type = foundInfos.get(0).xpath("//a/text()").toString();
			String risk = foundInfos.get(0).xpath("//td/text()").toString();
			risk = risk.replace("基金类型：", "").replaceAll("\\|", "").replaceAll("\\s*", "");
//			String amount = foundInfos.get(1).xpath("//td/text()").toString().replace("：", "");
			String manager = foundInfos.get(2).xpath("//a/text()").toString();
			String startTime = foundInfos.get(3).xpath("//td/text()").toString().replace("：", "");
//			String company = foundInfos.get(4).xpath("//a/text()").toString();
//			System.out.println(type + "," + risk + "," + amount + "," + manager + "," + startTime + "," + comany);
			
			List<Selectable> increaseInfos =  page.getHtml().xpath("//li[@class='increaseAmount']/table[@class='ui-table-hover']/tbody/tr").nodes().get(1).xpath("//td").nodes();
			String oneWeek = increaseInfos.get(1).xpath("//div/text()").toString().replace("%", "");
			String oneMonth = increaseInfos.get(2).xpath("//div/text()").toString().replace("%", "");
			String threeMonth = increaseInfos.get(3).xpath("//div/text()").toString().replace("%", "");
			String sixMonth = increaseInfos.get(4).xpath("//div/text()").toString().replace("%", "");
			String oneYear = increaseInfos.get(6).xpath("//div/text()").toString().replace("%", "");
			String twoYear = increaseInfos.get(7).xpath("//div/text()").toString().replace("%", "");
			String threeYear = increaseInfos.get(8).xpath("//div/text()").toString().replace("%", "");
//			System.out.println(oneWeek + "," + oneMonth + "," + threeYear);
			
			fund.setType(type);
			fund.setRisk(risk);
//			fund.setAmount(amount);
			fund.setManager(manager);
			fund.setStartTime(startTime);
//			fund.setCompany(company);
			
			fund.setOneWeek(oneWeek);
			fund.setOneMonth(oneMonth);
			fund.setThreeMonth(threeMonth);
			fund.setSixMonth(sixMonth);
			fund.setOneYear(oneYear);
			fund.setTwoYear(twoYear);
			fund.setThreeYear(threeYear);
			System.out.println(fund.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new FundDetailProcessor()).addUrl("http://fund.eastmoney.com/000011.html").thread(1).run();
	}
}
