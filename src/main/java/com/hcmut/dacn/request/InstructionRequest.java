package com.hcmut.dacn.request;

import lombok.Data;

import java.util.List;

@Data
public class InstructionRequest {
    private String content;

    private int stepOrder;

    private List<ImageInstructionRequest> imageInstructionRequests;
}
