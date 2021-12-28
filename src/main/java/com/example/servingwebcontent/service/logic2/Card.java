package com.example.servingwebcontent.service.logic2;

import java.util.List;

public interface Card {

    boolean isList();
    String getText();
    String getErrorText();
    Button getButtonByText(String buttonText);
    List<Button> getButtons();
}
