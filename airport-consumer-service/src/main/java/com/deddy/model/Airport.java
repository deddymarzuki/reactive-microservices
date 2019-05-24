package com.deddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {


    public String airportName;
    public String airportCode;

    public String cityCode;
    public boolean isActive;

}
