package com.hcmut.dacn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "num_like", nullable = false, columnDefinition = "int8 default 0")
    private Integer numLike=0;

    @Column(name = "num_dislike", nullable = false, columnDefinition = "int8 default 0")
    private Integer numDislike=0;

    @Column(name = "num_star", nullable = false)
    private Double numStart;

    @Column(name = "num_comment", nullable = false, columnDefinition = "int8 default 0")
    private Integer numComment = 0;

    @ManyToOne
    @JoinColumn(name = "recipe_id",nullable = false)
    private RecipeEntity recipe;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @CreationTimestamp
    @Column(name = "create_at",nullable = false, columnDefinition = "date default current_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date createAt;
}
