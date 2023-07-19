package com.hcmut.dacn.request;

import lombok.Data;


@Data
public class EvaluationRequest {
    private Long recipeId;

    private Long userId;

    private Double numStart;

    private String content;
}
