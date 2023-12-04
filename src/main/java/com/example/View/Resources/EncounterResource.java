package com.example.View.Resources;

import com.example.Core.SearchService;
import com.example.View.ViewModels.SearchCriteria;
import com.example.View.ViewModels.SearchResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@Path("/")
@ApplicationScoped
public class EncounterResource {

    @Inject
    SearchService searchService;

    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<SearchResult>> search(SearchCriteria searchCriteria) {
        return searchService.search(searchCriteria);
    }


}
