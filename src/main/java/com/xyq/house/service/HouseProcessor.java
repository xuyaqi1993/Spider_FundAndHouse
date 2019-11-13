package com.xyq.house.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import com.xyq.house.bean.House;
import com.xyq.utils.StringUtil;

public class HouseProcessor implements PageProcessor {
	private List<House> houseList = new ArrayList<House>();
	
    private String city;

    private String region;

    private int count;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HouseProcessor(){}

    public HouseProcessor(String city, String region){
        this.city = city;
        this.region = region;
        this.count = 1;
    }

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(500)
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     */
    @Override
    public void process(Page page) {
        try {
            System.out.println("=============process()================");
            //Thread.sleep(500);
            // 部分二：定义如何抽取页面信息，并保存下来
            if (!page.getHtml().xpath("//ul[@class='sellListContent']").match()) {
                page.setSkip(true);
            } else{
                int total = Integer.valueOf(page.getHtml().xpath("//div[@class='resultDes clear']/h2/span/text()").toString().trim());
                int totalPage = total/30 + 1;
                System.out.println("================总页数：" + totalPage + "当前页：" + count + "=================");
                
                if((count<totalPage) && (count<=100)){
                    count++;
                    //开始提取页面信息
//                    System.out.println(page.getUrl().toString());
//                    System.out.println(page.getHtml().toString());
//                    List<Selectable> targets = page.getHtml().xpath("//li[@class='clear LOGCLICKDATA']").nodes();
                    List<Selectable> targets = page.getHtml().xpath("//ul[@class='sellListContent']/li").nodes();
                    for (Selectable e : targets) {
                        try {
                            House house = new House();
                            String title = e.xpath("//div[@class='title']/a[1]/text()").toString();
                            String url = e.xpath("//a[@class='noresultRecommend img LOGCLICKDATA']/@href").toString();
                            String image = null;
                            if (e.xpath("//img[@class='lj-lazy']/@data-original").match()) {
                                image = e.xpath("//img[@class='lj-lazy']/@data-original").toString();
                            }
                            String s = e.xpath("//div[@class='houseInfo']/text()").toString();
                            String community = e.xpath("//div[@class='houseInfo']/a/text()").toString();
                            String floor = e.xpath("//div[@class='positionInfo']/text()").toString();
                            String street = e.xpath("//div[@class='positionInfo']/a[1]/text()").toString();
                            String totolPrice = e.xpath("//div[@class='totalPrice']/span[1]/text()").toString();
                            String averagePrice = StringUtils.strip(StringUtils.strip(e.xpath("//div[@class='unitPrice']/span[1]/text()").toString(), "单价"), "元/平米");
                            String followInfo = e.xpath("//div[@class='followInfo']/text()").toString();
                            String[] sl = followInfo.split("/");
                            String watch = StringUtil.collectStringNumber(sl[0]);
                            String view = StringUtil.collectStringNumber(sl[1]);
                            String releaseDate = sl[1];
                            String ss = StringUtils.strip(s.trim(), "|").trim();
                            String[] houseInfo = StringUtils.split(ss, "|");
                            String roomCount = houseInfo[0].trim();
                            Double houseArea = Double.valueOf(houseInfo[1].trim().split("平米")[0]);
                            String towards = houseInfo[2].trim();
                            String decoration = houseInfo[3].trim();
                            String elevator = null;
                            try{
                                elevator = houseInfo[4].trim();
                            }catch (ArrayIndexOutOfBoundsException ae){

                            }
                            house.setId(StringUtil.collectStringNumber(url));
                            house.setTitle(title);
                            house.setUrl(url);
                            house.setCommunity(community);
                            house.setStreet(street);
                            house.setRegion(region);
                            house.setCity(city);
                            house.setFloor(floor);
                            house.setTotalPrice(Double.valueOf(totolPrice));
                            house.setAveragePrice(Double.valueOf(averagePrice));
                            house.setImage(image);
                            house.setWatch(Integer.valueOf(watch));
                            house.setView(Integer.valueOf(view));
                            house.setReleaseDate(releaseDate);
                            house.setRoomCount(roomCount);
                            house.setHouseArea(houseArea);
                            house.setTowards(towards);
                            house.setDecoration(decoration);
                            house.setElevator(elevator);
                            System.out.println(house.toString());
                            houseList.add(house);
                        } catch (Exception ex) {
//                            logger.error("Function process() >> targets.forEach() Exception,details:",ex);
                        	
                        }
                    }

                    //page.putField("houses", houseList);
                    // 部分三：从页面发现后续的url地址来抓取
                    int index = page.getUrl().toString().indexOf("pg");
                    String newPage = page.getUrl().toString().substring(0, index) + "pg" + count + "/";
                    page.addTargetRequest(newPage);
                }else {
                    page.setSkip(true);
                }
            }
        } catch (Exception e){
            logger.error("Function process() Exception,details:",e);
        }
    }


    @Override
    public Site getSite() {
        return site;
    }
    
    public List<House> getHouseList() {
    	return houseList;
    }

    public static void main(String[] args){
        Spider.create(new HouseProcessor())
                .addUrl("https://zz.lianjia.com/ershoufang/erqi/pg1")
                .thread(2)
                .run();
    }

}
