package com.pds.common.dto;


import com.pds.common.entity.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "SeasonDtoBuilder")
public class SeasonDto {
    private int id;
    private String name;
    private String info;
    private String img;

    public SeasonDto(JSONObject jsonObject){
        this.id = jsonObject.getInt("seasonId");
        this.name = jsonObject.getString("className").split("\\(")[0].trim();
        this.info = jsonObject.getString("className").split("\\(")[1].split("\\)")[0];
        this.img = jsonObject.getString("seasonImg");
    }
    public static SeasonDtoBuilder builder(Season season){
        return SeasonDtoBuilder().id(season.getId())
                .name(season.getName())
                .info(season.getInfo())
                .img(season.getImg());
    }
}
