package com.hcmut.dacn.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String image;
    private Double cookLevel;
    private Double reputation;
}
