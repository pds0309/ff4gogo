package com.pds.schedule.task;


import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import com.pds.common.entity.Player;
import com.pds.common.entity.Season;
import com.pds.common.entity.UpdatedPlayer;
import com.pds.common.repository.PlayerRepository;
import com.pds.common.repository.SeasonRepository;
import com.pds.common.repository.UpdatedPlayerRepository;
import com.pds.schedule.api.PlayerFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class PlayerUpdate {

    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;
    private final UpdatedPlayerRepository updatedPlayerRepository;

    private final PlayerFinder playerFinder;

    public long countSeason(){
        return seasonRepository.count();
    }
    public long countPlayer(){
        return playerRepository.count();
    }

    void updateSeason() {
        List<SeasonDto> newSeasonDto = playerFinder.getRecentSeason();
        if(newSeasonDto.size() <= countSeason()){
            return;
        }
        seasonRepository.saveAll(newSeasonDto
                .stream()
                .map(s -> Season.builder(s).build())
                .collect(Collectors.toList()));
        log.info("새로운 시즌 추가됨");
    }


    void updatePlayers(List<Player> newPlayerList) {
        if(!newPlayerList.isEmpty()) {
            playerRepository.saveAll(newPlayerList);
            updatedPlayerRepository.saveAll(newPlayerList.stream()
                    .map(n -> UpdatedPlayer.builder(n).build())
                    .collect(Collectors.toList()));
        }
    }

    public List<Player> getNewPlayerList() {
        List<PlayerDto.PlayerApiResponse> recentPlayerDto = playerFinder.getRecentPlayerDto();
        if(recentPlayerDto.size() <= countPlayer()){
            return new ArrayList<>();
        }
        List<Player> newPlayerList = new ArrayList<>();
        for (PlayerDto.PlayerApiResponse playerApiResponse : recentPlayerDto) {
            if (!playerRepository.findById(playerApiResponse.getPlayerId()).isPresent()) {
                newPlayerList
                        .add(Player.builder(playerApiResponse,
                                seasonRepository.findById(playerApiResponse.getPlayerId() / 1000000).orElse(null)).build());
            }
        }
        return newPlayerList;
    }
}
