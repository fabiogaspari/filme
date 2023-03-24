package com.texoit.filme.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.texoit.filme.builders.FilmeBuilder;
import com.texoit.filme.dtos.IntervaloDTO;
import com.texoit.filme.dtos.MinMaxIntervalosDTO;
import com.texoit.filme.dtos.ProducaoAnoDTO;
import com.texoit.filme.dtos.ProducaoAnosDTO;
import com.texoit.filme.models.Filme;
import com.texoit.filme.repositories.FilmeRepository;
import com.texoit.filme.services.contract.AbstractService;

@Service
public class FilmeService extends AbstractService<Filme> {
    
    @Autowired
    protected FilmeRepository repository;

    public FilmeService(FilmeRepository repository, FilmeBuilder builder) {
        super(repository, builder);
    }

    /** 
     * @return IntervaloDTO
     */
    public IntervaloDTO maiorIntervalo() {
        IntervaloDTO intervalo = new IntervaloDTO();
        return intervalo;
    }

    /** 
     * @param ano
     * @param titulo
     * @return Set<Filme>
     */
    public Set<Filme> findByAnoAndTitulo(Integer ano, String titulo) {
        return this.repository.findByAnoAndTitulo(ano, titulo);
    }
    
    /** 
     * @return MinMaxIntervalosDTO
     */
    public MinMaxIntervalosDTO findMinMaxIntervalor() {
        Set<Filme> vencedores = this.repository.findByVencedor(true);

        List<ProducaoAnoDTO> listaProducoesAnos = new ArrayList<ProducaoAnoDTO>();

        vencedores.stream().forEach(v -> {
            v.getProducoes().forEach(p -> {
                ProducaoAnoDTO producaoAno = new ProducaoAnoDTO();
                producaoAno.setProducao(p);
                producaoAno.setAno(v.getAno());
                listaProducoesAnos.add(producaoAno);
            });
        });

        List<ProducaoAnosDTO> listaProducaoVencedorAnos = new ArrayList<ProducaoAnosDTO>(); 

        for (int i = 0; i < (listaProducoesAnos.size() - 1); i++) {
            final int k;
            k = i;
            ProducaoAnosDTO producaoAnosDTO = new ProducaoAnosDTO();
            producaoAnosDTO.setAnos(new ArrayList<Integer>());
            producaoAnosDTO.setProducao(listaProducoesAnos.get(i).getProducao());
            
            //se o Producao ja esta na lista, nao insere novamente
            if ( listaProducaoVencedorAnos.stream().filter(lpva -> lpva.getProducao().getProducao().equals(listaProducoesAnos.get(k).getProducao().getProducao())).count() == 0 ) {
                for (int j = 0; j < (listaProducoesAnos.size() - 1); j++) {
                    boolean produtoIgual = listaProducoesAnos.get(i).getProducao().equals(listaProducoesAnos.get(j).getProducao());
                    if ( produtoIgual ) {
                        producaoAnosDTO.getAnos().add(listaProducoesAnos.get(j).getAno());
                    }
                }            
                listaProducaoVencedorAnos.add(producaoAnosDTO);
            }
        }

        List<IntervaloDTO> listaMinIntervalos = new ArrayList<IntervaloDTO>();
        List<IntervaloDTO> listaMaxIntervalos = new ArrayList<IntervaloDTO>();
        for (int i = 0; i < ( listaProducaoVencedorAnos.size() - 1); i++) {
            if ( listaProducaoVencedorAnos.get(i).getAnos().size() > 1 ) {
                listaProducaoVencedorAnos.get(i).setAnos(listaProducaoVencedorAnos.get(i).getAnos().stream().sorted().toList());
                
                IntervaloDTO minIntervalo = new IntervaloDTO();
                minIntervalo.setInterval((listaProducaoVencedorAnos.get(i).getAnos().get(1) - listaProducaoVencedorAnos.get(i).getAnos().get(0)));
                minIntervalo.setPreviousWin(listaProducaoVencedorAnos.get(i).getAnos().get(0));
                minIntervalo.setFollowingWin(listaProducaoVencedorAnos.get(i).getAnos().get(1));
                minIntervalo.setProducer(listaProducaoVencedorAnos.get(i).getProducao().getProducao());
                listaMinIntervalos.add(minIntervalo);
    
                IntervaloDTO maxIntervalo = new IntervaloDTO();
                maxIntervalo.setInterval((listaProducaoVencedorAnos.get(i).getAnos().get(listaProducaoVencedorAnos.get(i).getAnos().size() - 1) - listaProducaoVencedorAnos.get(i).getAnos().get(0)));
                maxIntervalo.setPreviousWin(listaProducaoVencedorAnos.get(i).getAnos().get(0));
                maxIntervalo.setFollowingWin(listaProducaoVencedorAnos.get(i).getAnos().get(listaProducaoVencedorAnos.get(i).getAnos().size() - 1));
                maxIntervalo.setProducer(listaProducaoVencedorAnos.get(i).getProducao().getProducao());
                listaMaxIntervalos.add(maxIntervalo);
            }
        }

        int min = listaMinIntervalos.stream().map(IntervaloDTO::getInterval).mapToInt(Number::intValue).summaryStatistics().getMin();
        int max = listaMinIntervalos.stream().map(IntervaloDTO::getInterval).mapToInt(Number::intValue).summaryStatistics().getMax();

        Set<IntervaloDTO> listaMinimos = new HashSet<>(listaMinIntervalos.stream().filter(lmi -> lmi.getInterval().equals(min)).toList());
        Set<IntervaloDTO> listaMaximos = new HashSet<>(listaMaxIntervalos.stream().filter(lma -> lma.getInterval().equals(max)).toList());
        
        MinMaxIntervalosDTO minMaxIntervalosDTO = new MinMaxIntervalosDTO();
        minMaxIntervalosDTO.setMin(listaMinimos);
        minMaxIntervalosDTO.setMax(listaMaximos);

        return minMaxIntervalosDTO;
    }
}
