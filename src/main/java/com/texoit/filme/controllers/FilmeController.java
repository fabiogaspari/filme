package com.texoit.filme.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.filme.controllers.contract.BaseController;
import com.texoit.filme.dtos.MinMaxIntervalosDTO;
import com.texoit.filme.models.Filme;
import com.texoit.filme.services.FilmeService;

@RestController
@RequestMapping("/filmes")
public class FilmeController extends BaseController<Filme> {

    @Autowired
    protected FilmeService service;

    protected FilmeController(FilmeService service) {
        super(service);
    }

    @GetMapping("/intervalo-premios")
    protected ResponseEntity<MinMaxIntervalosDTO> premios() {
        return new ResponseEntity<MinMaxIntervalosDTO>(
            this.service.findMinMaxIntervalo(),
            null, 
            HttpStatus.OK
        );
    }
    
}
