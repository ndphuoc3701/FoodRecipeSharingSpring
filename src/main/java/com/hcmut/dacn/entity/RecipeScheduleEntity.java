package com.hcmut.dacn.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recipe_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    @Column(name = "schedule_time", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private Date scheduleTime;

    private String note;
}
