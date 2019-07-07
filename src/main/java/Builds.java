/**
 * Created by shvedko on 25.10.2018.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Builds extends JavaPlugin {

    private static String typeOfBuildsScan = "scan";
    private String typeOfBuildsCreate = "create";

    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int deep = 0;
    private int width = 0;
    private int hieght = 0;

    private List<Block> buildData = new ArrayList<Block>();
    private String blockName;

    public void onEnable() {
    }

    public void onDisable() {

    }

    /**
     * @param sender
     * @param command
     * @param commandName
     * @param args
     * @return
     */
    public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args) {
        Player player = (Player) sender;

        World world = player.getWorld();

        readCoordinates(args);

        if (args[0].equals(typeOfBuildsScan)) {
            System.out.println("Start scanning!");
            scanBlocks(world);
            saveToJson();
        } else {
            System.out.println("Start creating!");

            loadBlockData();
            createBlocks(world, player);
        }

        return true;
    }

    /**
     *
     */
    private void saveToJson() {
        Gson gsonBuilder = new GsonBuilder().create();

        String jsonFromJavaArrayList = gsonBuilder.toJson(buildData);

        try (FileWriter file = new FileWriter(blockName + ".json")) {
            file.write(jsonFromJavaArrayList);
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBlockData(){
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try {
            BufferedReader br = new BufferedReader(new FileReader(blockName + ".json"));
            JsonElement jsonElement = jsonParser.parse(br);

            //Create generic type
            Type type = new TypeToken<List<Block>>() {}.getType();

            buildData = gson.fromJson(jsonElement, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param args
     */
    private void readCoordinates(String[] args) {
        blockName = args[1];
        x = Integer.valueOf(args[2]);
        y = Integer.valueOf(args[3]);
        z = Integer.valueOf(args[4]);
        deep = Integer.valueOf(args[5]);
        width = Integer.valueOf(args[6]);
        hieght = Integer.valueOf(args[7]);
    }

    /**
     * @param world
     * @param player
     */
    private void createBlocks(World world, Player player) {
        if (x != 0 && y != 0 && z != 0) {
            Location position = player.getLocation();

            position.setX(position.getX() + 5);

            for (Block block : buildData) {
                double x0 = block.getLocation().getX();
                double y0 = block.getLocation().getY();
                double z0 = block.getLocation().getZ();

                block.getLocation().setX(x + x0);
                block.getLocation().setY(y + y0);
                block.getLocation().setZ(z + z0);

                world.getBlockAt(position).setType(block.getType());
            }

        }
    }

    /**
     * @param world
     */
    private void scanBlocks(World world) {

//        if (x != 0 && y != 0 && z != 0) {
        int deepNew, widthNew;
        if(deep < 0){
            deepNew = deep * (-1);
        }

        if(width < 0){
            widthNew = width * (-1);
        }

        for (int k = y; k <= y + hieght; k++) {
            for (int j = z; j <= z + width; j++) {
                for (int i = x; i <= x + deep; i++) {
                    Block block = world.getBlockAt(i, j, k);
                    if(deep < 0){
                        block.getLocation().setX(i * (-1));
                    }

                    if(width < 0){
                        block.getLocation().setY(k * (-1));
                    }

                    block.getLocation().setZ(j);
                    buildData.add(block);
                }
            }
        }
        }
//    }
}
