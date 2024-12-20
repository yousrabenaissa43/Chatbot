package org.acme.infra;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.business.TextDocument;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TextDocumentRepository {


    @Inject
    EntityManager em;

    @Transactional
    public void save(TextDocument doc) {
        em.persist(doc);
    }

    @Transactional
    public TextDocument findById(UUID id) {
        return em.find(TextDocument.class, id);
    }

    @Transactional
    public List<TextDocument> getAllTextDocument(){
        return em.createQuery("SELECT t FROM TextDocument t", TextDocument.class).getResultList();
    }


}
