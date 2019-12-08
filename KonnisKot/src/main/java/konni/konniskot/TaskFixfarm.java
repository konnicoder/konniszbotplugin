/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

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
public class TaskFixfarm extends Task {

    private static final Location SHULKER_WALK = new Location(290, 137, -8721).centerHorizontally();
    private static final Location BUTTON_SHULKER_LOC = new Location(289, 137, -8721).centerHorizontally();
    private static final Location SHULKER_OUTPUT_LOC = new Location(290, 138, -8723).centerHorizontally();
    
    public TaskFixfarm() {
        super(100);
    }

    public void run() {
      
        try {
            
            
            ai.moveTo(SHULKER_WALK);
            ai.tick();
            ai.openContainer(SHULKER_OUTPUT_LOC);
            int x = count(Material.GOLD_NUGGET , false, true);
            Main.self.sendChat("There are " + x +" goldnuggets in this container" );
                
           
            if(count(Material.GOLDEN_SWORD , false, true)>0 || count(Material.GOLD_INGOT , false, true)>0 || count(Material.FEATHER , false, true)>0) {
            System.out.println("ERROR");
            Main.self.sendChat("/msg Konni999 Error at the Shulkeroutput, ding konni");
            ai.tick(5);
            }
            
            
            
            
        } catch (InterruptedException ex) {
           
        }
      
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
    
}
