package com.texoit.filme.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texoit.filme.builders.ProducaoBuilder;
import com.texoit.filme.models.Producao;
import com.texoit.filme.repositories.ProducaoRepository;
import com.texoit.filme.services.contract.AbstractService;

@Service
public class ProducaoService extends AbstractService<Producao> {
    
    @Autowired
    protected ProducaoRepository repository;

    public ProducaoService(ProducaoRepository repository, ProducaoBuilder builder) {
        super(repository, builder);
    }
    
    public Set<Producao> findByProducao(String Producao) {
        return this.repository.findByProducao(Producao);
    }
}
