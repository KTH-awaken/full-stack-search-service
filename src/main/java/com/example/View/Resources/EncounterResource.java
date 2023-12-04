package com.example.View.Resources;

import com.example.Core.SearchService;
import com.example.Entities.Encounter;
import com.example.Persistence.EncounterRepo;
import com.example.View.ViewModels.SearchCriteria;
import com.example.View.ViewModels.SearchResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/")
@ApplicationScoped
public class EncounterResource {

    @Inject
    SearchService searchService;



    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchResult> search(SearchCriteria searchCriteria) {
        return searchService.search(searchCriteria);

    }
}
