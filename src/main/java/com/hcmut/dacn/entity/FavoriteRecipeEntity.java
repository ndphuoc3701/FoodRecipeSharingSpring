package com.hcmut.dacn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "favorite_recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_id",nullable = false)
    private RecipeEntity recipe;

    @CreationTimestamp
    @Column(name = "create_at",nullable = false, columnDefinition = "date default current_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private Date createdDate;
}
