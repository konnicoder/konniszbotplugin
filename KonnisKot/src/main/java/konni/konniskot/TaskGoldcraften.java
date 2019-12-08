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
public class TaskGoldcraften extends Task {

    private static final Location startpunkt = new Location(299, 137, -8712).centerHorizontally();
    private static final Location craftingbench = new Location(301, 138, -8712).centerHorizontally();
    private static final Location nuggetholen = new Location(299, 137, -8713).centerHorizontally();
    private static final Location nuggetTesseract = new Location(300, 138, -8713).centerHorizontally();
    private static final Location ingotablegen = new Location(299, 137, -8714).centerHorizontally();
    private static final Location ingotTesseract = new Location(300, 138, -8714).centerHorizontally();
    private boolean rungoldcraft = true;
    public TaskGoldcraften() {
        super(100);
    }

    public void run() {
        try {
            while (rungoldcraft) {
                
                ai.moveTo(nuggetholen);
                System.out.println("task startet");
                ai.tick();
                while (InventoryUtil.count(Material.GOLD_NUGGET, true, false) < 9 * 64) {
                    Main.self.clickBlock(nuggetTesseract);
                    ai.tick(3);
                }
                ai.moveTo(startpunkt);
        
                ai.openContainer(craftingbench);
                int staticOffset = Main.self.getInventory().getStaticOffset();
                for (int crafting = 1; crafting <= 9; crafting++) {
                    for (int local = staticOffset; local < staticOffset + 36; local++) {
                        if (Main.self.getInventory().getSlot(local) != null
                                && Main.self.getInventory().getSlot(local).getType() == Material.GOLD_NUGGET) {
                            ai.transferItem(local, crafting);
                            System.out.println("MIAUUUU " + local + " " + crafting);

                            ai.tick(2);
                            break;
                        }
                    }
                }
                Main.self.getInventory().click(0, 1, 0);
                ai.closeContainer();
                
                ai.moveTo(ingotablegen);
                ai.tick(3);
                Main.self.sneak(true);
                ai.tick();
                Main.self.placeBlock(ingotTesseract, BlockFace.EAST);
                ai.tick();
                Main.self.sneak(false);
            }
        } catch (InterruptedException guenther) {
        }
        unregister();
    }
public void cancel(){
        rungoldcraft = false;
        System.out.println("miauuu");
        
    }
}
