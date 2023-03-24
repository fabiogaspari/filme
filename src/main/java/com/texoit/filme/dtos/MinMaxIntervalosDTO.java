package com.texoit.filme.dtos;

import java.util.Set;

import lombok.Data;

@Data
public class MinMaxIntervalosDTO {
    
    private Set<IntervaloDTO> min;
    private Set<IntervaloDTO> max;
}
