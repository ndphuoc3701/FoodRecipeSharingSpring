package com.hcmut.dacn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recipe")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private byte[] imageData;

    @Column(name = "num_star", nullable = false, columnDefinition = "float8 default 0")
    private Double numStar=(double)0;

    @Column(name = "num_evaluation", nullable = false, columnDefinition = "int8 default 0")
    private Integer numEvaluation=0;

    @Column(name = "num_favorite", nullable = false, columnDefinition = "int8 default 0")
    private Integer numFavorite=0;

    @ManyToOne
    @JoinColumn(name = "owner_id",nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "recipe",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    private List<InstructionEntity> instructions;

    @OneToMany(mappedBy = "recipe",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    private List<IngredientRecipeEntity> ingredientRecipes;

    @CreationTimestamp
    @Column(name = "created_date",nullable = false, columnDefinition = "date default current_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date createdDate;
}
