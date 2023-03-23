package com.texoit.filme.integration.contract;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public abstract class BaseIntegrationTest<T> {
    protected String route;

    final Class<T> typeParameterClass;

    public BaseIntegrationTest(String route, Class<T> typeParameterClass) {
        this.route = route;
        this.typeParameterClass = typeParameterClass;
    }

    @Value("${server.baseurl}")
    private String baseurl;

    protected abstract T create();
    protected abstract void getListAssertions(List<T> list);
    protected abstract void getAssertions(T entity);
    protected abstract void postAssertions(T entity);
    protected abstract Long getId(T entity);

}
