package com.hpi.entities.eTrade;

    import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

@Getter
@Setter
public class QuoteResponse
{
    @JsonProperty("QuoteData")
    private List<QuoteData> quoteData;

//    public List<QuoteData> getQuoteData() {
//        return quoteData;
//    }
//
//    public void setQuoteData(List<QuoteData> quoteData) {
//        this.quoteData = quoteData;
//    }
}


