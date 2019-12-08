/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskTrashcan extends Task {

    private static final Location trashchest = new Location(298, 137, -8701).centerHorizontally();
    private static final Location trashablegen = new Location(299, 137, -8701).centerHorizontally();
    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();

    public TaskTrashcan() {
        super(100);
    }

    public void run() {

        try {
            ai.moveTo(trashablegen);
            ai.tick(20);
            System.out.println("Waitdone-deposit Trash");
            dumpTrash();

        } catch (InterruptedException trashfail) {

        }

    }

    private boolean dumpTrash() throws InterruptedException {
        if (InventoryUtil.findItem((i) -> i != null && TRASH_MATERIALS.contains(i.getType())) == -1) {
            return true;
        }

        if (!ai.openContainer(trashchest)) {
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

    static {
        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
    }
}
