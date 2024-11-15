package org.acme;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/chatbot")
public class ChatbotResource {
    ChatbotService chatbotService ;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getProduct() {
        return "{\"type\": \"chemise\", \"couleur\": \"blanche\", \"qualité\": \"haute\", \"prix\": \"50$\"}";
    }
}
