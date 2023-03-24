package com.texoit.filme.dtos;

import com.texoit.filme.models.Producao;

import lombok.Data;

@Data
public class ProducaoAnoDTO {
    private Producao producao;
    private Integer ano;
}