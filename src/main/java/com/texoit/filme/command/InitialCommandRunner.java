package com.texoit.filme.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.texoit.filme.helpers.FilmeHelper;
import com.texoit.filme.repositories.FilmeRepository;

@Component    
public class InitialCommandRunner implements CommandLineRunner {
 
    @Autowired
    FilmeRepository filmeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        FilmeHelper filmeHelper = new FilmeHelper();
        filmeHelper.popularBase(filmeRepository);
    }

}