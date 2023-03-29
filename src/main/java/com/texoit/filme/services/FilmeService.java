package com.texoit.filme.services;

import java.util.ArrayList;
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
    public MinMaxIntervalosDTO findMinMaxIntervalo() {
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

        List<ProducaoAnosDTO> listaProducaoVencedorAnos = this.construirListaProducaoVencedoreAnos(listaProducoesAnos);

        List<IntervaloDTO> intervalos = this.construrMinMaxIntervalos(listaProducaoVencedorAnos);

        return this.montarMinMaxIntervalos(intervalos);
    }
    
    /** 
     * @param listaProducoesAnos
     * @return List<ProducaoAnosDTO>
     */
    public List<ProducaoAnosDTO> construirListaProducaoVencedoreAnos(List<ProducaoAnoDTO> listaProducoesAnos) {
        List<ProducaoAnosDTO> listaProducaoVencedorAnos = new ArrayList<ProducaoAnosDTO>(); 

        listaProducoesAnos.forEach(lpa -> {
            ProducaoAnosDTO producaoAnosDTO = new ProducaoAnosDTO();
            producaoAnosDTO.setAnos(new ArrayList<Integer>());
            producaoAnosDTO.setProducao(lpa.getProducao());
            
            //se producao ja esta na lista, nao insere novamente
            if ( listaProducaoVencedorAnos.stream().filter(lpva -> lpva.getProducao().getProducao()
                                                                        .equals(lpa.getProducao().getProducao()))
                                                                        .count() == 0 
            ) {
                    listaProducoesAnos.forEach(lpa2 -> {
                        boolean produtoIgual = lpa.getProducao().equals(lpa2.getProducao());
                        if ( produtoIgual ) {
                            producaoAnosDTO.getAnos().add(lpa2.getAno());
                        }
                    });
                listaProducaoVencedorAnos.add(producaoAnosDTO);
            }
        });

        return listaProducaoVencedorAnos;
    }
    
    /** 
     * @param listaProducaoVencedorAnos
     * @return List<IntervaloDTO>
     */
    public List<IntervaloDTO> construrMinMaxIntervalos(List<ProducaoAnosDTO> listaProducaoVencedorAnos) {
        List<IntervaloDTO> listaIntervalos = new ArrayList<IntervaloDTO>();
        
        listaProducaoVencedorAnos.forEach(lpva -> {
            if ( lpva.getAnos().size() > 1 ) {
                for (int i = 0; i <= lpva.getAnos().size()-2; i++) {
                    lpva.setAnos(lpva.getAnos().stream().sorted().toList());
                    
                    Integer diff = (lpva.getAnos().get(i+1) - lpva.getAnos().get(i));
                    
                    if ( diff > 0 ) {
                        IntervaloDTO intervalo = new IntervaloDTO();
                        intervalo.setInterval(diff);
                        intervalo.setPreviousWin(lpva.getAnos().get(i));
                        intervalo.setFollowingWin(lpva.getAnos().get(i+1));
                        intervalo.setProducer(lpva.getProducao().getProducao());
                        listaIntervalos.add(intervalo);
                    }
                }
            }
        });

        return listaIntervalos;
    }

    /** 
     * @param intervalos
     * @return MinMaxIntervalosDTO
     */
    public MinMaxIntervalosDTO montarMinMaxIntervalos(List<IntervaloDTO> intervalos) {
        int min = intervalos.stream().map(IntervaloDTO::getInterval).mapToInt(Number::intValue).summaryStatistics().getMin();
        int max = intervalos.stream().map(IntervaloDTO::getInterval).mapToInt(Number::intValue).summaryStatistics().getMax();

        List<IntervaloDTO> listaMinimos = intervalos.stream().filter(lmi -> lmi.getInterval().equals(min)).toList();
        List<IntervaloDTO> listaMaximos = intervalos.stream().filter(lma -> lma.getInterval().equals(max)).toList();
        
        MinMaxIntervalosDTO minMaxIntervalosDTO = new MinMaxIntervalosDTO();
        minMaxIntervalosDTO.setMin(listaMinimos);
        minMaxIntervalosDTO.setMax(listaMaximos);

        return minMaxIntervalosDTO;
    }
}
