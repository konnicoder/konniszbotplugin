/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zedly.zbot.event.ChatEvent;
import zedly.zbot.event.EventHandler;
import zedly.zbot.event.Listener;

/**
 *
 * @author Konstantin
 */
public class Watcher implements Listener {

    private final Pattern p = Pattern.compile("^.*<(.*?)> (.*)$");
    private final Pattern pmp = Pattern.compile("^\\[.*<(.*?)> -> Me\\] (.*)$");
    private final Pattern cp;
    private final String rp;

    public Watcher() {
        cp = Pattern.compile("^" + Main.config.getString("prefix", "zb") + " (.+)");
        rp = "\\[.+\\] ";
    }

    @EventHandler
    public void onChat(ChatEvent evt) {
        Matcher m = p.matcher(evt.getMessage());
        if (m.find()) {
            String user = m.group(1).replaceAll(rp, "");
            String message = m.group(2);

            Matcher cm = cp.matcher(message);
            if (cm.find()) {
                try {
                    CommandProcessor.onCommand(user,cm.group(1), false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Main.self.sendChat("An internal error occurred: " + ex.getClass());
                }
                return;
            }
        }
        m = pmp.matcher(evt.getMessage());
        if (m.find()) {
            String user = m.group(1).replaceAll(rp, "");
            String message = m.group(2);
            System.out.println("PM: " + message);
            try {
                CommandProcessor.onCommand(user, message, true);
            } catch (Exception ex) {
                ex.printStackTrace();
                Main.self.sendChat("/r An internal error occurred: " + ex.getClass());
            }
            return;
        }
    }
}
