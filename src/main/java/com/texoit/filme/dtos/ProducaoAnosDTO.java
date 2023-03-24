package com.texoit.filme.dtos;

import java.util.List;

import com.texoit.filme.models.Producao;

import lombok.Data;

@Data
public class ProducaoAnosDTO {
    private Producao producao;
    private List<Integer> anos;

    @Override
    public int hashCode() { 
        return producao.getProducao().hashCode(); 
    }

    @Override
    public boolean equals(Object obj) {
        return producao.getProducao().equals(((ProducaoAnosDTO)obj).getProducao().getProducao());
    }
}