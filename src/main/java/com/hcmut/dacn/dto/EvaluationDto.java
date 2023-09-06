package com.hcmut.dacn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EvaluationDto {
    private Long id;

    private String content;

    private Integer numLike;

    private Integer numDislike;

    private Integer numStar;

    private String note;

    private List<ImageDto> images;

    private UserDto user;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date createdDate;
}
