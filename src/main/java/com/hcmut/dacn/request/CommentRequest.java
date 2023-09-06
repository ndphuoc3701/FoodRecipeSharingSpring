package com.hcmut.dacn.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


//@Data
public class CommentRequest {
    private boolean favorite;
    public void setFavorite(boolean favorite){
        this.favorite=favorite;
    }
//    public boolean isFavorite(){
//        return favorite;
//    }
}
