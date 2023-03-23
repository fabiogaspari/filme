package com.texoit.filme.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.texoit.filme.models.Filme;

public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByAnoAndTituloAndEstudioAndProdutor(
        Integer ano, 
        String titulo, 
        String estudio, 
        String produtor
    );
}
