package com.texoit.filme.models;

import java.util.List;
import java.util.Set;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Filme {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    Integer ano;

    @NotNull
    String titulo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="filmes_producoes")
    private List<Producao> producoes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="filmes_estudios")
    private List<Estudio> estudios;

    @NotNull
    Boolean vencedor;
    
}
