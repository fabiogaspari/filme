package com.texoit.filme.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.texoit.filme.models.Filme;
import com.texoit.filme.repositories.FilmeRepository;

public class FilmeHelper {
    private final String DELIMITADOR_PONTO_E_VIRGULA = ";";
    
    public void popularBase(FilmeRepository filmeRepository) throws NumberFormatException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("movielist.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String linha;
        int primeiraLinha = 0;
        while ( (linha = br.readLine()) != null ) {
            if ( primeiraLinha == 0 ) {
                primeiraLinha++;
                continue;
            } 
            String[] values = linha.split(DELIMITADOR_PONTO_E_VIRGULA);
            Filme filme = new Filme();
            List<Filme> filmes = filmeRepository.findByAnoAndTituloAndEstudioAndProdutor(
                Integer.parseInt(values[0]),
                values[1], 
                values[2], 
                values[3]
            );

            if ( !filmes.isEmpty() ) continue;

            filme.setAno(Integer.parseInt(values[0]));
            filme.setTitulo(values[1]);
            filme.setEstudio(values[2]);
            filme.setProdutor(values[3]);
            filme.setVencedor(values.length >= 5 && values[4].equals("yes") ? true : false );
            filmeRepository.save(filme);
        }

        br.close();
    }
}
