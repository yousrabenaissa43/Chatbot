package org.acme.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.infra.KnowledgeEmbeddingsRepository;
import org.acme.infra.TextDocumentRepository;
import java.util.List;


@ApplicationScoped
public class KnowledgeService {
    @Inject
    KnowledgeEmbeddingsRepository    knowledgeEmbeddingsRepository;
    @Inject
    AiService aiService ;
    @Inject
    TextDocumentRepository textDocumentRepository ;


    @Transactional
    public List<String> findMostRelevantKnowledgeTextSegments(String question, int maxResults){
    return knowledgeEmbeddingsRepository.findMostRelevantKnowledgeTextSegments(question,maxResults);
    }

    @Transactional
    public void addKnowledgeDocument(TextDocument textDocument)  {
        knowledgeEmbeddingsRepository.index(textDocument);
    }

    @Transactional
    public List<TextDocument> getAllTextDocument(){
       return  textDocumentRepository.getAllTextDocument();
    }

}
