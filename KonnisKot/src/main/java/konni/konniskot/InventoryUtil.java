/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.function.Predicate;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Dennis
 */
public class InventoryUtil {

    private static int nextSlot = 0;

    public static int findItem(Predicate<ItemStack> itemFilter) {
        int staticOffset = Main.self.getInventory().getStaticOffset();
        for (int i = staticOffset; i < staticOffset + 36; i++) {
            if (itemFilter.test(Main.self.getInventory().getSlot(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean findAndSelect(Predicate<ItemStack> itemFilter) {
        if (itemFilter.test(Main.self.getInventory().getItemInHand())) {
            return true;
        }
        for (int i = 36; i <= 44; i++) {
            if (itemFilter.test(Main.self.getInventory().getSlot(i))) {
                Main.self.selectSlot(i - 36);
                return true;
            }
        }
        for (int i = 9; i <= 35; i++) {
            if (itemFilter.test(Main.self.getInventory().getSlot(i))) {
                Main.self.getInventory().click(i, 0, 0);
                Main.self.getInventory().click(nextSlot + 36, 0, 0);
                Main.self.getInventory().click(i, 0, 0);
                Main.self.selectSlot(nextSlot);
                nextSlot = (nextSlot + 1) % 9;
                return true;
            }
        }
        return false;
    }

    public static boolean findAndSelect(Material mat, int amount) {
        return findAndSelect((i) -> i.getType() == mat && i.getAmount() == amount);
    }

    public static int count(Material mat, boolean testStatic, boolean testExternal) {
        int count = 0;
        int staticOffset = Main.self.getInventory().getStaticOffset();
        if (testExternal) {
            for (int i = 0; i <= staticOffset; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is != null && is.getType() == mat) {
                    count += is.getAmount();
                }
            }
        }
        if (testStatic) {
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is != null && is.getType() == mat) {
                    count += is.getAmount();
                }
            }
        }
        return count;
    }

    public static int findFreeStorageSlot(boolean staticInv) {
        int staticStart = Main.self.getInventory().getStaticOffset();
        if (staticInv) {
            for (int i = staticStart; i < staticStart + 36; i++) {
                if (Main.self.getInventory().getSlot(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < staticStart; i++) {
                if (Main.self.getInventory().getSlot(i) == null) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static int countFreeStorageSlots(boolean testStatic, boolean testExternal) {
        int count = 0;
        int staticOffset = Main.self.getInventory().getStaticOffset();
        if (testExternal) {
            for (int i = 0; i <= staticOffset; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is == null) {
                    count += 1;
                }
            }
        }
        if (testStatic) {
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is == null) {
                    count += 1;
                }
            }
        }
        return count;
    }
}
