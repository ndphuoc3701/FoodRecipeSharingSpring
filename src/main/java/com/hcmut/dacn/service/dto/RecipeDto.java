package com.hcmut.dacn.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeDto {
    private Long id;

    private String name;

    private String imageUrl;

    private String material;

    private String instruction;

    private Double numStar;

    private Integer numEvaluation;

    private Integer numFavorite;

    private UserDto owner;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date createAt;
}
