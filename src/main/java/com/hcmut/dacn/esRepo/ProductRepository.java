package com.hcmut.dacn.esRepo;
import com.hcmut.dacn.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface ProductRepository extends ElasticsearchRepository<Product,Long> {
}