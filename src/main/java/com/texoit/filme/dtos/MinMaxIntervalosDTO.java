package com.texoit.filme.dtos;

import java.util.List;

import lombok.Data;

@Data
public class MinMaxIntervalosDTO {
    
    private List<IntervaloDTO> min;
    private List<IntervaloDTO> max;
}
