package com.texoit.filme.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.texoit.filme.models.Estudio;
import com.texoit.filme.models.Filme;
import com.texoit.filme.models.Producao;
import com.texoit.filme.services.EstudioService;
import com.texoit.filme.services.FilmeService;
import com.texoit.filme.services.ProducaoService;

public class FilmeHelper {
    private final String DELIMITADOR_PONTO_E_VIRGULA = ";";
    private final String DELIMITADOR_VIRGURA = ",";
    private final String DELIMITADOR_AND = "and ";
    
    /** 
     * @param filmeService
     * @throws NumberFormatException
     * @throws IOException
     */
    public void popularBase(FilmeService filmeService, EstudioService estudioService, ProducaoService ProducaoService) throws NumberFormatException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("movielist.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String linha;
        int primeiraLinha = 0;
        while ( (linha = br.readLine()) != null ) {
            if ( primeiraLinha == 0 ) {
                primeiraLinha++;
                continue;
            } 

            List<String> values =  Arrays.asList(linha.split(DELIMITADOR_PONTO_E_VIRGULA));

            Set<Filme> filmes = filmeService.findByAnoAndTitulo(Integer.parseInt(values.get(0)), values.get(1));

            boolean existe = this.compararProducaoEEstudio(filmes);
            if ( existe ) continue;

            List<Estudio> estudios = new ArrayList<Estudio>();

            boolean vencedor =  values.get((values.size()-1)).trim().equals("yes");

            int finalProducoes = values.size()-2; 
            if ( vencedor ) {
                finalProducoes = values.size()-3;
            }

            for( int i = 2; i <= finalProducoes; i++ ) {
                String stringEstudio = values.get(i).trim();
                List<String> splittedEstudios = Arrays.asList(stringEstudio.split(DELIMITADOR_VIRGURA));
                splittedEstudios.forEach(sE -> {
                    Estudio estudio = new Estudio();
                    estudio.setEstudio(sE.trim());
                    Set<Estudio> existeEstudio = estudioService.findByEstudio(sE.trim());
                    if ( existeEstudio.size() == 0 ) {
                        estudioService.create(estudio);
                    } else {
                        estudio = existeEstudio.iterator().next();
                    }
                    estudios.add(estudio);
                });
            }

            String stringProducoes = "";

            if ( vencedor ) {
                stringProducoes = values.get((values.size()-2));
            } else {
                stringProducoes = values.get((values.size()-1));
            }

            List<String> listaProducoes = Arrays.asList(stringProducoes.split(DELIMITADOR_AND));
            List<Producao> Producoes = new ArrayList<Producao>();
            
            listaProducoes.forEach(p -> {
                List<String> listaProducoes2 = Arrays.asList(p.split(DELIMITADOR_VIRGURA));
                listaProducoes2.forEach(p2 -> {
                    if ( !p2.isEmpty() ) {
                        Producao Producao = new Producao();
                        Producao.setProducao(p2.trim());
                        Set<Producao> existeProducao = ProducaoService.findByProducao(p2.trim());
                        if ( existeProducao.size() == 0 ) {
                            ProducaoService.create(Producao);
                        } else {
                            Producao = existeProducao.iterator().next();
                        }
                        Producoes.add(Producao);
                    }
                });
            });

            Filme filme = new Filme();
            filme.setAno(Integer.parseInt(values.get(0)));
            filme.setTitulo(values.get(1));
            filme.setEstudios(estudios);
            filme.setProducoes(Producoes);
            filme.setVencedor(vencedor);
            filmeService.create(filme);
        }

        br.close();
    }

    /** 
     * @param filmes
     * @return boolean
     */
    public boolean compararProducaoEEstudio(Set<Filme> filmes) {
        Comparator<Producao> ordenarProducoes = new Comparator<Producao>() {
            @Override
            public int compare(Producao o1, Producao o2) {
                return o1.getProducao().compareTo(o2.getProducao());
            }
        };
        
        boolean existe = false;

        if ( !filmes.isEmpty() ) {
            existe = filmes.stream().filter(new Predicate<Filme>() {
                @Override
                public boolean test(Filme t) {
                    return filmes.stream()
                        .filter(f -> f.getProducoes().stream()
                            .sorted(ordenarProducoes)
                            .equals(t.getProducoes().stream()
                                .sorted(ordenarProducoes)))
                        .count() > 0;
                } 
            }).count() > 0;
        }
        
        Comparator<Estudio> ordenarEstudios = new Comparator<Estudio>() {
            @Override
            public int compare(Estudio o1, Estudio o2) {
                return o1.getEstudio().compareTo(o2.getEstudio());
            }
        };

        if ( existe ) {
            existe = filmes.stream().filter(new Predicate<Filme>() {
                @Override
                public boolean test(Filme t) {
                    return filmes.stream()
                        .filter(f -> f.getEstudios().stream()
                            .sorted(ordenarEstudios)
                            .equals(t.getEstudios().stream()
                                .sorted(ordenarEstudios)))
                        .count() > 0;
                } 
            }).count() > 0;
        }

        return existe;
    }
}
