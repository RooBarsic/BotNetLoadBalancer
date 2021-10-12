package com.example.api.bots.vk;

import com.example.api.bots.BotRequestListener;
import com.example.api.bots.BotResponseSender;
import com.example.message.BotNetRequest;
import com.example.message.data.BotNetFileType;
import com.example.message.data.BotNetUser;
import com.example.message.data.UiPlatform;
import com.example.servingwebcontent.service.RequestService;
import com.petersamokhin.bots.sdk.callbacks.messages.OnEveryMessageCallback;
import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 *
 *
 * Developers : Farrukh Karimov,
 * Created: 11.02.2021
 * Modified : 11.02.2021
 * */
public class VkBotRequestListener implements BotRequestListener, OnEveryMessageCallback {
    private final int VK_GROUP_ID;
    private final String VK_TOKEN;
    private final Group vkGroup;
    private final RequestService requestService;

    public VkBotRequestListener(final int groupId, @NotNull final String token, RequestService requestService) {
        VK_GROUP_ID = groupId;
        VK_TOKEN = token;
        vkGroup = new Group(VK_GROUP_ID, VK_TOKEN);

        vkGroup.onEveryMessage(this::OnEveryMessage);
        this.requestService = requestService;
    }

    @Override
    public void OnEveryMessage(Message message) {
        try {
            System.out.println("#### new message from VK");
            final BotNetRequest botNetRequest = new BotNetRequest();
            List<String> filesPath = new ArrayList<>();

            // get BotNet user
            final String senderChatId = Integer.toString(message.authorId());

            // set sender user, received message, and receiving UiPlatform
            botNetRequest.setUserChatId(senderChatId);
            botNetRequest.setUiPlatform(UiPlatform.VK);
            botNetRequest.setMessage(message.getText());


            BotNetFileType botNetFileType = BotNetFileType.NON;

            if (message.isDocMessage()) {
                botNetFileType = BotNetFileType.DOCUMENT;
            }
            else if (message.isAudioMessage()) {
                botNetFileType = BotNetFileType.AUDIO;
            }
            else if (message.isGifMessage()) {
                botNetFileType = BotNetFileType.GIF;
            }
            else if (message.isPhotoMessage()) {
                botNetFileType = BotNetFileType.IMAGE;
            }
            else if (message.isStickerMessage()) {
                botNetFileType = BotNetFileType.STICKER;
            }
            else if (message.isVoiceMessage()) {
                botNetFileType = BotNetFileType.VOICE;
            }
//            else if (message.isLinkMessage()) {
//                //botNetFileType = BotNetFileType.DOCUMENT;
//                // implementation not finished yet
//                //VkUtils.downloadLinkMessage(message.getAttachments());
//                botNetRequest.setReceiverUser(senderUser);
//                botNetRequest.setResponsePlatform(UiPlatform.VK);
//                botNetRequest.setResponseMessage("Sorry. Can't handle that type of requests yet. Working on it.");
//                return;
//            }
//            else if (message.isVideoMessage()) {
//                botNetFileType = BotNetFileType.VIDEO;
//                // implementation not finished yet
//                //VkUtils.downloadVideo(message.getAttachments());
//                botNetRequest.setReceiverUser(senderUser);
//                botNetRequest.setResponsePlatform(UiPlatform.VK);
//                botNetRequest.setResponseMessage("please send video as a file");
//                boxQueue.addLast(botNetRequest);
//                return;
//            }
//            else if (message.isWallMessage()) {
//                //botNetFileType = BotNetFileType.DOCUMENT;
//                // implementation not finished yet
//                //final JSONArray attachments = message.getAttachments();
//                botNetRequest.setReceiverUser(senderUser);
//                botNetRequest.setResponsePlatform(UiPlatform.VK);
//                botNetRequest.setResponseMessage("Sorry. Can't handle that type of requests yet. Working on it.");
//                return;
//            }
//
//            final JSONArray attachments = message.getAttachments();
//            for (int i = 0; i < attachments.length(); i++) {
//                final JSONObject attachment = attachments.getJSONObject(i);
//                BotNetFile botNetFile = null;
//                switch (botNetFileType) {
//                    case IMAGE :
//                        botNetFile = VkUtils.downloadPhoto(attachment);
//                        break;
//                    case GIF :
//                        botNetFile = VkUtils.downloadGif(attachment);
//                        break;
//                    case VIDEO_NOTE:
//                    case VIDEO :
//                        //botNetFile = VkUtils.downloadVideo(attachment);
//                        break;
//                    case AUDIO :
//                        botNetFile = VkUtils.downloadAudio(attachment);
//                        break;
//                    case VOICE :
//                        botNetFile = VkUtils.downloadVoice(attachment);
//                        break;
//                    case DOCUMENT :
//                        botNetFile = VkUtils.downloadDocument(attachment);
//                        break;
//                    case STICKER :
//                        break;
//                }
//                if ((botNetFile != null) && (botNetFile.isFileOk())) {
//                    //add file to box
//                    botNetRequest.addFile(botNetFile);
//                }
//            }

            // add filled box to the processing queue

            requestService.addRequestToProcessingQueue(botNetRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public BotResponseSender getBotResponseSender() {
        return new VkBotRequestSender(vkGroup);
    }
}
