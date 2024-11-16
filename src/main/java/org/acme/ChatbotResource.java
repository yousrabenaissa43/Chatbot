package org.acme;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/chatbot")
public class ChatbotResource {

    @Inject
    ChatbotService chatbotService ;}

   /* @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String chat(String body ) {
        return chatbotService.chat(body);
    }
}*/

