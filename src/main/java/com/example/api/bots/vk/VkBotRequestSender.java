package com.example.api.bots.vk;

import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetResponse;
import com.example.message.data.BotNetFile;
import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import org.jetbrains.annotations.NotNull;


/**
 *
 *
 * Developers : Farrukh Karimov,
 * Created: 11.02.2021
 * Modified : 11.02.2021
 * */
public class VkBotRequestSender implements BotResponseSender {
    private final Group vkGroup;

    VkBotRequestSender(@NotNull final Group vkGroup) {
        this.vkGroup = vkGroup;
    }

    @Override
    public boolean sendBotNetResponse(@NotNull BotNetResponse response) {
        final String receiverChatId = response.getReceiverChatId();
        final String responseText = response.getMessage();

        // send files if has some
//        if (response.hasFiles()) {
//            List<BotNetFile> filesList = response.getFilesList();
//            for (BotNetFile file : filesList) {
//                sendBotNetFile(receiverChatId, file);
//            }
//        }

        if ((response.getMessage().length() == 0) && (response.hasButtons() == false)) {
            return true;
        }

        final Message message = new Message();

        message.from(vkGroup);
        message.to(Integer.parseInt(receiverChatId));
        message.text(responseText);

        // add buttons if has some
        if (response.hasButtons()) {

        }

        // send response
        message.send();

        return true;
    }

    @Override
    public boolean sendBotNetFile(@NotNull final String chatId, @NotNull final BotNetFile botNetFile) {
        try {
            final Message response = new Message();
            response.from(vkGroup);
            response.to(Integer.parseInt(chatId));
            switch (botNetFile.getType()) {
                case IMAGE :
                case GIF :
                    response.photo(botNetFile.getFullPath());
                case VOICE :
                    response.sendVoiceMessage(botNetFile.getFullPath());
                case VIDEO_NOTE:
                case VIDEO :
                case AUDIO :
                case DOCUMENT :
                    response.doc(botNetFile.getFullPath());
            }
            response.send();
            System.out.println(" Attachment type=" + botNetFile.getType() + " was sent" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
