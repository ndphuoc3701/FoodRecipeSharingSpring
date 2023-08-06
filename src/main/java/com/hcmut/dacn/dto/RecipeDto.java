package com.hcmut.dacn.dto;

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

    private String image;

    private boolean favorite;

    private Double numStar;

    private Integer numEvaluation;

    private Integer numFavorite;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date scheduledDate;
}
