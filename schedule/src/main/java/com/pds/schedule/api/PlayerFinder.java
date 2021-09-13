package com.pds.schedule.api;


import com.pds.common.api.Fifa4PlayerApi;
import com.pds.common.dto.PlayerDto;
import com.pds.common.dto.SeasonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerFinder {
    private final Fifa4PlayerApi playerApi;

    public List<PlayerDto.PlayerApiResponse> getRecentPlayerDto() {
        return playerApi.fromJSONtoPlayer(playerApi.getPlayerMetaData());
    }

    public List<SeasonDto> getRecentSeason(){
        return playerApi.fromJSONtoSeason(playerApi.getSeasonMetaData());
    }
}
