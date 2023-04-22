package me.andidroid.testwar;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import io.opentelemetry.instrumentation.annotations.WithSpan;

@Path("/hello")
public class HelloResource {

    @Context
    private HttpServletRequest httpRequest;

    @Inject
    @ConfigProperty(name = "jboss.node.name")
    private String node;

    @GET
    @WithSpan
    @Produces("text/plain")
    @Path("/hello")
    @Counted(name = "hello", description = "count of hello method")
    public Response hello() {
        return Response.ok("Hello!").build();
    }

    @GET
    @WithSpan
    @Produces("text/plain")
    @Path("/session")
    @Counted(name = "session", description = "count of session method")
    public Response session() {
        System.out.println("session on node: " + node);
        System.out.println("session id: " + httpRequest.getSession().getId());
        Integer count = (Integer) httpRequest.getSession().getAttribute("count");
        System.out.println("old value of count: " + count);
        if (count == null) {
            count = Integer.valueOf(1);
        } else {
            count++;
        }
        httpRequest.getSession().setAttribute("count", count);
        return Response.ok(count).build();
    }
}
