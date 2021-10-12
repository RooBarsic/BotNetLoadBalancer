package com.example.api.bots.agent;


import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetRequest;
import com.example.message.data.UiPlatform;
import com.example.servingwebcontent.service.RequestService;
import org.jetbrains.annotations.NotNull;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.fetcher.OnEventFetchListener;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;

import java.util.List;

/**
 *
 *
 * Developers : Farrukh Karimov,
 * Created: 11.02.2021
 * Modified : 11.02.2021
 * */
public class MailRuAgentBotRequestListener implements BotRequestListener,OnEventFetchListener {
    private final BotApiClient apiClient;
    private final BotApiClientController apiClientController;
    private final String TOKEN;
    private final RequestService requestService;

    public MailRuAgentBotRequestListener(@NotNull final String token,
                                         RequestService requestService) {
        TOKEN = token;
        apiClient = new BotApiClient(TOKEN);
        apiClientController = BotApiClientController.startBot(apiClient);
        this.requestService = requestService;
    }

    /** Method for starting MailRuAgentBot */
    public void startBot() {
        apiClient.addOnEventFetchListener(this);
    }
    @Override
    public void onEventFetch(List<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            System.out.println(" Mail_Ru_Agent -----" + i);
            final Event event = events.get(i);
            final BotNetRequest botNetRequest = new BotNetRequest();

            if (event instanceof NewMessageEvent) {
                final NewMessageEvent newMessageEvent = (NewMessageEvent) events.get(i);

                // set sender user, received message, and receiving UiPlatform
                botNetRequest.setUserChatId(newMessageEvent.getChat().getChatId());
                botNetRequest.setUiPlatform(UiPlatform.MAIL_RU_AGENT);
                botNetRequest.setMessage(newMessageEvent.getText());

                System.out.println("MailRu :::: message = " + botNetRequest.getMessage());
                //TODO: when user users gifs from "fmedia.giphy.com", we receiving the link to the gif instead of that gif file
                //TODO: already tried to download, but we wasn't able to play the downloaded file at all

                // add attachments if has some
                if ((newMessageEvent.getParts() != null) && (newMessageEvent.getParts().size() > 0)) {
                    //TODO: assume that if request has attachment than it doesn't have any text message
                    //TODO: When user sends attachment the newMessageEvent.getText() - would contain the link for sharing/loading that attachment
//                    botNetRequest.setReceivedMessage("");
//                    final List<Part> parts = newMessageEvent.getParts();
//                    for (final Part part : parts) {
//                        final BotNetFile botNetFile;
//
//                        if (part instanceof File) {
//                            final File file = (File) part;
//                            botNetFile = MailRuUtils.loadAttachmentToBotNetFile(file.getFileId(), TOKEN, "mail/file/");
//                        }
//                        else if (part instanceof Voice) {
//                            final Voice voice = (Voice) part;
//                            botNetFile = MailRuUtils.loadAttachmentToBotNetFile(voice.getFileId(), TOKEN, "mail/voice/");
//                            botNetFile.setType(BotNetFileType.VOICE);
//                        }
//                        else if (part instanceof Sticker) {
//                            final Sticker sticker = (Sticker) part;
//                            botNetFile = MailRuUtils.loadAttachmentToBotNetFile(sticker.getFileId(), TOKEN, "mail/sticker/");
//                            botNetFile.setType(BotNetFileType.IMAGE);
//                        } else {
//                            continue;
//                        }
//
//                        if (botNetFile.isFileOk()) {
//                            //add file to box
//                            botNetRequest.addFile(botNetFile);
//                        }
//                    }
                }
            }
            else if (event instanceof CallbackQueryEvent) {
                final CallbackQueryEvent callbackQueryEvent = (CallbackQueryEvent) events.get(i);

                // set sender user, received message, and receiving UiPlatform
                botNetRequest.setUserChatId(callbackQueryEvent.getMessageChat().getChatId());
                botNetRequest.setUiPlatform(UiPlatform.MAIL_RU_AGENT);
                botNetRequest.setMessage(callbackQueryEvent.getCallbackData());
            }
            else {
                continue;
            }

            // add filled box to the processing queue
            requestService.addRequestToProcessingQueue(botNetRequest);
        }

    }

    @Override
    public BotResponseSender getBotResponseSender() {
        return new MailRuAgentBotRequestSender(apiClientController);
    }
}


