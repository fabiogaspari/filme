package com.texoit.filme.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.google.gson.Gson;
import com.texoit.filme.dtos.IntervaloDTO;
import com.texoit.filme.dtos.MinMaxIntervalosDTO;
import com.texoit.filme.models.Estudio;
import com.texoit.filme.models.Filme;
import com.texoit.filme.models.Producao;
import com.texoit.filme.services.EstudioService;
import com.texoit.filme.services.FilmeService;
import com.texoit.filme.services.ProducaoService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("fixedport")
@TestInstance(Lifecycle.PER_CLASS)
public class FilmeTests {
    
    static {
        System.setProperty("enviroment", "test");
    }

    @Value("${server.baseurl}")
    private String baseurl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ProducaoService producaoService;

    @Autowired
    private EstudioService estudioService;

    Filme model;
    
    Filme create(String estudio, String producao, String titulo, Boolean isVencedor) {
        Estudio objEstudio = new Estudio();
        objEstudio.setEstudio(estudio);
        Estudio estudioCriado = estudioService.create(objEstudio);
        List<Estudio> estudios = new ArrayList<Estudio>();
        estudios.add(estudioCriado);

        Producao objProducao = new Producao();
        objProducao.setProducao(producao);
        Producao producaoCriado = producaoService.create(objProducao);
        List<Producao> producoes = new ArrayList<Producao>();
        producoes.add(producaoCriado);

        this.model = new Filme();
        this.model.setAno(1990);
        this.model.setEstudios(estudios);
        this.model.setProducoes(producoes);
        this.model.setTitulo(titulo);
        this.model.setVencedor(isVencedor);
        
        return filmeService.create(this.model);
    }

    Filme baseCreate() {
        return create("estudio", "producao", "titulo", false);
    }

    @Test
    public void getList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<Filme[]> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/", HttpMethod.GET, new HttpEntity<>(headers), Filme[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void get() {
        Filme created = baseCreate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Filme> entity = new HttpEntity<Filme>(headers);
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.GET, entity, String.class);
        
        Filme model = new Gson().fromJson(response.getBody(), Filme.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(model.getTitulo(), created.getTitulo());
    }

    @Test
    public void post() {
        Filme created = baseCreate();

        HttpEntity<Filme> entity = new HttpEntity<Filme>(created);

        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/", HttpMethod.POST, entity, String.class);
        
        Filme model = new Gson().fromJson(response.getBody(), Filme.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(model.getTitulo(), created.getTitulo());
    }

    @Test
    public void put() {
        Filme created = baseCreate();
        Filme other = create("estudio 2", "producao 2", "titulo 2", true);
        
        HttpEntity<Filme> entity = new HttpEntity<Filme>(other);

        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.PUT, entity, String.class);
        
        Filme model = new Gson().fromJson(response.getBody(), Filme.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(model.getTitulo(), other.getTitulo());
    }

    @Test
    public void delete() {
        Filme created = baseCreate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Filme> entity = new HttpEntity<Filme>(headers);
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.DELETE, entity, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Boolean.parseBoolean(response.getBody()));
    }

    @Test
    public void verificarIntervaloPremios() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/intervalo-premios", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        
        MinMaxIntervalosDTO minMaxIntervaloDTO = new MinMaxIntervalosDTO();

        List<IntervaloDTO> setMinIntervalos = new ArrayList<>();
        IntervaloDTO minIntervalo = new IntervaloDTO();
        minIntervalo.setFollowingWin(1991);
        minIntervalo.setPreviousWin(1990);
        minIntervalo.setInterval(1);
        minIntervalo.setProducer("Joel Silver");
        setMinIntervalos.add(minIntervalo);
        
        List<IntervaloDTO> setMaxIntervalos = new ArrayList<>();
        IntervaloDTO maxIntervalo = new IntervaloDTO();
        maxIntervalo.setFollowingWin(2015);
        maxIntervalo.setPreviousWin(2002);
        maxIntervalo.setInterval(13);
        maxIntervalo.setProducer("Matthew Vaughn");
        setMaxIntervalos.add(maxIntervalo);

        minMaxIntervaloDTO.setMin(setMinIntervalos);
        minMaxIntervaloDTO.setMax(setMaxIntervalos);

        MinMaxIntervalosDTO gsonResponse = new Gson().fromJson(response.getBody(), MinMaxIntervalosDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(minMaxIntervaloDTO.getMax().equals(gsonResponse.getMax()));
        assertTrue(minMaxIntervaloDTO.getMin().equals(gsonResponse.getMin()));
    }

}