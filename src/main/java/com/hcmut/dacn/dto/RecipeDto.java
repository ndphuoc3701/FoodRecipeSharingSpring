package com.hcmut.dacn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "recipe")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeDto {
    private Long id;

    private String name;

    private String unsignedName;

    private String image;

    private boolean favorite;

    private Double numStar;

    private Integer numEvaluation;

    private Integer numFavorite;

    private String ingredients;

    private String unsignedIngredients;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;
}
