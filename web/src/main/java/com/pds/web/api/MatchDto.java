package com.pds.web.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pds.common.dto.PlayerDto;
import lombok.*;

import java.util.List;

public class MatchDto {
    private MatchDto(){
        //not use
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BasicDto {
        private String nickname;
        private String matchResult; //매치 결과 (“승”, “무”, “패”)
        private int matchEndType;
        private int goalTotal;
        private String controller; //사용한 컨트롤러 타입 (keyboard / pad / etc 중 1)
        private String matchDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SummaryDto {

        private int possession;
        private int shootTotal;
        private int effectiveShootTotal;
        private int offsideCount;
        private int foul;
        private int yellowCards;
        private int redCards;
        private int shootPenaltyKick;
        private int ownGoal;
        private int shootFreekick;
        private int cornerKick;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PassDto{
        private int passTry;
        private int passSuccess;
        private int shortPassTry;
        private int shortPassSuccess;
        private int longPassTry;
        private int longPassSuccess;
        private int bouncingLobPassTry;
        private int bouncingLobPassSuccess;
        private int drivenGroundPassTry;
        private int drivenGroundPassSuccess;
        private int throughPassTry;
        private int throughPassSuccess;
        private int lobbedThroughPassTry;
        private int lobbedThroughPassSuccess;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchPlayerDto {
        private int spId;
        private int spPosition;
        private int spGrade;
        private int shoot;
        private int effectiveShoot;
        private int assist;
        private int goal;
        private int dribble;
        private int intercept;
        private int defending;
        private int passTry;
        private int passSuccess;
        private int dribbleTry;
        private int dribbleSuccess;
        private int ballPossesionTry;
        private int ballPossesionSuccess;
        private int aerialTry;
        private int aerialSuccess;
        private int blockTry;
        private int block;
        private int tackleTry;
        private int tackle;
        private int yellowCards;
        private int redCards;
        private double spRating;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true,value = {"spIdType","spLevel"})
    public static class ShootDto {
        private int goalTime;
        private double x;
        private double y;
        private int type; //슛 종류 (1 : normal , 2 : finesse , 3 : header)
        private int result;//슛 결과 (1 : ontarget , 2 : offtarget , 3 : goal)
        private int spId;
        private int spGrade;
        private boolean assist;
        private int assistSpId;
        private double assistX;
        private double assistY;
        private boolean hitPost;
        private boolean inPenalty; //페널티박스 안에서 넣은 슛 여부 (안 : true, 밖 : false)
        public void setX(double x) {
            this.x = x;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Info {
        private List<BasicDto> basicDtoList;
        private List<SummaryDto> summaryDtoList;
        private List<PassDto> passDtoList;
        private List<List<MatchPlayerDto>> matchPlayerDtoList;
        private List<List<ShootDto>> shootDtoList;

        public Info(List<BasicDto> basicDto, List<SummaryDto> summaryDto, List<PassDto> passDto,
                    List<List<MatchPlayerDto>> matchPlayerDtoList, List<List<ShootDto>> shootDtoList) {
            this.basicDtoList = basicDto;
            this.summaryDtoList = summaryDto;
            this.passDtoList = passDto;
            this.matchPlayerDtoList = matchPlayerDtoList;
            this.shootDtoList = shootDtoList;
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(builderMethodName = "BestBuilder")
    public static class BestDto{
        private int spId;
        private int goal;
        private double spRating;
        private double assist;
        private int cnt;
        public void setAll(MatchPlayerDto matchPlayerDto){
            this.spId = matchPlayerDto.spId;
            this.goal += matchPlayerDto.goal;
            this.spRating = (this.spRating*this.cnt+matchPlayerDto.spRating)/(this.cnt+1);
            this.assist += matchPlayerDto.assist;
            this.cnt += 1;
        }
        public static BestDtoBuilder builder(MatchPlayerDto matchPlayerDto){
            return BestBuilder()
                    .spId(matchPlayerDto.getSpId())
                    .goal(matchPlayerDto.getGoal())
                    .spRating(matchPlayerDto.getSpRating())
                    .assist(matchPlayerDto.getAssist())
                    .cnt(1);
        }
    }
}
