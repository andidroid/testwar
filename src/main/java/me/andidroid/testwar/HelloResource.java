package me.andidroid.testwar;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.ConcurrentGauge;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Path("/")
public class HelloResource
{
    
    @Context
    private HttpServletRequest httpRequest;
    
    @Inject
    @ConfigProperty(name = "jboss.node.name")
    private String node;
    
    @GET
    @Produces("text/plain")
    @Path("/hello")
    @Counted(name = "hello", description = "count of hello method")
    public Response hello()
    {
        return Response.ok("Hello!").build();
    }
    
    @GET
    @Produces("text/plain")
    @Path("/session")
    @Counted(name = "session", description = "count of session method")
    public Response session()
    {
        System.out.println("session on node: " + node);
        System.out.println("session id: " + httpRequest.getSession().getId());
        Integer count = (Integer) httpRequest.getSession().getAttribute("count");
        System.out.println("old value of count: " + count);
        if(count == null)
        {
            count = Integer.valueOf(1);
        }
        else
        {
            count++;
        }
        httpRequest.getSession().setAttribute("count", count);
        return Response.ok(count).build();
    }
}
