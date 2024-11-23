package org.acme.infra;

import java.util.List;
import java.util.Map;
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
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import jakarta.enterprise.context.ApplicationScoped;
import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;

@ApplicationScoped
public class KnowledgeEmbeddingsRepository {
    @Inject
    EmbeddingModel embeddingModel;
    @Inject
    EmbeddingStore store;

    static final String SOURCE_TEXT = "DocumentId %s index %d : %s\n";

    public void index(String content, Map<String,String> metadata){
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(1024, 100))
                .build();
        ingestor.ingest(new Document(content,Metadata.from(metadata))); //encapsulation
    }
    
    
    public List<String> findMostRelevantKnowledgeTextSegments(@NotBlank String tenantId, String question, int maxResults){
        Response<Embedding> embeddedQuery = embeddingModel.embed(question);
        Filter tenantFilter = new IsEqualTo("tenantId",tenantId);
        EmbeddingSearchResult matchingSchemaDocs = store.search(new EmbeddingSearchRequest(embeddedQuery.content(), maxResults, 0.5, tenantFilter));
        
        List<TextSegment> relevantSegments = ((List<EmbeddingMatch<TextSegment>>)matchingSchemaDocs.matches()).stream().map(m->m.embedded()).toList();
        return relevantSegments.stream().map(s->String.format(SOURCE_TEXT, s.metadata().getUUID("documentId").toString(),s.metadata().getInteger("index"),s.text())).toList();

    }
    // @Tool("use this tool to get complementary documents from the knowledge base.")
    // public List<String> findMostRelevantKnowledgeDocuments(String question){
    //     Response<Embedding> embeddedQuery = embeddingModel.embed(question);
    //     // Filter datasourceSchemaDocs = and(
    //     //     metadataKey("datasourceId").isEqualTo(datasourceId),
    //     //     metadataKey("documentType").isEqualTo(DocumentType.SCHEMA_DOC.toString())
    //     // );
    //     EmbeddingSearchResult matchingSchemaDocs = store.search(new EmbeddingSearchRequest(embeddedQuery.content(), 2, 0.7, null));
        
    //     List<TextSegment> relevantSegments = ((List<EmbeddingMatch<TextSegment>>)matchingSchemaDocs.matches()).stream().map(m->m.embedded()).toList();
    //     return relevantSegments.stream().map(s->String.format(SOURCE_TEXT, s.metadata().getUUID("documentId").toString(),s.metadata().getInteger("index"),s.text())).toList();

    // }
    
}