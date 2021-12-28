package com.example.servingwebcontent.service.logic2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ButtonImpl {
    @JsonProperty("text")
    private String text;
    @JsonProperty("card")
    private CardImpl card;

    ButtonImpl(String text, CardImpl card) {
        this.text = text;
        this.card = card;
    }
}
