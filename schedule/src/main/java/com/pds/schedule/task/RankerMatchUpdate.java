package com.pds.schedule.task;


import com.pds.common.config.GenModelMapper;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.StatDto;
import com.pds.common.entity.*;
import com.pds.common.repository.PlayerRepository;
import com.pds.common.repository.RankerMatchRepository;
import com.pds.schedule.api.RankerMatchFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class RankerMatchUpdate {

    private final RankerMatchFinder matchFinder;
    private final GenModelMapper modelMapper;

    private final PlayerRepository playerRepository;
    private final RankerMatchRepository matchRepository;



    public List<Stat> getStatList() {
        List<JSONObject> jsonObjectList = matchFinder.getDetailRankerMatchList(getUpdateRequiredMatchMap());
        return jsonToStat(jsonObjectList);
    }

    public List<Stat> jsonToStat(List<JSONObject> recentMatchList) {
        List<StatDto.StatBodyDto> bodyDtoList = new ArrayList<>();
        List<StatDto.StatIdDto> idDtoList = new ArrayList<>();
        List<Stat> statList = new ArrayList<>();
        int j = 0;
        for (JSONObject jsonObject : recentMatchList) {
            Optional<Player> player = playerRepository.findById(jsonObject.getInt("pId"));
            if (player.isPresent()) {
                idDtoList.add(
                        new StatDto.StatIdDto((PlayerDto.Info) modelMapper.entityToDto(player.get(), PlayerDto.Info.class),
                                jsonObject.getInt("statId")));
                bodyDtoList.add(StatDto.StatBodyDto.builder(jsonObject).build());
                statList.add(
                        new Stat((StatId) modelMapper.dtoToEntity(idDtoList.get(j), StatId.class),
                                bodyDtoList.get(j)));
                j++;
            }
        }
        return statList;
    }

    public Map<String, List<String>> getUpdateRequiredMatchMap() {
        Map<String, List<String>> rankerMap = matchFinder.getRankersMatchCodeMap(matchFinder.getRankerIdList());
        for (Map.Entry<String, List<String>> entry : rankerMap.entrySet()) {
            List<String> prevMatch = matchRepository.findAllMatchCodeByUserId(entry.getKey());
            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                String matchCode = entry.getValue().get(i);
                // api ???????????? ????????? ?????????????????? ???????????? ?????? ???????????? ????????? ??????????????? ??????
                if (prevMatch.contains(matchCode)) {
                    entry.getValue().remove(matchCode);
                    matchRepository.save(new RankerMatch(new RankerMatchId(matchCode, entry.getKey()), 1));
                }
                // api?????? ??????????????? ?????? ???????????? ??????????????? ?????? ??? ????????? ?????? ?????? ????????????
                // ?????? ???????????? ???????????? ???????????? ?????????????????? api??????????????? ?????? ?????? = ??? ?????? ????????? ????????? ?????? ????????? ????????????
                else {
                    matchRepository.deleteMatchesNotUsed(entry.getKey());
                    break;
                }
            }
        }
        for (Map.Entry<String, List<String>> entry : rankerMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                String matchCode = entry.getValue().get(i);
                matchRepository.save(new RankerMatch(new RankerMatchId(matchCode, entry.getKey()), 0));
            }
        }
        log.info("?????? ??????????????? ?????????");
        return rankerMap;
    }

    public void setRankerMatchZero() {
        matchRepository.updateAllZero();
    }

}
