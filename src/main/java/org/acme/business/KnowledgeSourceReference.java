package org.acme.business;

import java.util.UUID;

public record KnowledgeSourceReference(UUID documentId, int index, String snippet) {
    
}
