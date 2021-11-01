package com.pds.web.service;


import com.pds.common.config.GenModelMapper;
import com.pds.common.dto.StatDto;
import com.pds.common.entity.Stat;
import com.pds.common.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatRepository statRepository;
    private final GenModelMapper<StatDto.Info> modelMapper;

    public int getThisSeason(){
        return statRepository.findTopStatIdMatchSidByOrderByStatIdMatchSidDesc().getStatIdMatchSid();
    }

    public int getThisSeasonMatchNum(int curMatchSeason){
        return statRepository.findSeasonMatchNum(curMatchSeason);
    }

    public int getCnt75(int curMatchSeason){
        List<Integer> cntList = statRepository.findCnt(curMatchSeason);
        return cntList.get((int)(cntList.size()*0.9));
    }

    public List<StatDto.Info> getTopGoalPlayers(int curSeason , int cnt75){
        return statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByGoalDesc(cnt75,curSeason)
                .stream().map(stat -> modelMapper.entityToDto(stat,StatDto.Info.class))
                .collect(Collectors.toList());
    }

    public List<StatDto.Info> getTopRatingPlayers(int curSeason , int cnt75){
        return statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByStarDesc(cnt75,curSeason)
                .stream().map(stat -> modelMapper.entityToDto(stat,StatDto.Info.class))
                .collect(Collectors.toList());
    }
    public List<StatDto.Info> getTopCntPlayers(int curSeason , int cnt75){
        return statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByCntDesc(cnt75,curSeason)
                .stream().map(stat -> modelMapper.entityToDto(stat,StatDto.Info.class))
                .collect(Collectors.toList());
    }
    public List<StatDto.Info> getTopWinPlayers(int curSeason , int cnt75){
        return statRepository.findTop5ByCntAfterAndStatIdMatchSidIsOrderByWinDesc(cnt75,curSeason)
                .stream().map(stat -> modelMapper.entityToDto(stat,StatDto.Info.class))
                .collect(Collectors.toList());
    }

    public List<StatDto.Info> getBest11Players(int season , int cnt75){
        return statRepository.findTopBest11OneMatchSeason(season,cnt75)
                .stream().map(stat -> modelMapper.entityToDto(stat,StatDto.Info.class))
                .collect(Collectors.toList());
    }
}
