package konni.konniskot;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskGoldfarm extends Task {

    private static final Location home = new Location(295, 137, -8702).centerHorizontally();
    private static final Location BUTTON_LOC = new Location(293, 138, -8702).centerHorizontally();
    private static final Location PLAYER_FUEL_LOC = new Location(295.5, 137, -8704.5);
    private static final Location BACKUP_LOC = new Location(295, 137, -8699);

    private static final Location TRASH_CHEST_LOC = new Location(298, 137, -8701).centerHorizontally();
    private static final Location TRASH_WALK_LOC = new Location(299, 137, -8701).centerHorizontally();

    private static final Location SHULKER_WALK = new Location(290, 137, -8721).centerHorizontally();
    private static final Location BUTTON_SHULKER_LOC = new Location(289, 137, -8721).centerHorizontally();
    private static final Location SHULKER_OUTPUT_LOC = new Location(290, 138, -8723).centerHorizontally();
    private static final Location NUGGET_TESSERACT_WALK = new Location(299, 137, -8713).centerHorizontally();
    private static final Location NUGGET_TESSERACT_LOC = new Location(300, 138, -8713).centerHorizontally();

    private static final Location GOLD_INGOT_CHEST_WALK = new Location(298, 133, -8716).centerHorizontally();
    private static final Location GOLD_INGOT_CHEST_LOC = new Location(298, 132, -8716).centerHorizontally();
    private static final Location GOLD_INGOT_TESS_WALK = new Location(299, 137, -8714).centerHorizontally();
    private static final Location GOLD_INGOT_TESS_LOC = new Location(300, 138, -8714).centerHorizontally();

    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();
    private static boolean playerPresent = true;
    private static boolean grinding = false;
    private static boolean shulkerempty = false;
    private static boolean daily = true;
    private static int nuggettransfer = 0;
    private static int goldingottransfer = 0;
    public TaskGoldfarm() {
        super(100);
    }

    public void run() {

        int grinds = Main.config.getInt("grinds", 0);
        try {

            while (daily) {
                int grd = 0;

                grinding = true;
                ai.tick();
                Main.self.selectSlot(1);
                if (Main.self.getLocation().distanceTo(home) > 0.1) {
                    ai.moveTo(home);
                }
                while (grinding) {
                    grindpigmen();
                    grd++;
                    System.out.println(grd);

                    if (grd >= grinds) {
                        grinding = false;
                        System.out.println("done grinding");
                    }
                }
                ai.tick(2);
                //Trashcan start
                ai.moveTo(TRASH_WALK_LOC);
                ai.tick(20);
                System.out.println("wait done, deposit trash");
                dumpTrash();
                shulkerempty = true;
                //Trashcan end

                ai.tick();
                ai.moveTo(SHULKER_WALK);
                ai.tick(2);
                checkshulkers();

                while (shulkerempty) {
                    shulkers();
                    ai.tick();
                    System.out.println(nuggettransfer);
                }
                ai.tick();
                serviceoutput();
                ai.tick();
                Main.self.sendChat("/msg ImakeYourBedrock " + nuggettransfer);
            }

        } catch (InterruptedException ehmm) {
        }
        unregister();

    }

    public void cancel() {
        daily = false;
        grinding = false;
        shulkerempty = false;
    }

    public void serviceoutput() throws InterruptedException {
        ai.moveTo(GOLD_INGOT_CHEST_WALK);
        ai.tick(3);
        ai.openContainer(GOLD_INGOT_CHEST_LOC);
        ai.tick(5);
        emptyChest();
        ai.tick();
        ai.closeContainer();
        ai.tick(3);
        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(GOLD_INGOT_TESS_WALK);
        ai.tick();
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(GOLD_INGOT_TESS_LOC, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);

    }

    public void checkshulkers() throws InterruptedException {
        System.out.println("checking shulkers...");
        if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() != Material.SHULKER_BOX) {
            System.out.println("shulker not found");
            ai.tick(5);
            System.out.println("press button");
            Main.self.placeBlock(BUTTON_SHULKER_LOC, BlockFace.EAST);
            ai.tick(20);
        } else {
            System.out.println("shulker avaliable");
        }
    }

    public void shulkers() throws InterruptedException {
        ai.tick();
        ai.moveTo(SHULKER_WALK);
        ai.tick();
        if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() == Material.SHULKER_BOX) {
            ai.openContainer(SHULKER_OUTPUT_LOC);
            ai.tick();
            if (InventoryUtil.count(Material.GOLDEN_SWORD, false, true) > 0 || InventoryUtil.count(Material.GOLD_INGOT, false, true) > 0 || InventoryUtil.count(Material.FEATHER, false, true) > 0) {
                System.out.println("ERROR");
                Main.self.sendChat("/msg Konni999 Error at the Shulkeroutput, ding konni");
            }
            ai.tick();
            emptyChest();

            ai.tick(10);
            ai.closeContainer();
            ai.tick();
            nuggettransfer = nuggettransfer + InventoryUtil.count(Material.GOLD_NUGGET, true, false);
            ai.tick();
            ai.moveTo(NUGGET_TESSERACT_WALK);
            ai.tick(0);
            Main.self.sneak(true);
            ai.tick();
            Main.self.placeBlock(NUGGET_TESSERACT_LOC, BlockFace.EAST);
            ai.tick();
            Main.self.sneak(false);
        } else {
            System.out.println("no shulker found");

            shulkerempty = false;
        }

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

    private void grind() throws InterruptedException {

        ai.tick();
        tryAttack(home, home);
        //System.out.println("hit");
        //System.out.println("press button");
        ai.tick(3);
        Main.self.placeBlock(BUTTON_LOC, BlockFace.EAST);
        ai.tick(5);
    }

    private boolean dumpTrash() throws InterruptedException {
        if (InventoryUtil.findItem((i) -> i != null && TRASH_MATERIALS.contains(i.getType())) == -1) {
            return true;
        }

        if (!ai.openContainer(TRASH_CHEST_LOC)) {
            System.err.println("Can't open disposal");
            ai.tick(50);
            return false;
        }

        int staticOffset = Main.self.getInventory().getStaticOffset();
        boolean hasTrash;
        do {
            hasTrash = false;
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                if (Main.self.getInventory().getSlot(i) != null
                        && TRASH_MATERIALS.contains(Main.self.getInventory().getSlot(i).getType())) {
                    ai.depositSlot(i);
                    hasTrash = true;
                }
            }
        } while (hasTrash);

        ai.closeContainer();

        return true;
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

    private void grindpigmen() throws InterruptedException {
        try {
            //Main.self.sendChat("Don't forget to turn on the deadman-switch-system!");

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

        } catch (InterruptedException ehmm) {
        }
    }

    static {
        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
    }
}
