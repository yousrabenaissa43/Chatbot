package org.acme.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import jakarta.validation.constraints.NotBlank;
import org.acme.infra.KnowledgeEmbeddingsRepository;


import java.util.List;
import java.util.Map;

@ApplicationScoped
public class RagService {
    @Inject
    KnowledgeEmbeddingsRepository    knowledgeEmbeddingsRepository;

    public void index(String content, Map<String,String> metadata){
        knowledgeEmbeddingsRepository.index(content,metadata);
    }

    public List<String> findMostRelevantKnowledgeTextSegments(@NotBlank String tenantId, String question, int maxResults){
    return knowledgeEmbeddingsRepository.findMostRelevantKnowledgeTextSegments(tenantId,question,maxResults);
    }

}
