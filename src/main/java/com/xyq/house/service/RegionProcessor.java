/**  
 * @Title:  LianJiaSpider.java   
 * @Package com.xyq   
 * @Description:    TODO
 * @author: xuyaqi  
 * @date:   2019年9月24日 下午2:22:53   
 * @version V1.0 
 */ 
package com.xyq.house.service;

import java.util.LinkedList;
import java.util.List;

import com.xyq.house.bean.Region;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 *  @Description:
 *  @Author:xuyaqi
 *  @2019年9月24日
 */
public class RegionProcessor implements PageProcessor{
	
	private List<Region> regionList = new LinkedList<Region>();
	
	public static void main(String[] args) {
		Spider.create(new RegionProcessor()).addUrl("https://zz.lianjia.com/ershoufang/").thread(1).run();
	}
	
	public void startProcessor(String url){
		Spider.create(new RegionProcessor()).addUrl(url).thread(1).run();
	}
	
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
	
    
	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		try {
            System.out.println("=============process()================");
            // 部分二：定义如何抽取页面信息，并保存下来
            if (!page.getHtml().xpath("//div[@data-role='ershoufang']").match()){
                page.setSkip(true);
            }
            //开始提取页面信息
            System.out.println(page.getUrl().toString());
            
            List<Selectable> regions = page.getHtml().xpath("//div[@data-role='ershoufang']/div[1]/a").nodes();
            for (Selectable regionS : regions) {
                String regionName = regionS.xpath("a/text()").toString();
                String regionUrl = regionS.xpath("a/@href").toString();
                System.out.println("区域：" + regionName);
                System.out.println("  |——链接：" + regionUrl);
                
                Region region = new Region();
                region.setRegionName(regionName);
                region.setRegionUrl(regionUrl);
                regionList.add(region);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	public List<Region> getRegionList(){
		return regionList;
	}
}
