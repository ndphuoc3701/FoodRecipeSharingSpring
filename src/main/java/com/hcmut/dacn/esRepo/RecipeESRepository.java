package com.hcmut.dacn.esRepo;

import com.hcmut.dacn.dto.RecipeDto;
import com.hcmut.dacn.entity.RecipeES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeESRepository extends ElasticsearchRepository<RecipeDto,Long> {
//    @Query("{\"match\":{\"name\":{\"query\":\"?0\",\"fuzziness\":2,\"max_expansions\":10,\"prefix_length\":1}}}")
//    Page<RecipeDto> getRecipesByKeyword(String keyword, Pageable pageable);

//    @Query("{\"bool\":{\"should\":[{\"match\":{\"name\":{\"query\":\"?0\",\"fuzziness\":2,\"max_expansions\":10,\"prefix_length\":1}}},{\"match\":{\"ingredients\":{\"query\":\"?0\",\"fuzziness\":2,\"max_expansions\":10,\"prefix_length\":1}}}]}}")
//    Page<RecipeDto> getRecipesByKeyword(String keyword, Pageable pageable);

    @Query("?0")
    Page<RecipeDto> getRecipesByKeyword(@Param("query") String query, Pageable pageable);
//
//    @Query("{\"bool\":{\"must\":?0}}")
//    Page<RecipeDto> getRecipesByKeywordd(String query, Pageable pageable);


}
