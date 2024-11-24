package org.acme.business;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.service.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.acme.infra.KnowledgeEmbeddingsRepository;
import org.acme.infra.TextDocumentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@ApplicationScoped
public class KnowledgeService {
    @Inject
    KnowledgeEmbeddingsRepository    knowledgeEmbeddingsRepository;
    @Inject
    AiService aiService ;
    @Inject
    TextDocumentRepository textDocumentRepository ;



    public List<String> findMostRelevantKnowledgeTextSegments(String question, int maxResults){
    return knowledgeEmbeddingsRepository.findMostRelevantKnowledgeTextSegments(question,maxResults);
    }

    @Transactional
    public void addKnowledgeDocument(TextDocument textDocument)  {
        knowledgeEmbeddingsRepository.index(textDocument);

    }

}
