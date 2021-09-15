package com.pds.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

public class UsersDto {

    private UsersDto(){
        //Not used
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info{
        private String userName;
        private String userId;
        private int level;
        private int highRank;
        public Info(UserApiResponse u , int rank){
            this.userName = u.getUserName();
            this.userId = u.getUserId();
            this.level = u.getLevel();
            this.highRank = rank;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UserApiResponse{
        private String userName;
        private String userId;
        private int level;
        public UserApiResponse(JSONObject jsonObject){
            this.userName = jsonObject.getString("nickname");
            this.userId = jsonObject.getString("accessId");
            this.level = jsonObject.getInt("level");
        }
    }
}
