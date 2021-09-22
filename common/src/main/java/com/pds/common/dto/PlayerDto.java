package com.pds.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

public class PlayerDto {

    private PlayerDto(){
        //Not used
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info{
        private int playerId;
        private String playerName;
        private SeasonDto season;
    }
    @Getter
    public static class PlayerApiResponse{
        private final int playerId;
        private final String playerName;
        public PlayerApiResponse(JSONObject jsonObject){
            this.playerId = jsonObject.getInt("id");
            this.playerName = jsonObject.getString("name");
        }
    }
    @Getter
    public static class InfoWithImage{
        private final Info info;
        private final String pImg;
        public InfoWithImage(Info info , String pImg){
            this.info = info;
            this.pImg = pImg;
        }
    }
}
