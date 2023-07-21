package com.hcmut.dacn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "Comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "num_like", nullable = false, columnDefinition = "int8 default 0")
    private Integer numLike=0;

    @Column(name = "num_dislike", nullable = false, columnDefinition = "int8 default 0")
    private Integer numDislike=0;

    @ManyToOne
    @JoinColumn(name = "evaluation_id",nullable = false)
    private EvaluationEntity evaluation;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @CreationTimestamp
    @Column(name = "create_at",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date createAt;
}
