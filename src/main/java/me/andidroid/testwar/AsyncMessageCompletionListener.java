package me.andidroid.testwar;

import javax.jms.CompletionListener;
import javax.jms.Message;

public final class AsyncMessageCompletionListener implements CompletionListener
{
    /**
     * Logging via slf4j api
     */
    static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AsyncMessageCompletionListener.class);
    
    @Override
    public void onException(Message message, Exception exception)
    {
        LOGGER.error("error sending message", exception);
    }
    
    @Override
    public void onCompletion(Message message)
    {
        LOGGER.info("message send successfully {}", message);
    }
}
