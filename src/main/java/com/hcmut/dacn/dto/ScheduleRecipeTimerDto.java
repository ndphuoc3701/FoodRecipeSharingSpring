package com.hcmut.dacn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ScheduleRecipeTimerDto {
    private String name;
    private String image;
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date scheduleTime;

}
