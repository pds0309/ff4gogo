package com.pds.common.entity;


import com.pds.common.dto.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "UsersBuilder")
public class Users {
    @Id
    @Column
    private String userName;

    @Column
    private String userId;

    @Column(name = "USER_LEVEL")
    private int level;

    @Column
    private int highRank;

    public static UsersBuilder builder(UsersDto.Info dto){
        return UsersBuilder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .level(dto.getLevel())
                .highRank(dto.getHighRank());
    }
}
