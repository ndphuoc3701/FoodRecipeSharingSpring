package com.hcmut.dacn.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class Pagination<T> {
    List<T> objects;
    int totalPages;
}
