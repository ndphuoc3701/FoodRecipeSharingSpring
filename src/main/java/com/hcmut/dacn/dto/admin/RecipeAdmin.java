package com.hcmut.dacn.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RecipeAdmin {
    private Long id;

    private String name;

    private Double numStar;

    private Integer numEvaluation;

    private Integer numFavorite;

    private Long ownerId;
}
