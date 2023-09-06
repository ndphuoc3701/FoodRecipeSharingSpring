package com.hcmut.dacn.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.UserDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EvaluationAdmin {
    private Long id;

    private String content;

    private Integer numLike;

    private Integer numDislike;

    private Integer numStar;

    private String note;

    private Long userId;

    private Long recipeId;
}