package com.pds.schedule.task;


import com.pds.common.config.GenModelMapper;
import com.pds.common.config.ModelMapperConfig;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.entity.Stat;
import com.pds.common.repository.PlayerRepository;
import com.pds.common.repository.RankerMatchRepository;
import com.pds.schedule.TestMatchDetail;
import com.pds.schedule.api.RankerMatchFinder;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RankerMatchUpdate.class, ModelMapperConfig.class, GenModelMapper.class})
class RankerMatchUpdateTest {


    private final String userId = TestMatchDetail.userId;
    private final String matchCode = "6139d4a6b4c3eaad958aee1e";


    @Autowired
    private RankerMatchUpdate matchUpdate;

    @MockBean
    private RankerMatchFinder matchFinder;
    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private RankerMatchRepository matchRepository;

    @Test
    void getUpdateRequiredMatchMapTest() {
        Map<String, List<String>> request = new HashMap<>();
        List<String> matchList = new ArrayList<>();
        matchList.add("ABC");
        matchList.add(matchCode);
        request.put(userId, matchList);
        given(matchFinder.getRankersMatchCodeMap(matchFinder.getRankerIdList())).willReturn(request);

        given(matchRepository.findAllMatchCodeByUserId(userId)).willReturn(Collections.singletonList(matchCode));
        Map<String , List<String>> result = matchUpdate.getUpdateRequiredMatchMap();
        System.out.println(result);
        assertTrue(request.containsKey(userId));
        assertEquals(1,request.get(userId).size());
        assertEquals("ABC",request.get(userId).get(0));
    }

    @Test
    void jsonToStatTest(){
        String player1 = "{\"statId\":202109,\"goal\":0,\"star\":8,\"cnt\":1,\"assist\":0,\"pId\":250204923,\"win\":1,\"spPosition\":14}";
        String player2 = "{\"statId\":202109,\"goal\":0,\"star\":6,\"cnt\":1,\"assist\":0,\"pId\":300245279,\"win\":0,\"spPosition\":25}";
        String player3 = "{\"statId\":202107,\"goal\":0,\"star\":4,\"cnt\":1,\"assist\":0,\"pId\":1234,\"win\":0,\"spPosition\":13}";
        List<JSONObject> jsonList = new ArrayList<>();
        jsonList.add(new JSONObject(player1));
        jsonList.add(new JSONObject(player2));
        jsonList.add(new JSONObject(player3));
        given(playerRepository.findById(250204923))
                .willReturn(Optional.of(new Player(250204923,"선수1",new Season(250,"","",""))));
        given(playerRepository.findById(300245279))
                .willReturn(Optional.of(new Player(300245279,"선수2",new Season(300,"","",""))));
        given(playerRepository.findById(1234))
                .willReturn(Optional.empty());

        List<Stat> statList = matchUpdate.jsonToStat(jsonList);
        assertEquals(2,statList.size());
        assertEquals(250204923,statList.get(0).getStatId().getPlayer().getPlayerId());
        assertEquals(300,statList.get(1).getStatId().getPlayer().getSeason().getId());
        assertEquals(8,statList.get(0).getStar());
        assertEquals("CM",statList.get(0).getSpPosition());
    }
}
