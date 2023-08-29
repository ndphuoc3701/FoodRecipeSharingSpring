package com.hcmut.dacn.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Schedule {
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date scheduleTime;
    private String image;
    private Long userId;
}