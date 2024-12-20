package org.acme.infra;


import java.util.List;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

import dev.langchain4j.store.embedding.filter.Filter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.business.TextDocument;

@ApplicationScoped
public class KnowledgeEmbeddingsRepository {
    @Inject
    EmbeddingModel embeddingModel;
    @Inject
    EmbeddingStore store;

    static final String SOURCE_TEXT = "DocumentId %s index %d : %s\n";



    public void index(TextDocument textDocument) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(1024, 100)) // Split the document into segments
                .build();

        String documentId = java.util.UUID.randomUUID().toString();

        // Create Metadata
        Metadata metadata = new Metadata();
        metadata.add("documentId", documentId);

        Document document = new Document(textDocument.content, metadata);
        ingestor.ingest(document);
    }





    public List<String> findMostRelevantKnowledgeTextSegments(String question, int maxResults) {
        Response<Embedding> embeddedQuery = embeddingModel.embed(question);

        Filter noFilter = null;

        EmbeddingSearchResult matchingSchemaDocs = store.search(
                new EmbeddingSearchRequest(embeddedQuery.content(), maxResults, 0.5, noFilter)
        );

        List<TextSegment> relevantSegments = ((List<EmbeddingMatch<TextSegment>>)matchingSchemaDocs.matches())
                .stream()
                .map(m -> m.embedded())
                .toList();

        return relevantSegments.stream()
                .map(s -> String.format(SOURCE_TEXT,
                        s.metadata().getUUID("documentId").toString(),
                        s.metadata().getInteger("index"),
                        s.text()))
                .toList();
    }







}