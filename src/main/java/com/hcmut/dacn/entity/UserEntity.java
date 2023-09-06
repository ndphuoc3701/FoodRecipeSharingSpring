package com.hcmut.dacn.entity;

import com.hcmut.dacn.repository.FavoriteRecipeRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private byte[] imageData;
    private String username;
    private String password;
    private Integer numLike=0;


    @Column(name = "cook_level", nullable = false, columnDefinition = "float8 default 0")
    private Double cookLevel=(double)0;

    @Column(name = "evaluation_level", nullable = false, columnDefinition = "float8 default 0")
    private Double evaluationLevel=(double)0;

    @OneToMany(mappedBy = "user")
    private List<FavoriteRecipeEntity> favoriteRecipes;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
