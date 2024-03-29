package me.andidroid.testwar;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@Path("/cache")
public class CacheResource {

    @Inject
    private CacheService cacheService;

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
    // @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam(value = "id") String id) {
        cacheService.putRemoteCache(id, "test");
        return Response.ok(cacheService.getRemoteCache(id)).build();
    }
}
