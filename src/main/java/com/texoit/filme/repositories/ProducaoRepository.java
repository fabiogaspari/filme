package com.texoit.filme.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.filme.models.Producao;

public interface ProducaoRepository extends JpaRepository<Producao, Long>{
    Set<Producao> findByProducao(String producao);
}
