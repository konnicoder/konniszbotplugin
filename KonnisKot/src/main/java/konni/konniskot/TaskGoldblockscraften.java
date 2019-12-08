/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskGoldblockscraften extends Task {

    private static final Location startpunkt = new Location(299, 137, -8712).centerHorizontally();
    private static final Location craftingbench = new Location(301, 138, -8712).centerHorizontally();
    private static final Location ingotholen = new Location(299, 137, -8714).centerHorizontally();
    private static final Location ingotTesseract = new Location(300, 138, -8714).centerHorizontally();
    private static final Location goldblockTesseract = new Location(300, 138, -8715).centerHorizontally();
    private static final Location goldblockablegen = new Location(299, 137, -8715).centerHorizontally();

    public TaskGoldblockscraften() {
        super(100);
    }

    public void run() {
        try {
            while (true) {

                ai.moveTo(ingotholen);
                ai.tick();
                while (InventoryUtil.count(Material.GOLD_INGOT, true, false) < 9 * 64) {
                    Main.self.clickBlock(ingotTesseract);
                    ai.tick(3);
                }
                ai.moveTo(startpunkt);

                ai.openContainer(craftingbench);
                int staticOffset = Main.self.getInventory().getStaticOffset();
                for (int crafting = 1; crafting <= 9; crafting++) {
                    for (int local = staticOffset; local < staticOffset + 36; local++) {
                        if (Main.self.getInventory().getSlot(local) != null
                                && Main.self.getInventory().getSlot(local).getType() == Material.GOLD_INGOT) {
                            ai.transferItem(local, crafting);
                            System.out.println("MIAUUUU " + local + " " + crafting);

                            ai.tick(2);
                            break;
                        }
                    }
                }
                Main.self.getInventory().click(0, 1, 0);
                ai.closeContainer();

                ai.moveTo(goldblockablegen);
                ai.tick(3);
                Main.self.sneak(true);
                ai.tick();
                Main.self.placeBlock(goldblockTesseract, BlockFace.EAST);
                ai.tick();
                Main.self.sneak(false);
            }
        } catch (InterruptedException siglinde) {
        }
    }

}
