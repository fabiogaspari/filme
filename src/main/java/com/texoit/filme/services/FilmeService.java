package com.texoit.filme.services;

import org.springframework.stereotype.Service;

import com.texoit.filme.builders.FilmeBuilder;
import com.texoit.filme.dtos.IntervaloDTO;
import com.texoit.filme.models.Filme;
import com.texoit.filme.repositories.FilmeRepository;
import com.texoit.filme.services.contract.AbstractService;

@Service
public class FilmeService extends AbstractService<Filme> {
    
    public FilmeService(FilmeRepository repository, FilmeBuilder builder) {
        super(repository, builder);
    }

    public IntervaloDTO maiorIntervalo() {
        IntervaloDTO intervalo = new IntervaloDTO();
        return intervalo;
    }
}
