package com.texoit.filme.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.filme.models.Filme;

public interface FilmeRepository extends JpaRepository<Filme, Long> {
    Set<Filme> findByAnoAndTitulo(Integer ano, String titulo);
    Set<Filme> findByVencedor(boolean vencedor);
}
