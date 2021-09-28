package com.pds.schedule.scrap;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class RankerScrapperTest {

    @Test
    void validRankerUrlTest() throws IOException {
        String url = "http://fifaonline4.nexon.com/datacenter/rank?n4pageno=1";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".profile_pointer");
        assertNotNull(elements);
    }
    @Test
    void getRankerListFromWebTest(){
        List<String> rankerList = RankerScrapper.getRankerListFromWeb(2);
        assertEquals(40,rankerList.size());
    }
}
