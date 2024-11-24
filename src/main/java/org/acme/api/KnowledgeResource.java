package org.acme.api;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.business.AiService;
import org.acme.business.KnowledgeService;
import org.acme.business.TextDocument;
import org.acme.infra.TextDocumentRepository;
import org.jboss.resteasy.reactive.RestForm;

import java.util.List;
import java.util.UUID;

@Path("/knowledge")
public class KnowledgeResource {

    @Inject
    KnowledgeService knowledgeService;
    @Inject
    AiService aiService;
    @Inject
    TextDocumentRepository textDocumentRepository;




    @POST
    @Path("/chat")

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
        UUID documentId = UUID.randomUUID();
        TextDocument doc = new TextDocument();
        doc.id = documentId;
        doc.content = content;
        doc.title=title;
        textDocumentRepository.save(doc);
        knowledgeService.addKnowledgeDocument(doc);
    }



}

