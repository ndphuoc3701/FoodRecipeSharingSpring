package com.hcmut.dacn.request;

import com.hcmut.dacn.dto.ImageDto;
import lombok.Data;

import java.util.List;


@Data
public class EvaluationRequest {
    private Long recipeId;

    private Long userId;

    private Double numStar;

    private String content;

    private String note;

    private List<ImageDto> images;
}
