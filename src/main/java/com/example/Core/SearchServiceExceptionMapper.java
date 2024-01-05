package com.example.Core;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class SearchServiceExceptionMapper implements ExceptionMapper<SearchServiceException> {

    @Override
    public Response toResponse(SearchServiceException exception) {
        // Log the stack trace
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error during search", exception);

        // Create an error response
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error occurred: " + exception.getMessage())
                .build();
    }
}
