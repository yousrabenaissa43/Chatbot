package org.acme.business;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class TextDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    public String tenantId;

    public String title;
    public String content;


}
