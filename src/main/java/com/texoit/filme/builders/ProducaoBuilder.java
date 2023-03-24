package com.texoit.filme.builders;

import org.springframework.stereotype.Component;

import com.texoit.filme.builders.contract.InterfaceBuilder;
import com.texoit.filme.models.Producao;

@Component
public class ProducaoBuilder implements InterfaceBuilder<Producao> {

    public Producao clone(Producao toBeCloned, Producao toClone) {
        toClone.setProducao(toBeCloned.getProducao());
        
        return toClone;
    } 
}
