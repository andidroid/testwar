package me.andidroid.testwar;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.opentracing.Traced;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Path("/test")
@Traced
public class TestResource {

    @Inject
    private TestService testService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam(value = "id") String id) {
        return Response.ok(testService.getById(Long.parseLong(id))).build();
    }
}
