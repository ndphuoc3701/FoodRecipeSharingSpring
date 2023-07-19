package com.hcmut.dacn.request;

import lombok.Data;


@Data
public class CommentRequest {
    private String content;
    private Long userId;
    private Long evaluationId;
}
