package com.example.servingwebcontent.service.logic2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CardImpl {
    public static String DEFAULT_WRONG_MATCH_TEXT = "I can't understand you.";

    @JsonProperty("text")
    private String text;
    @JsonProperty("errorText")
    private String errorText = null;
    @JsonProperty("buttons")
    private List<ButtonImpl> buttons;

    CardImpl(String text, String wrongMatchText, List<ButtonImpl> buttons) {
        this.text = text;
        this.errorText = wrongMatchText;
        this.buttons = buttons;
    }

    CardImpl(String text, List<ButtonImpl> buttons) {
        this(text, null, buttons);
    }

    public boolean isList() {
        return buttons == null || buttons.size() == 0;
    }

    public String getText() {
        return text;
    }

    public String getErrorText() {
        return errorText != null ? errorText : DEFAULT_WRONG_MATCH_TEXT;
    }

    public ButtonImpl getButtonByText(String buttonText) {
        for (ButtonImpl button : buttons) {
            if (button.getText().equals(buttonText)) {
                return button;
            }
        }
        return null;
    }

    public List<ButtonImpl> getButtons() {
        return new ArrayList<>(buttons);
    }
}

