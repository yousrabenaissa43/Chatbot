package org.acme.business;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.service.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.acme.infra.KnowledgeEmbeddingsRepository;
import org.acme.infra.TextDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class KnowledgeService {
    private static final Logger log = LoggerFactory.getLogger(KnowledgeService.class);
    @Inject
    KnowledgeEmbeddingsRepository    knowledgeEmbeddingsRepository;
    @Inject
    AiService aiService ;
    @Inject
    TextDocumentRepository textDocumentRepository ;




    public Result<String> chatWithResources(String prompt){

        List<String> relevantDocs =  knowledgeEmbeddingsRepository.findMostRelevantKnowledgeTextSegments("",prompt,5);
        return aiService.answer(prompt,relevantDocs) ;
    }

    public void index(String content, Map<String,String> metadata){
        knowledgeEmbeddingsRepository.index(content,metadata);
    }

    public List<String> findMostRelevantKnowledgeTextSegments(@NotBlank String tenantId, String question, int maxResults){
    return knowledgeEmbeddingsRepository.findMostRelevantKnowledgeTextSegments(tenantId,question,maxResults);
    }

    @Transactional
    public void addKnowledgeDocument(@NotBlank String tenantId, UUID documentId)  {
        TextDocument doc = textDocumentRepository.findById(documentId);
        Map<String,String> documentMetadata;
        String contentToIndex="";

            documentMetadata = Map.of("documentId",documentId.toString(), "type","TEXT", "title",doc.title,"tenantId",tenantId);
            contentToIndex=doc.title+"\n"+doc.content;

        knowledgeEmbeddingsRepository.index(contentToIndex,documentMetadata);

    }

}
