package com.example.api.bots.telegram;

import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetButton;
import com.example.message.data.BotNetFile;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for sending messages from Telegram bot to Telegram user
 *
 * @author Farrukh Karimov
 */
public class TelegramBotResponseSender extends DefaultAbsSender implements BotResponseSender {
    protected final String BOT_TOKEN;

    public TelegramBotResponseSender(@NotNull final DefaultBotOptions options, @NotNull final String BOT_TOKEN) {
        super(options);
        this.BOT_TOKEN = BOT_TOKEN;
    }

    @Override
    public boolean sendBotNetResponse(@NotNull final BotNetResponse botNetResponse) {
        final String receiverChatId = botNetResponse.getReceiverChatId();
        final String responseText = botNetResponse.getMessage();

        // send files if has some

        final SendMessage telegramResponseMessage = new SendMessage();
        telegramResponseMessage.setChatId(receiverChatId);
        telegramResponseMessage.setText(responseText);

        // add buttons if has some
        if (botNetResponse.hasButtons()) {
            if (botNetResponse.isInlineButtons()) {
                final List<List<BotNetButton>> buttonsMatrix = botNetResponse.getButtonsMatrix();
                final List<KeyboardRow> keyboardRowList = new ArrayList<>();
                for (final List<BotNetButton> buttonsInRow : buttonsMatrix) {
                    final KeyboardRow keyboardRow = new KeyboardRow();

                    for (final BotNetButton botNetButton : buttonsInRow) {
                        keyboardRow.add(botNetButton.getButtonHiddenText());
                    }
                    keyboardRowList.add(keyboardRow);
                }

                telegramResponseMessage.setReplyMarkup(
                        new ReplyKeyboardMarkup()
                                .setKeyboard(keyboardRowList)
                                .setResizeKeyboard(true)
                );
            } else {
                final List<List<BotNetButton>> buttonsMatrix = botNetResponse.getButtonsMatrix();
                final List<List<InlineKeyboardButton>> telegramButtonsMatrix = new LinkedList<>();

                for (final List<BotNetButton> buttonsInRow : buttonsMatrix) {
                    telegramButtonsMatrix.add(new LinkedList<>());

                    for (final BotNetButton botNetButton : buttonsInRow) {

                        final InlineKeyboardButton telegramKeyboardButton = new InlineKeyboardButton();
                        telegramKeyboardButton.setText(botNetButton.getButtonText());
                        telegramKeyboardButton.setCallbackData(botNetButton.getButtonHiddenText());

                        telegramButtonsMatrix
                                .get(telegramButtonsMatrix.size() - 1)
                                .add(telegramKeyboardButton);
                    }
                }
                telegramResponseMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(telegramButtonsMatrix));
            }
        }

        // send response
        try {
            execute(telegramResponseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean sendBotNetFile(@NotNull final String chatId, @NotNull final BotNetFile botNetFile) {
        try {
            switch (botNetFile.getType()) {
                case IMAGE:
                    final SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);
                    sendPhoto.setNewPhoto(new File(botNetFile.getFullPath()));
                    sendPhoto(sendPhoto);
                    break;
                case VOICE:
                    final SendVoice sendVoice = new SendVoice();
                    sendVoice.setChatId(chatId);
                    sendVoice.setNewVoice(new File(botNetFile.getFullPath()));
                    sendVoice(sendVoice);
                    break;
                case AUDIO:
                    final SendAudio sendAudio = new SendAudio();
                    sendAudio.setChatId(chatId);
                    sendAudio.setNewAudio(new File(botNetFile.getFullPath()));
                    sendAudio(sendAudio);
                    break;
                case GIF:
                    File file = new File(botNetFile.getFullPath());
                    File destFile = new File(botNetFile.getLocalStoragePath() + "mp4.mp4");
                    file.renameTo(destFile);
                    botNetFile.setFileName("mp4.mp4");
                    System.out.println("Telegram bot ::: renamed file to mp4.mp4");
                case VIDEO:
                    final SendVideo sendVideo = new SendVideo();
                    sendVideo.setChatId(chatId);
                    sendVideo.setNewVideo(new File(botNetFile.getFullPath()));
                    sendVideo(sendVideo);
                    break;
                case VIDEO_NOTE:
                    final SendVideoNote sendVideoNote = new SendVideoNote();
                    sendVideoNote.setChatId(chatId);
                    sendVideoNote.setNewVideoNote(new File(botNetFile.getFullPath()));
                    sendVideoNote(sendVideoNote);
                    break;
                case DOCUMENT:
                    final SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(chatId);
                    sendDocument.setNewDocument(new File(botNetFile.getFullPath()));
                    sendDocument(sendDocument);
                    break;
                case STICKER:
                    final SendSticker sendSticker = new SendSticker();
                    sendSticker.setChatId(chatId);
                    sendSticker.setNewSticker(new File(botNetFile.getFullPath()));
                    sendSticker(sendSticker);
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
