/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import zedly.zbot.YamlConfiguration;
import zedly.zbot.plugin.ZBotPlugin;
import zedly.zbot.self.Self;

/**
 *
 * @author Konstantin
 */
public class Main extends ZBotPlugin {

    public static final List<String> admins = new ArrayList<>();
    private static final ArrayList<String> resourceFileNames = new ArrayList<>();

    public static YamlConfiguration config;
    public static Main instance;
    public static Self self;
    public static Random rnd = new Random();

    @Override
    public void onEnable(Self self) {
        System.out.println("OIIIIII");
        config = getConfig();
        Main.self = self;
        instance = this;
        self.registerEvents(new Watcher());

        admins.clear();
        for (String item : config.getList("admins", String.class)) {
            admins.add(item);
        }
        
    }

    @Override
    public void onDisable() {

    }

}
