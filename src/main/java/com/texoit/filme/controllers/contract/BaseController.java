package com.texoit.filme.controllers.contract;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.texoit.filme.services.contract.AbstractService;
import jakarta.validation.Valid;

@RestController
public abstract class BaseController<T> {

    protected AbstractService<T> service;

    protected BaseController(AbstractService<T> service) {
        this.service = service;
    }
    
    @PostMapping("/")
    protected ResponseEntity<T> create(@RequestBody @Valid T object) {
        return new ResponseEntity<T>(
            this.service.create(object),
            null, 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/")
    protected ResponseEntity<List<T>> all() {
        return new ResponseEntity<List<T>>(
            this.service.all(),
            null, 
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    protected ResponseEntity<T> read(@PathVariable Long id) {
        return new ResponseEntity<T>(
            this.service.read(id),
            null, 
            HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    protected ResponseEntity<T> update(
        @RequestBody T object, 
        @PathVariable Long id ) {
        return new ResponseEntity<T>(
            this.service.update(object, id),
            null, 
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Boolean> delete(
        @PathVariable Long id ) {
        return new ResponseEntity<Boolean>(
            this.service.delete(id),
            null, 
            HttpStatus.OK
        );
    }


}
