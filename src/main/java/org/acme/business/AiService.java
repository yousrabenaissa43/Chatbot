package org.acme.business;


import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

//@RegisterAiService(tools = KnowledgeRepository.class)
@RegisterAiService
@ApplicationScoped
public interface AiService {
    @SystemMessage("""
    You are an AI answering questions about some documents.
    Your response must be polite, and be relevant to the question.
    When you don't know, respond that you don't know the answer.
    Answer in the same language as the question.
    """)
    @UserMessage("""
        this is the question the user asked : {question}
        Use the following documents as sources: {relevantDocs}
        respond to the question with sufficent and clear and complete response.
        then return the response to the question and the list of documentId, index and snippet of text segments that you used and that are relevant to the response. 
        """)
    Result<KnowledgeAnswer> answerWithSources(String question, List<String> relevantDocs);

    @SystemMessage("""
    You are an AI answering questions about some documents.
    Your response must be polite, and be relevant to the question.
    When you don't know, respond that you don't know the answer.
    Answer in the same language as the question.
    """)
    @UserMessage("""
        this is the question the user asked : {question}
        Use the following documents as sources: {relevantDocs}
        respond to the question with sufficent and clear and complete response.
        """)
    Result<String> answer(String question, List<String> relevantDocs);

}
