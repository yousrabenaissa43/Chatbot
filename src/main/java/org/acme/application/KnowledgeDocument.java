package org.acme.application;

import java.util.UUID;

public record KnowledgeDocument(UUID id, String label, String type) {
    
}
