package org.acme.business;

import java.util.List;

public record KnowledgeAnswer(String response, List<KnowledgeSourceReference> sources) {
    
}
