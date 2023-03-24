package com.texoit.filme.builders;

import org.springframework.stereotype.Component;

import com.texoit.filme.builders.contract.InterfaceBuilder;
import com.texoit.filme.models.Estudio;

@Component
public class EstudioBuilder implements InterfaceBuilder<Estudio> {

    public Estudio clone(Estudio toBeCloned, Estudio toClone) {
        toClone.setEstudio(toBeCloned.getEstudio());
        
        return toClone;
    } 
    
}
