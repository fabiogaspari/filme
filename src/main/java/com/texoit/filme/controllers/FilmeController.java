package com.texoit.filme.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.filme.controllers.contract.BaseController;
import com.texoit.filme.models.Filme;
import com.texoit.filme.services.FilmeService;

@RestController
@RequestMapping("/filmes")
public class FilmeController extends BaseController<Filme> {

    protected FilmeController(FilmeService service) {
        super(service);
    }

    @GetMapping("/pior")
    protected ResponseEntity<Object> premios() {
        return new ResponseEntity<Object>(
            this.service.all(),
            null, 
            HttpStatus.OK
        );
    }
    
}
