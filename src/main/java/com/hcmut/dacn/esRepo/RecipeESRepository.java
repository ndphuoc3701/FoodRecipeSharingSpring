package com.hcmut.dacn.esRepo;

import com.hcmut.dacn.dto.RecipeDto;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.hcmut.dacn.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
//public interface RecipeESRepository extends ElasticsearchRepository<RecipeDto,Long> {
//}

public interface RecipeESRepository extends JpaRepository<RecipeEntity,Long> {



}
