/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Konstantin
 */
class CommandProcessor {

    private static TaskGoldcraften goldcraft;
    private static TaskGrindpigmen pigmentask;
    private static TaskShulkerleeren shulkerrun;
    private static TaskGoldfarm goldfarm;
    private static final File scrambleComplimentFile = new File(Main.instance.getDataFolder(), "compliments.txt");
    private static final ArrayList<String> compliments = new ArrayList<>();

    static void onCommand(String user, String command, boolean pm) throws Exception {
        String[] args = command.split(" ");
        loadComplimentsDictionary();

        switch (args[0]) {

            case "hallo":
                Main.self.sendChat("Hallo " + user + ", nice to meet you!");
                break;

            case "Test":
                Main.self.sendChat("This is Konnis first programming session");
                break;

            case "mario":
                Main.self.sendChat("everything is tested");
                break;

            case "lil":
                Main.self.sendChat("give me dah weed!");
                break;
                
            case "compliment":
                String comp = compliments.get(Main.rnd.nextInt(compliments.size()));
                Main.self.sendChat(comp);
                break;
                
            case "afk":
                Main.self.sendChat("/afk");
                break;

        }
        if (!Main.admins.contains(user)) {
            return;

        }
        switch (args[0]) {

            case "1":
                Main.self.sendChat("1");
                break;
                
            case "trash":
                new TaskTrashcan().start();
                break;

            case "xp":
                Main.self.sendChat("/home xp");
                break;
                
            case "testt":
                new TaskFixfarm().start();
                break;

            case "grind":
                pigmentask = new TaskGrindpigmen();
                pigmentask.start();
                break;

            case "stopgrind":
                if (pigmentask != null) {
                    pigmentask.cancel();
                    System.out.println("stopped TaskGrindpigmen");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;

            case "daily":
                shulkerrun = new TaskShulkerleeren();
                shulkerrun.start();
                break;

            case "button":
                new TaskButtonpress().start();
                break;
                
            case "gold":
                goldfarm = new TaskGoldfarm();
                goldfarm.start();
                break;
                
            case "gold_stop":
                if (goldfarm != null) {
                    goldfarm.cancel();
                    System.out.println("stopped TaskGoldfarm");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;

            case "craft_gold":
                goldcraft = new TaskGoldcraften();
                goldcraft.start();
                Main.self.sendChat("crafting goldingots");
                break;

            case "craft_gold_stop":
                if (goldcraft != null) {
                    goldcraft.cancel();
                    System.out.println("stopped TaskGoldcraften");
                } else {
                    System.out.println("Task läuft nicht");
                }
                break;
        }
    }

    private static void loadComplimentsDictionary() {
        try (BufferedReader br = new BufferedReader(new FileReader(scrambleComplimentFile))) {
            String t;
            while ((t = br.readLine()) != null) {
                compliments.add(t);
            }
            br.close();
        } catch (IOException ex) {
        }
    }
}
