package com.hcmut.dacn.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcmut.dacn.service.EvaluationLearntRecipeDto;
import lombok.Data;

import java.util.Date;

@Data
public class EvaluationRecipeDto {
    Long id;

    private String content;

    private Integer numLike;

    private Integer numDislike;

    private Double numStar;

    private Integer numComment;

    private UserDto user;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date createAt;
}
