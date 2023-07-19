package com.hcmut.dacn.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EvaluationDto {
    Long id;

    private String content;

    private Integer numLike;

    private Integer numDislike;

    private Double numStart;

    private Integer numComment;

    private Long recipeId;

    private UserDto user;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date createAt;
}
