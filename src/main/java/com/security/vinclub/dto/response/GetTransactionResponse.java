package com.security.vinclub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class GetTransactionResponse {
    @JsonProperty("date")
    private String date;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private String type;
}
