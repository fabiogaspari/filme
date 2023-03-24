package com.texoit.filme.dtos;

import lombok.Data;

@Data
public class IntervaloDTO {
    
    private String producer;

    private Integer interval;

    private Integer previousWin;

    private Integer followingWin;

    @Override
    public int hashCode() { 
        return producer.hashCode() + interval.hashCode() + previousWin.hashCode() + followingWin.hashCode(); 
    }

    @Override
    public boolean equals(Object obj) {
        boolean producerIgual = producer.equals(((IntervaloDTO)obj).getProducer());
        boolean intervalIgual = interval.equals(((IntervaloDTO)obj).getInterval());
        boolean previousWinIgual = previousWin.equals(((IntervaloDTO)obj).getPreviousWin());
        boolean followingWinIgual = followingWin.equals(((IntervaloDTO)obj).getFollowingWin());

        return producerIgual && intervalIgual && previousWinIgual && followingWinIgual;
    }
}
