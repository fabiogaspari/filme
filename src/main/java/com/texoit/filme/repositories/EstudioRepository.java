package com.texoit.filme.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.filme.models.Estudio;

public interface EstudioRepository extends JpaRepository<Estudio, Long> {
    Set<Estudio> findByEstudio(String estudio);
}
