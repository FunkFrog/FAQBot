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
                .setColor(Tokens.EMBED_COLOR)
                .setFooter("FAQBot", Tokens.ICON);
    }
}
