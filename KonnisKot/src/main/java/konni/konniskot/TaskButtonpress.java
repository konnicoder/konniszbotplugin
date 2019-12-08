package konni.konniskot;

import java.util.List;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.YamlConfiguration;

/**
 *
 * @author Konstantin
 */
public class TaskButtonpress extends Task {

 
   
    public TaskButtonpress() {
        super(100);
    }

    public void run() {
        try {
           int penis = Main.config.getInt("penis", 0);
           ai.tick();
           System.out.println(penis);
           ai.tick();
          
           ai.tick();
            
            
        } catch (InterruptedException fi) {
        }
    }

}
