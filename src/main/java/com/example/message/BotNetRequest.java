package com.example.message;

import com.example.message.data.UiPlatform;
import org.jetbrains.annotations.NotNull;


/**
 *
 *
 * @author Farrukh Karimov
 */

public class BotNetRequest {
    private String userChatId;
    private String message;
    private UiPlatform uiPlatform;

    public BotNetRequest(@NotNull final String userChatId,
                         @NotNull final String message,
                         @NotNull final UiPlatform uiPlatform) {
        this.message = message;
        this.userChatId = userChatId;
        this.uiPlatform = uiPlatform;
    }

    public BotNetRequest() {
        this.message = "null";
        this.userChatId = "null";
        this.uiPlatform = UiPlatform.DEFAULT;
    }


    public String getUserChatId() {
        return userChatId;
    }

    public void setUserChatId(String userChatId) {
        this.userChatId = userChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UiPlatform getUiPlatform() {
        return uiPlatform;
    }

    public void setUiPlatform(UiPlatform uiPlatform) {
        this.uiPlatform = uiPlatform;
    }
}
