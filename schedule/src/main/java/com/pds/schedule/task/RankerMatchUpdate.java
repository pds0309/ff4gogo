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
                // api 조회에서 오래된 매치코드부터 확인하여 이미 테이블에 저장된 매치였는지 확인
                if (prevMatch.contains(matchCode)) {
                    entry.getValue().remove(matchCode);
                    matchRepository.save(new RankerMatch(new RankerMatchId(matchCode, entry.getKey()), 1));
                }
                // api조회 매치코드를 이전 경기목록 테이블에서 찾을 수 없다면 최신 경기 데이터임
                // 이전 경기목록 테이블에 존재하는 매치코드이나 api조회에서는 없는 경우 = 더 이상 저장할 필요가 없는 과거의 매치코드
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
        log.info("랭커 매치리스트 최신화");
        return rankerMap;
    }

    public void setRankerMatchZero() {
        matchRepository.updateAllZero();
    }

}
