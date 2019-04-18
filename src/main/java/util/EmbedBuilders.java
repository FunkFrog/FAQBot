package util;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

public class EmbedBuilders {
    public static EmbedBuilder permissionCheckFailed() {
        return new EmbedBuilder()
                .setTitle("Sorry, you don't have the permissions to perform that action!")
                .setColor(Color.RED)
                .setFooter("FAQBot", Tokens.ICON);
    }

    public static EmbedBuilder failEmbed(String err) {
        return new EmbedBuilder()
                .setTitle(err)
                .setColor(Color.RED)
                .setFooter("FAQBot", Tokens.ICON);
    }

    public static EmbedBuilder successEmbed(String succ) {
        return new EmbedBuilder()
                .setTitle(succ)
                .setColor(Tokens.USER_EMBED_COLOR)
                .setFooter("FAQBot", Tokens.ICON);
    }

    public static EmbedBuilder userFAQEmbed(String FAQ, String message) {
        return new EmbedBuilder()
                .setTitle("FAQ: " + FAQ.toUpperCase())
                .setDescription(message)
                .setFooter("FAQBot", Tokens.ICON)
                .setColor(Tokens.USER_EMBED_COLOR);
    }

    public static EmbedBuilder imageFAQEmbed(String command, String image) {
        return new EmbedBuilder()
                .setTitle("Official FAQ: " + command.toUpperCase())
                .setImage(image)
                .setFooter("FAQBot", Tokens.ICON)
                .setColor(Tokens.SYS_EMBED_COLOR);
    }

    public static EmbedBuilder sysFAQEmbed(String command, String message) {
        return new EmbedBuilder()
                .setTitle("Official FAQ: " + command.toUpperCase())
                .setDescription(message)
                .setFooter("FAQBot", Tokens.ICON)
                .setColor(Tokens.SYS_EMBED_COLOR);
    }
}
