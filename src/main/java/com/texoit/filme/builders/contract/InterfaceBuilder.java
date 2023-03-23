package com.texoit.filme.builders.contract;

public interface InterfaceBuilder<T> {
    public T clone(T objectToBeCloned, T objectToClone);
}
