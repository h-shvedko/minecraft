/**
 * Created by shvedko on 25.10.2018.
 */

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Builds extends JavaPlugin {
    public void onEnable() {
    }

    public void onDisable() {

    }

    public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args){
        Player player = (Player) sender;

        Location position = player.getLocation();
        World world = player.getWorld();

        position.setX(position.getX() + 3);
        double yStart = position.getY();
        double zStart = position.getZ();

        for (int i = 0; i < Integer.parseInt(args[1]); i++){
            position.setY(yStart + i);
            for (int j = 0; j < Integer.parseInt(args[2]); j++){
                position.setZ(zStart + j);
                world.getBlockAt(position).setType(this.getBlockType(args[0]));
            }
        }

        return true;
    }

    private Material getBlockType(String arg) {
        switch (arg){
            case "ground":
                return Material.DIRT;
            case "granit":
                return Material.GRANITE;
            case "glass":
                return Material.GLASS;
            case "diamond":
                return Material.DIAMOND;
            case "wood":
                return Material.OAK_WOOD;
            default:
                return Material.BEEF;
        }
    }
}
