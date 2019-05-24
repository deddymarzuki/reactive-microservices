package com.deddy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AirportDocument extends Airport {

    @Id
    public String id;
}
