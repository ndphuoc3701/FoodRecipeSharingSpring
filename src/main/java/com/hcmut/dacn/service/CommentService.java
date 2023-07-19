package com.hcmut.dacn.service;

import com.hcmut.dacn.entity.CommentEntity;
import com.hcmut.dacn.request.CommentRequest;
import com.hcmut.dacn.service.dao.CommentDao;
import com.hcmut.dacn.service.dao.EvaluationDao;
import com.hcmut.dacn.service.dao.UserDao;
import com.hcmut.dacn.service.dto.CommentDto;
import com.hcmut.dacn.service.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
//    @Autowired
    private CommentDao commentDao;

//    @Autowired
    private UserDao userDao;

//    @Autowired
    private EvaluationDao evaluationDao;

    @Autowired
    private CommentMapper commentMapper;


    public List<CommentDto> getAllByEvaluationId(Long evaluationId){
        return commentMapper.toDtos(commentDao.getAllByEvaluationId(evaluationId));
    }
    public CommentDto create(CommentRequest commentRequest){
        CommentEntity comment=new CommentEntity();
        comment.setContent(commentRequest.getContent());
        comment.setUser(userDao.getByUserId(commentRequest.getUserId()));
        comment.setEvaluation(evaluationDao.getByEvaluationId(commentRequest.getEvaluationId()));
        return commentMapper.toDto(commentDao.create(comment));
    }
}
