package com.hcmut.dacn.service.mapper;
import com.hcmut.dacn.entity.*;
import com.hcmut.dacn.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "evaluationId",source = "comment.evaluation.id")
    CommentDto toDto(CommentEntity comment);
    List<CommentDto> toDtos(List<CommentEntity> comments);
}
