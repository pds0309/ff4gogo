package com.pds.common.dto;


import com.pds.common.entity.UserCount;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder(builderMethodName = "UserCountDtoBuilder")
public class UserCountDto {
    private final String userName;
    private final int totalCount;
    private final int todayCount;
    public static UserCountDtoBuilder builder(UserCount userCount){
        return UserCountDtoBuilder()
                .userName(userCount.getUsers().getUserName())
                .todayCount(userCount.getTodayCount())
                .totalCount(userCount.getTotalCount());
    }
}
