package com.hpi.entities.eTrade;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter
public class Root
{
    @JsonProperty("QuoteResponse")
    private QuoteResponse quoteResponse;
}
