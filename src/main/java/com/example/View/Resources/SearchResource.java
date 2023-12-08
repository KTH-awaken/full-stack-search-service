package com.example.View.Resources;

import com.example.Core.SearchService;
import com.example.View.ViewModels.SearchCriteria;
import com.example.View.ViewModels.SearchResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
@Path("/search")
public class SearchResource {

    @Inject
    SearchService searchService;

    @GET
    @Path("/find/{criteria}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<SearchResult>> search(@PathParam("criteria") String criteria) {
        SearchCriteria searchCriteria = new SearchCriteria(criteria,criteria,criteria,criteria,criteria);
        return searchService.search(searchCriteria);
    }
    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<SearchResult> find() {
        return new ArrayList<>();
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String search() {
        return "test";
    }


}
