package com.github.mahdim1000.exceptionhandling.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utility class for resolving internationalized messages.
 * Provides a simple interface for message resolution with locale support.
 */
@Component
public class MessageResolver {
    
    private final MessageSource messageSource;

    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Resolve message for the current locale context.
     */
    public String resolve(String messageKey) {
        return resolve(messageKey, null, LocaleContextHolder.getLocale());
    }

    /**
     * Resolve message with arguments for the current locale context.
     */
    public String resolve(String messageKey, Object[] args) {
        return resolve(messageKey, args, LocaleContextHolder.getLocale());
    }

    /**
     * Resolve message for a specific locale.
     */
    public String resolve(String messageKey, Locale locale) {
        return resolve(messageKey, null, locale);
    }

    /**
     * Resolve message with arguments for a specific locale.
     */
    public String resolve(String messageKey, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(messageKey, args, locale);
        } catch (Exception e) {
            // Fallback to the message key itself if resolution fails
            return messageKey;
        }
    }

    /**
     * Resolve message with default fallback.
     */
    public String resolve(String messageKey, String defaultMessage) {
        return resolve(messageKey, null, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * Resolve message with arguments and default fallback.
     */
    public String resolve(String messageKey, Object[] args, String defaultMessage, Locale locale) {
        return messageSource.getMessage(messageKey, args, defaultMessage, locale);
    }
}
