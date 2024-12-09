package org.acme.api;

import dev.langchain4j.agent.tool.P;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.acme.business.AiService;
import org.acme.business.KnowledgeService;
import org.acme.business.TextDocument;
import org.acme.infra.TextDocumentRepository;
import org.jboss.resteasy.reactive.RestForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/knowledge")
public class KnowledgeResource {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeResource.class);
    @Inject
    KnowledgeService knowledgeService;
    @Inject
    AiService aiService;
    @Inject
    TextDocumentRepository textDocumentRepository;




    @POST
    @Path("/chat")
    @Transactional
    public String chatWithResources(@RestForm("content") String question){
        List<String> relevantDocs = knowledgeService.findMostRelevantKnowledgeTextSegments(question,5);
        return aiService.answer(question,relevantDocs);
    }

    @POST
    @Path("/fromText")
    @Transactional
    public void addKnowledgeText(
            @RestForm("title") String title,
            @RestForm("content") String content){
        TextDocument doc = new TextDocument();
        doc.content = content;
        doc.title= title;
        textDocumentRepository.save(doc);
        knowledgeService.addKnowledgeDocument(doc);
    }

    @GET
    @Path("/getAllTextDocument")
    @Transactional
   public List<TextDocument> getAllTextDocument(){
        return knowledgeService.getAllTextDocument();
   }



    //testing

    @GET
    @Path("/addDocument")
    @Transactional
    public void addDocument(){
        addKnowledgeText("hobbies", "I enjoy painting, hiking, and reading.");
        addKnowledgeText("occupation", "I work as a software engineer specializing in AI systems.");
        addKnowledgeText("education", "I graduated with a degree in Computer Science from MIT.");
        addKnowledgeText("location", "I live in Paris, France.");
        addKnowledgeText("pets", "I have a dog named Max and a cat named Luna.");

    }


    @GET
    @Path("/getAnswer")
    @Transactional
    public String getAnswer(){
        List<String> relevantDocs = knowledgeService
                .findMostRelevantKnowledgeTextSegments("Can you tell me about my pets ?",1);

        return aiService.answer("Can you tell me about my pets , it's okay to tell me  ?",relevantDocs);
    }
}

