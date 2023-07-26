package com.hcmut.dacn.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstructionDto {
    private String content;
    private int stepOrder;
    private List<ImageDto> images;
}
