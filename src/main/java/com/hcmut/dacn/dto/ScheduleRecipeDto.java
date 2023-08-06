package com.hcmut.dacn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduleRecipeDto {
    private Long id;

    private RecipeDto recipe;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date scheduleTime;

    private String note;

    @Data
    public static class EvaluationLearntRecipeDto {
        private Long id;
        private String name;
        private String image;
    }
}
