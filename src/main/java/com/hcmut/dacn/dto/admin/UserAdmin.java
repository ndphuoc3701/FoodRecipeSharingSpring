package com.hcmut.dacn.dto.admin;

import lombok.Data;

@Data
public class UserAdmin {
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private Double cookLevel;
    private Double evaluationLevel;
}
