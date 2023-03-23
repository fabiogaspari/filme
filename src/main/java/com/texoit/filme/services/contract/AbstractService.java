package com.texoit.filme.services.contract;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.texoit.filme.builders.contract.InterfaceBuilder;
public abstract class AbstractService<T> {
    
    protected JpaRepository<T, Long> repository;

    InterfaceBuilder<T> builder;

    public AbstractService() {}

    public AbstractService(JpaRepository<T, Long> repository, InterfaceBuilder<T> builder) {
        this.repository = repository;
        this.builder = builder;
    }
    
    public T create(T object) {
        return this.repository.save(object);
    }

    public T read(Long id) {
        return (T) this.repository
            .findById(id)
            .orElse(null);
    }

    public List<T> all() {
        return (List<T>) this.repository.findAll();
    }

    public T update(T toBeCloned, Long id) {
        T toClone = (T) this.read(id);
        if ( toClone == null ) return null;
        
        T clone = builder.clone(toBeCloned, toClone);
        return this.repository.save(clone);
    }
    
    public boolean delete(Long id) {
        T object = this.read(id);
        if ( object == null ) return false;
        
        this.repository.deleteById(id);
        return true;
    }

}
