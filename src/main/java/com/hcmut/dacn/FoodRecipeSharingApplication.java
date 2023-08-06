package com.hcmut.dacn;

import com.hcmut.dacn.esRepo.RecipeESRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories("com.hcmut.dacn.esRepo")
@EnableJpaRepositories("com.hcmut.dacn.repository")
public class FoodRecipeSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodRecipeSharingApplication.class, args);
	}

}
