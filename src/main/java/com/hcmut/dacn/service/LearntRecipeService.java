package com.hcmut.dacn.service;

import com.hcmut.dacn.repository.LearntRecipeRepository;
import com.hcmut.dacn.dto.LearntRecipeDto;
import com.hcmut.dacn.mapper.LearntRecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearntRecipeService {
    @Autowired
    private LearntRecipeRepository learntRecipeRepository;
    @Autowired
    private LearntRecipeMapper learntRecipeMapper;


    public List<LearntRecipeDto> getLearntRecipeByUserIdAndPage(Long userId, int page){
        return learntRecipeMapper.toDTOs(learntRecipeRepository.findLearntRecipesByUser_Id(userId, PageRequest.of(page-1,10)).getContent());

    }
}
