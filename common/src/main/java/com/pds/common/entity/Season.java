package com.pds.common.entity;


import com.pds.common.dto.SeasonDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "SeasonBuilder")
public class Season {
    @Id
    @Column(name="S_ID")
    private int id;

    @Column(nullable = false , name = "S_NAME")
    private String name;
    @Column(nullable = false, name="S_INFO")
    private String info;
    @Column(nullable = false, name = "S_IMG_URL")
    private String img;

    public static SeasonBuilder builder(SeasonDto seasonDto){
        return SeasonBuilder()
                .id(seasonDto.getId())
                .name(seasonDto.getName())
                .info(seasonDto.getInfo())
                .img(seasonDto.getImg());
    }
}
