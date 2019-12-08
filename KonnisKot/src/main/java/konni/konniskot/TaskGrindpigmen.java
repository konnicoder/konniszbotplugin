/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskGrindpigmen extends Task {

    private static final Location home = new Location(295, 137, -8702).centerHorizontally();
    private static final Location BUTTON_LOC = new Location(293, 138, -8702).centerHorizontally();
    private static final Location PLAYER_FUEL_LOC = new Location(295.5, 137, -8704.5);
    private static final Location BACKUP_LOC = new Location(295, 137, -8699);
    private static boolean playerPresent = false;
    private static boolean shouldrun = true;

    public TaskGrindpigmen() {
        super(100);
    }

    public void run() {

        try {
            
            Main.self.sendChat("Don't forget to turn on the deadman-switch-system!");
            ai.moveTo(home);
            
            while (shouldrun) {
                testPlayerPresent();
                ai.tick();
                if (playerPresent) {
                    System.out.println("player present");

                    if (Main.self.getLocation().distanceTo(BACKUP_LOC) > 0.1) {
                        Main.self.sendChat("I can see you!");
                        tryAttack(home, home);
                        ai.tick(40);
                        tryAttack(home, home);
                        ai.tick();
                        ai.moveTo(BACKUP_LOC);
                        Main.self.sendChat("Let me make some space for you!");
                        System.out.println("backing up");
                    }

                } else {
                    if (Main.self.getLocation().distanceTo(home) > 0.1) {
                        ai.moveTo(home);
                        System.out.println("komme zurück");
                    }
                    grind();
                }

            }

        } catch (InterruptedException ehmm) {

        }
        unregister();
    }

    public static boolean testPlayerPresent() {
        try {

            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.PLAYER) {
                    continue;
                }
                if (ent.getLocation().distanceTo(PLAYER_FUEL_LOC) < 3) {
                    playerPresent = true;
                    return true;
                }
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
            return false;
        }
        playerPresent = false;
        return true;
    }

    private boolean tryAttack(Location home, Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {
            ai.moveTo(home);
            ai.tick();
            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    public void cancel() {
        shouldrun = false;
    }

    private boolean grind() throws InterruptedException {
        ai.tick();
        tryAttack(home, home);
        System.out.println("hit");
        System.out.println("press button");
        ai.tick(3);
        Main.self.placeBlock(BUTTON_LOC, BlockFace.EAST);
        ai.tick(5);
        return false;
    }
    
    private boolean emptyChest() throws InterruptedException {
        for (int i = 0; i < Main.self.getInventory().getStaticOffset(); i++) {
            if (InventoryUtil.findFreeStorageSlot(true) == -1) {
                return true;
            }
            if (Main.self.getInventory().getSlot(i) != null) {
                ai.withdrawSlot(i);
            }
        }
        return false;

    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.CHICKEN
                        && ent.getType() != EntityType.PIG_ZOMBIE) {
                    continue;
                }
                if (ent.getLocation().distanceTo(attackLoc) < 4) {
                    annoyingEntities++;
                    if (nearestAnnoying == null
                            || ent.getLocation().distanceTo(attackLoc)
                            < nearestAnnoying.getLocation().distanceTo(attackLoc)) {
                        nearestAnnoying = ent;
                    }
                }
            }
            if (nearestAnnoying != null) {
                return nearestAnnoying;
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
        }
        return null;
    }

}
