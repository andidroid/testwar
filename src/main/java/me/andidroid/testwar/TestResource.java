package me.andidroid.testwar;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

import io.opentelemetry.instrumentation.annotations.WithSpan;

@Path("/test")
public class TestResource {

    @Inject
    private TestService testService;

    @GET
    @Path("/all")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    // @Consumes(MediaType.APPLICATION_JSON)
    @WithSpan
    public Response getAll() {
        return Response.ok(testService.getAll()).build();
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    // @Consumes(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok(testService.getAll()).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    // @Consumes(MediaType.APPLICATION_JSON)
    @WithSpan
    public Response getById(@PathParam(value = "id") String id) {
        return Response.ok(testService.getById(Long.parseLong(id))).build();
    }
}
