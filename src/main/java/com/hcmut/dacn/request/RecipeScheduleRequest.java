package com.hcmut.dacn.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RecipeScheduleRequest {
    private Long userId;
    private Long recipeId;
    String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date scheduleTime;
}
