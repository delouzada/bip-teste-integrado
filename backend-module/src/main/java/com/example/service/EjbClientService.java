package com.example.service;

import com.example.dto.TransferenciaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EjbClientService {

    @Value("${ejb.transfer.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public EjbClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void chamarTransferenciaEJB(TransferenciaDTO dto) {
        String url = baseUrl + "/transferencias";
        HttpEntity<TransferenciaDTO> entity = new HttpEntity<>(dto);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }
}
