/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskShulkerleeren extends Task {

    private static final Location workspot1 = new Location(290, 137, -8721).centerHorizontally();
    private static final Location shulkeroutput = new Location(290, 138, -8723).centerHorizontally();
    private static final Location NUGGET_TESSERACT_WALK = new Location(299, 137, -8713).centerHorizontally();
    private static final Location NUGGET_TESSERACT_LOC = new Location(300, 138, -8713).centerHorizontally();
    private static final Location BUTTON_SHULKER_LOC = new Location(289, 137, -8721).centerHorizontally();  
    private boolean shulkerrun = true;

    public TaskShulkerleeren() {
        super(100);
    }

    public void run() {
        try {
            int x = 0;
            ai.tick();
            ai.moveTo(workspot1);
            ai.tick();
            if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() != Material.SHULKER_BOX) {
                System.out.println("Angfangstest: Shulker nicht vorhanden");
                ai.tick();
                Main.self.sendChat("press button");
                Main.self.placeBlock(BUTTON_SHULKER_LOC, BlockFace.EAST);
                ai.tick(10);
            }

            while (shulkerrun) {
                ai.tick();
                ai.moveTo(workspot1);
                ai.tick();
                if (Main.self.getEnvironment().getBlockAt(290, 138, -8723).getType() == Material.SHULKER_BOX) {
                    System.out.println("shulker vorhanden " + x);
                    ai.openContainer(shulkeroutput);
                    ai.tick();
                    emptyChest();
                    x = x + InventoryUtil.count(Material.GOLD_NUGGET, true, false);
                    System.out.println(x);
                    ai.tick(10);
                    ai.closeContainer();
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
                    Main.self.sendChat("transfered" + " " + x + " " + "goldnuggets into the Tesseract");
                    cancel();
                }

            }
        } catch (InterruptedException ex) {

        }
        unregister();
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

    public void cancel() {
        shulkerrun = false;

    }
    
}
