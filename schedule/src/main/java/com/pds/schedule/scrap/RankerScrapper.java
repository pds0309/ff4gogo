package com.pds.schedule.scrap;


import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class RankerScrapper {

    private RankerScrapper(){
        //
    }
    private static final String URL = "http://fifaonline4.nexon.com/datacenter/rank?n4pageno=";

    public static List<String> getRankerListFromWeb(int pageNum){
        List<String> rankerList = new ArrayList<>();
        for(int i = 0 ; i < pageNum; i ++){
            Elements elements = null;
            try {
                elements = Jsoup.connect(URL+i).get().select(".profile_pointer");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Element e : elements){
                rankerList.add(e.text());
            }
        }
        log.info(rankerList.size() + "명의 랭커 수집 완료");
        return rankerList;
    }
}
