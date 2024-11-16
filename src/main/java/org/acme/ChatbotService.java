package org.acme;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService()
public interface ChatbotService {

    @SystemMessage("""
    you will analyse the query and you will answer ONLY in JSON like this depending on the data provided in the message (if not provided put it to null ) 
    input : Je veux une chemise blanche de haute qualité à peu près 50$.
    Output: {
            "type": "chemise",
            "colour": "blanche",
            "quality": "haute",
            "price": "50"
            }
    """)
   /* @UserMessage("""
            Je veux une {type} {colour} de {quality} qualité à peut prés {price}$.
            
            """)
    public  String analyseMessage(String type, String colour,String quality ,int  price); */
    public String chat(@UserMessage  String message );
}