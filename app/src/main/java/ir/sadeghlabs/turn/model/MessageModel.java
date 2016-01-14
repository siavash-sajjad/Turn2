package ir.sadeghlabs.turn.model;

/**
 * Created by Siavash on 12/10/2015.
 */
public class MessageModel {
    private int messageId;
    private String title;
    private String messageText;
    private long messageDateTime;
    private boolean enable;


    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(long messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
