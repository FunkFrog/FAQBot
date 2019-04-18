import database.SysFAQ;
import database.UserFAQ;
import util.EmbedBuilders;
import util.Init;

import java.util.Optional;

class FAQListener {
    static void addFAQListener() {
        Init.api.addMessageCreateListener(message -> {
            try {
                String msg = message.getReadableMessageContent();
                if (msg.isEmpty() || msg.length() < 5) return;

                if (msg.substring(0, 4).equalsIgnoreCase("FAQ!")) {
                    String command = msg.substring(4).toLowerCase();
                    Optional<String> userFAQ = UserFAQ.getFAQMessage(command);
                    Optional<String> sysFAQ = SysFAQ.getSysFAQMessage(command);

                    if (userFAQ.isPresent()) {
                        message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(
                                EmbedBuilders.userFAQEmbed(command, userFAQ.get())));

                    } else if (sysFAQ.isPresent()) {
                        if (SysFAQ.isImageEmbed(command)) {
                            Optional<String> image = SysFAQ.getFAQImage(command);

                            image.ifPresent(s -> message.getServerTextChannel()
                                    .ifPresent(ch -> ch.sendMessage(
                                            EmbedBuilders.imageFAQEmbed(command, image.get()))));

                        } else {
                            message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(
                                    EmbedBuilders.sysFAQEmbed(command, sysFAQ.get())));

                        }
                    } else {
                        message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(
                                EmbedBuilders.failEmbed("Could not find that FAQ!")));

                    }
                }
            } catch (Exception e) {
                System.out.println("Exception handling message.");
                e.printStackTrace();
            }
        });
    }
}
