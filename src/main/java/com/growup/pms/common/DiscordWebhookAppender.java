package com.growup.pms.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import io.micrometer.common.util.StringUtils;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiscordWebhookAppender extends AppenderBase<ILoggingEvent> {
    private WebhookClient webhookClient;
    private String webhookUrl;
    private String username;
    private String avatarUrl;

    @Override
    public void start() {
        init();
        if (webhookUrl != null) {
            webhookClient = WebhookClient.withUrl(webhookUrl);
            super.start();
        }
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        webhookClient.send(embedBuilder.buildEmbed(eventObject, username, avatarUrl));
    }

    @Override
    public void stop() {
        if (webhookClient != null) {
            webhookClient.close();
        }
        super.stop();
    }

    private void init() {
        // 로그백에서 값과 기본 값이 모두 NULL 이면 문자열 "KEY_IS_UNDEFINED"가 값으로 들어오는 비직관적인 동작을 수행함
        webhookUrl = isDefined("webhookUrl") ? webhookUrl : null;
        avatarUrl = isDefined("avatarUrl") ? webhookUrl : null;
    }

    private boolean isDefined(String key) {
        return !CoreConstants.UNDEFINED_PROPERTY_SUFFIX.equals(key);
    }

    private static class EmbedBuilder {
        private static final int ERROR_COLOR = 0xFF0000;
        private static final int WARN_COLOR = 0xFFA500;
        private static final int DEFAULT_COLOR = 0x000000;

        private static final String MDC_KEY_METHOD = "method";
        private static final String MDC_REQUEST_URI = "requestUri";
        private static final String MDC_KEY_TRACE_ID = "traceId";

        WebhookEmbed buildEmbed(ILoggingEvent eventObject, String username, String avatarUrl) {
            WebhookEmbedBuilder builder = new WebhookEmbedBuilder()
                    .setColor(getColorByLogLevel(eventObject.getLevel()))
                    .addField(new WebhookEmbed.EmbedField(false, "Level", eventObject.getLevel().levelStr))
                    .setTimestamp(Instant.ofEpochMilli(eventObject.getTimeStamp()));

            setAuthor(builder, username, avatarUrl);
            addRequestInfo(builder, eventObject.getMDCPropertyMap());
            addStackTrace(builder, eventObject.getFormattedMessage());
            addTraceId(builder, eventObject.getMDCPropertyMap().get(MDC_KEY_TRACE_ID));

            return builder.build();
        }

        private int getColorByLogLevel(ch.qos.logback.classic.Level level) {
            return switch (level.toInt()) {
                case ch.qos.logback.classic.Level.ERROR_INT -> ERROR_COLOR;
                case ch.qos.logback.classic.Level.WARN_INT -> WARN_COLOR;
                default -> DEFAULT_COLOR;
            };
        }

        private void setAuthor(WebhookEmbedBuilder builder, String username, String avatarUrl) {
            if (username != null) {
                builder.setAuthor(new WebhookEmbed.EmbedAuthor(username, avatarUrl, null));
            }
        }

        private void addTraceId(WebhookEmbedBuilder embedBuilder, String traceId) {
            if (traceId != null) {
                embedBuilder.setFooter(new WebhookEmbed.EmbedFooter(traceId, null));
            }
        }

        private void addRequestInfo(WebhookEmbedBuilder embedBuilder, Map<String, String> mdc) {
            String method = mdc.get(MDC_KEY_METHOD);
            String uri = mdc.get(MDC_REQUEST_URI);
            if (method != null && uri != null) {
                String requestInfo = method + " " + uri;
                embedBuilder.addField(new WebhookEmbed.EmbedField(false, "Request URI", requestInfo));
            }
        }

        private void addStackTrace(WebhookEmbedBuilder embedBuilder, String message) {
            if (StringUtils.isNotEmpty(message)) {
                embedBuilder.addField(new WebhookEmbed.EmbedField(false, "Stack Trace", "```\n" + message + "\n```"));
            }
        }
    }
}
