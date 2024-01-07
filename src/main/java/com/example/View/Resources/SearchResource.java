package com.example.View.Resources;

import com.example.Core.SearchService;
import com.example.Core.SearchServiceException;
import com.example.View.ViewModels.SearchCriteria;
import com.example.View.ViewModels.SearchResult;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@ApplicationScoped
@Path("/")
public class SearchResource {

    @Inject
    SearchService searchService;

    @GET
    @Path("search/find/{criteria}")
    @RolesAllowed({"DOCTOR", "EMPLOYEE"})
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<SearchResult>> search(@PathParam("criteria") String criteria) {
        SearchCriteria searchCriteria = new SearchCriteria(criteria, criteria, criteria, criteria, criteria);

        return searchService.search(searchCriteria)
                .onFailure().transform(ex -> new SearchServiceException("Error occurred during search", ex));
    }

    @GET
    @Path("search/find")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<SearchResult> find() {
        return new ArrayList<>();
    }

    @GET
    @Path("health")
    @Produces(MediaType.TEXT_PLAIN)
    public String search() {
        return "ok";
    }


}
