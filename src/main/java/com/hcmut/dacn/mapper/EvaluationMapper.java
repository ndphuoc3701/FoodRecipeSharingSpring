package com.hcmut.dacn.mapper;

import com.hcmut.dacn.dto.EvaluationDto;
import com.hcmut.dacn.dto.ImageDto;
import com.hcmut.dacn.dto.admin.EvaluationAdmin;
import com.hcmut.dacn.entity.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",imports = StandardCharsets.class)
public abstract class EvaluationMapper {
    @Mapping(target = "user.image",expression = "java(new String(userEntity.getImageData(),StandardCharsets.UTF_8))")
    @Mapping(target = "images",expression = "java(toImageDtos(evaluation.getImages()))")
    public abstract EvaluationDto toDto(EvaluationEntity evaluation);
    public abstract List<EvaluationDto> toDtos(List<EvaluationEntity> evaluations);
    List<ImageDto> toImageDtos(List<ImageEntity> imageEntities){
        return imageEntities.stream().map(imageEntity -> {
            ImageDto imageDto= new ImageDto();
            imageDto.setData(new String(imageEntity.getData(),StandardCharsets.UTF_8));
            return imageDto;
        }).collect(Collectors.toList());
    }

    @Mapping(target = "userId", source = "evaluation.user.id")
    @Mapping(target = "recipeId", source = "evaluation.recipe.id")
    public abstract EvaluationAdmin toAdminDto(EvaluationEntity evaluation);
    public abstract List<EvaluationAdmin> toAdminDtos(List<EvaluationEntity> evaluations);

}
