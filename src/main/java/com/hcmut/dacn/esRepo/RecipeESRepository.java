package com.hcmut.dacn.esRepo;

import com.hcmut.dacn.entity.RecipeES;
import com.hcmut.dacn.entity.RecipeEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeESRepository extends ElasticsearchRepository<RecipeES,Long> {

}
