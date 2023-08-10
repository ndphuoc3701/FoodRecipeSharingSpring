package com.hcmut.dacn.esRepo;
import com.hcmut.dacn.entity.RecipeES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//@Repository
public interface ProductRepository extends ElasticsearchRepository<RecipeES,Long> {
}