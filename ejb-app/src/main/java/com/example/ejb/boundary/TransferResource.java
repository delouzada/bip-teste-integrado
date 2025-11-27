package com.example.ejb.boundary;

import com.example.ejb.BeneficioEjbService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transferencias")
public class TransferResource {

    @EJB
    private BeneficioEjbService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transferir(TransferRequestDto dto) {
        service.transferir(dto.fromId, dto.toId, dto.valor);
        return Response.ok().build();
    }
}
