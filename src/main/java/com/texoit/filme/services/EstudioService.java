package com.texoit.filme.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texoit.filme.builders.EstudioBuilder;
import com.texoit.filme.models.Estudio;
import com.texoit.filme.repositories.EstudioRepository;
import com.texoit.filme.services.contract.AbstractService;

@Service
public class EstudioService extends AbstractService<Estudio> {
    
    @Autowired
    protected EstudioRepository repository;

    public EstudioService(EstudioRepository repository, EstudioBuilder builder) {
        super(repository, builder);
    }

    public Set<Estudio> findByEstudio(String estudio) {
        return this.repository.findByEstudio(estudio);
    }

}
