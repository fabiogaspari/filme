package com.texoit.filme.dtos;

import lombok.Data;

@Data
public class IntervaloDTO {
    
    private String producer;

    private Integer interval;

    private String previousWin;

    private String followingWin;

}
