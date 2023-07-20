package com.hcmut.dacn.controller;


import com.hcmut.dacn.request.CommentRequest;
import com.hcmut.dacn.service.CommentService;
import com.hcmut.dacn.service.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentController {
    public final static String PATH="comments";
    @Autowired
    CommentService commentService;
    @GetMapping("query")
    public List<CommentDto> getByEvaluationId(@RequestParam("evaluationId") Long evaluationId){
        return commentService.getAllByEvaluationId(evaluationId);
    }
    @PostMapping
    public CommentDto create(CommentRequest commentRequest){
        return commentService.create(commentRequest);
    }
}
