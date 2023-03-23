package com.texoit.filme.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

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
import com.texoit.filme.helpers.FilmeHelper;
import com.texoit.filme.models.Filme;
import com.texoit.filme.repositories.FilmeRepository;
import com.texoit.filme.services.FilmeService;

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
    private FilmeRepository filmeRepository;

    Filme model;
    
    Filme create(String estudio, String produtor, String titulo, Boolean isVencedor) {
        this.model = new Filme();
        this.model.setAno(1990);
        this.model.setEstudio(estudio);
        this.model.setProdutor(produtor);
        this.model.setTitulo(titulo);
        this.model.setVencedor(isVencedor);
        
        return filmeService.create(this.model);
    }

    Filme baseCreate() {
        return create("estudio", "produtor", "titulo", false);
    }

    @BeforeAll
    public void beforeAll() {
        FilmeHelper filmeHelper = new FilmeHelper();
        try {
            filmeHelper.popularBase(filmeRepository);
        } catch (IOException e) {
            System.exit(0);
        }
    }

    @Test
    public void getList() {
        Filme created = baseCreate();

        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/", HttpMethod.GET, null, String.class);
        
        List<Filme> list = new Gson().fromJson(response.getBody(), new TypeToken<List<Filme>>() {}.getType());

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(list.get((list.size() - 1)).getTitulo(), created.getTitulo());
    }

    @Test
    public void get() {
        Filme created = baseCreate();
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.GET, null, String.class);
        
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
        Filme other = create("estudio 2", "produtor 2", "titulo 2", true);
        
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
        
        ResponseEntity<String> response = this.testRestTemplate
            .exchange(baseurl+"/filmes/"+created.getId(), HttpMethod.DELETE, null, String.class);
        
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Boolean.parseBoolean(response.getBody()));
    }

}