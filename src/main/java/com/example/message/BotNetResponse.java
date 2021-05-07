package com.example.message;

import com.example.message.data.BotNetButton;
import com.example.message.data.BotNetFile;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Farrukh Karimov
 */
public class BotNetResponse {
    private String receiverChatId;
    private String message;
    private List<List<BotNetButton>> buttonsMatrix;
    private List<BotNetFile> filesList;

    public BotNetResponse() {
        buttonsMatrix = new LinkedList<>();
        filesList = new LinkedList<>();
        buttonsMatrix.add(new LinkedList<>());
    }

    /** Method to add button to the last row of buttons */
    public BotNetResponse addButton(@NotNull final BotNetButton botNetButton) {
        buttonsMatrix.get(buttonsMatrix.size() - 1).add(botNetButton);
        return this;
    }

    /** Method to set new row of buttons */
    public BotNetResponse setNewButtonsLine() {
        buttonsMatrix.add(new LinkedList<>());
        return this;
    }

    public void cleanButtons() {
        buttonsMatrix.clear();
        setNewButtonsLine();
    }

    public boolean hasButtons() {
        return getButtonsMatrix().size() > 1 || getButtonsMatrix().get(0).size() > 0;
    }

    public List<List<BotNetButton>> getButtonsMatrix() {
        final List<List<BotNetButton>> result = new LinkedList<>();
        for (List<BotNetButton> buttonsRow : buttonsMatrix ) {
            result.add(new LinkedList<>(buttonsRow));
        }
        return result;
    }

    public String getReceiverChatId() {
        return receiverChatId;
    }

    public void setReceiverChatId(String receiverChatId) {
        this.receiverChatId = receiverChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NotNull final String message) {
        this.message = message;
    }

    public boolean hasFiles() {
        return !filesList.isEmpty();
    }

    public void addFile(@NotNull BotNetFile file) {
        filesList.add(file);
    }

    public void clearFiles() {
        filesList.clear();
    }
}
