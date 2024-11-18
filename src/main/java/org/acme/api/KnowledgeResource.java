package org.acme.api;


import dev.langchain4j.service.Result;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.acme.business.AiService;
import org.acme.business.RagService;
import org.acme.business.TextDocument;
import org.acme.infra.TextDocumentRepository;
import org.jboss.resteasy.reactive.RestForm;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public class KnowledgeResource {

    @Inject
    RagService ragService;
    @Inject
    AiService aiService;
    @Inject
    TextDocumentRepository textDocumentRepository;

    public void index(String content, Map<String,String> metadata){
        ragService.index(content, metadata);
    }

    public Result<String> chatWithResources(String question){
        List<String> relevantDocs = ragService.findMostRelevantKnowledgeTextSegments("1",question,5);
        return aiService.answer(question,relevantDocs);
    }

    @POST
    @Path("/fromText")
    @Transactional
    public Response addKnowledgeText(
            @NotBlank @PathParam("tenantId") String tenantId,
            @RestForm("content") String content) {
        UUID documentId = UUID.randomUUID();
        TextDocument doc = new TextDocument();
        doc.id = documentId;
        doc.tenantId = tenantId;
        doc.content = content;
        textDocumentRepository.save(doc);

        return Response.status(Response.Status.CREATED).build();
    }
}

