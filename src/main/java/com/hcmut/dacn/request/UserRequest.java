package com.hcmut.dacn.request;

import lombok.Data;

@Data
public class UserRequest {
    private String fullName;
    private String username;
    private String password;
    private String image;
}
