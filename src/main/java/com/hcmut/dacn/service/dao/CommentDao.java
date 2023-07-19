package com.hcmut.dacn.service.dao;

import com.hcmut.dacn.entity.CommentEntity;

import java.util.List;
public interface CommentDao {
    List<CommentEntity> getAllByEvaluationId(Long evaluationId);
    CommentEntity create(CommentEntity comment);
}
