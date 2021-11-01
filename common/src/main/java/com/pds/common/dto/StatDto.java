package com.pds.common.dto;


import com.pds.common.enums.Positions;
import lombok.*;
import org.json.JSONObject;

public class StatDto {
    private StatDto(){
        //Not used
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private StatIdDto statId;
        private double goal;
        private double assist;
        private double star;
        private int cnt;
        private double win;
        private String spPosition;
        private String mostPos;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatIdDto{
        private PlayerDto.Info player;
        private int matchSid;
    }
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "StatBodyDtoJsonBuilder")
    public static class StatBodyDto{
        private double goal;
        private double assist;
        private double star;
        private int cnt;
        private double win;
        private String spPosition;
        public static StatBodyDtoBuilder builder(JSONObject jsonObject){
            return StatBodyDtoJsonBuilder()
                    .goal(jsonObject.getDouble("goal"))
                    .assist(jsonObject.getDouble("assist"))
                    .star(jsonObject.getDouble("star"))
                    .cnt(jsonObject.getInt("cnt"))
                    .win(jsonObject.getDouble("win"))
                    .spPosition(Positions.getPos(jsonObject.getInt("spPosition")).getPosInfo());
        }
    }
}
