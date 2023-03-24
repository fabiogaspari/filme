package com.texoit.filme.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.texoit.filme.helpers.FilmeHelper;
import com.texoit.filme.services.EstudioService;
import com.texoit.filme.services.FilmeService;
import com.texoit.filme.services.ProducaoService;

@Component    
public class InitialCommandRunner implements CommandLineRunner {
 
    @Autowired
    FilmeService filmeService;

    @Autowired
    ProducaoService producaoService;

    @Autowired
    EstudioService estudioService;
    
    @Override
    public void run(String... args) throws Exception {
        FilmeHelper filmeHelper = new FilmeHelper();
        filmeHelper.popularBase(filmeService, estudioService, producaoService);
    }

}