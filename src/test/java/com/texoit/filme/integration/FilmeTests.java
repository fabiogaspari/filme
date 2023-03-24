package com.texoit.filme.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
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
import com.google.gson.reflect.TypeToken;
import com.texoit.filme.dtos.IntervaloDTO;
import com.texoit.filme.dtos.MinMaxIntervalosDTO;
import com.texoit.filme.helpers.FilmeHelper;
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
    
    @Value("${server.baseurl}")
    private String baseurl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FilmeService filmeService;

    @Autowired
    private ProducaoService ProducaoService;

    @Autowired
    private EstudioService estudioService;

    Filme model;
    
    Filme create(String estudio, String Producao, String titulo, Boolean isVencedor) {
        Estudio objEstudio = new Estudio();
        objEstudio.setEstudio(estudio);
        Estudio estudioCriado = estudioService.create(objEstudio);
        List<Estudio> estudios = new ArrayList<Estudio>();
        estudios.add(estudioCriado);

        Producao objProducao = new Producao();
        objProducao.setProducao(Producao);
        Producao ProducaoCriado = ProducaoService.create(objProducao);
        List<Producao> Producoes = new ArrayList<Producao>();
        Producoes.add(ProducaoCriado);

        this.model = new Filme();
        this.model.setAno(1990);
        this.model.setEstudios(estudios);
        this.model.setProducoes(Producoes);
        this.model.setTitulo(titulo);
        this.model.setVencedor(isVencedor);
        
        return filmeService.create(this.model);
    }

    Filme baseCreate() {
        return create("estudio", "Producao", "titulo", false);
    }

    @BeforeAll
    public void beforeAll() {
        FilmeHelper filmeHelper = new FilmeHelper();
        try {
            filmeHelper.popularBase(filmeService, estudioService, ProducaoService);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    @Test
    public void getList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<Filme[]> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/", HttpMethod.GET, new HttpEntity<>(headers), Filme[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
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

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(model.getTitulo(), created.getTitulo());
    }

    @Test
    public void post() {
        Filme created = baseCreate();

        HttpEntity<Filme> entity = new HttpEntity<Filme>(created);

        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/", HttpMethod.POST, entity, String.class);
        
        Filme model = new Gson().fromJson(response.getBody(), Filme.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(model.getTitulo(), created.getTitulo());
    }

    @Test
    public void put() {
        Filme created = baseCreate();
        Filme other = create("estudio 2", "Producao 2", "titulo 2", true);
        
        HttpEntity<Filme> entity = new HttpEntity<Filme>(other);

        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.PUT, entity, String.class);
        
        Filme model = new Gson().fromJson(response.getBody(), Filme.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
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
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Boolean.parseBoolean(response.getBody()));
    }

    @Test
    public void verificarIntervaloPremios() {
        Filme created = baseCreate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/intervalo-premios", HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        
        MinMaxIntervalosDTO minMaxIntervaloDTO = filmeService.findMinMaxIntervalor();
        MinMaxIntervalosDTO gsonResponse = new Gson().fromJson(response.getBody(), MinMaxIntervalosDTO.class);

        assertTrue(minMaxIntervaloDTO.getMax().equals(gsonResponse.getMax()));
        assertTrue(minMaxIntervaloDTO.getMin().equals(gsonResponse.getMin()));
    }

}