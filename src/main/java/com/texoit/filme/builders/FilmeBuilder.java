package com.texoit.filme.builders;

import org.springframework.stereotype.Component;

import com.texoit.filme.builders.contract.InterfaceBuilder;
import com.texoit.filme.models.Filme;
@Component
public class FilmeBuilder implements InterfaceBuilder<Filme> {

    public Filme clone(Filme toBeCloned, Filme toClone) {
        toClone.setTitulo(toBeCloned.getTitulo());
        toClone.setAno(toBeCloned.getAno());
        toClone.setEstudios(toBeCloned.getEstudios());
        toClone.setProducoes(toBeCloned.getProducoes());
        toClone.setVencedor(toBeCloned.getVencedor());
        
        return toClone;
    } 
}
