package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.*;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PendingCommand;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;
import java.util.*;

/**
 * Server only logic
 * Created by ezterry on 2/24/17.
 */
@SuppressWarnings("unused,WeakerAccess")
public class ServerProxy extends CommonProxy {
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (event.getSide() == Side.SERVER) {
            Log.info("Injecting Server Defaults");

            MinecraftServer s = FMLServerHandler.instance().getServer();
            if (s.isDedicatedServer()) {
                DedicatedServer server = (DedicatedServer) s;

                UUID type = null;
                try{
                    type = UUID.fromString(server.getStringProperty(Reference.MOD_ID, ""));
                } catch (Exception e){
                    Log.info("Default World Type UUID not set in server.properties");
                }

                String levelName = server.getStringProperty("level-name","world");
                File levelDir = new File(levelName);
                File levelData = new File(levelDir,"level.dat");
                if(!(levelDir.isDirectory() && levelData.isFile())){
                    Log.info("World Type missing find default or ask user");
                    WorldTypeNode config = getWorldConfig(type,server);

                    Log.info("Updating server.properties");
                    server.setProperty("level-type", ((StringTypeNode)config.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue());
                    server.setProperty("generator-settings", ((StringTypeNode)config.getField(WorldTypeNode.Fields.CUSTOMIZATION_STRING)).getValue());
                    if(!((StringTypeNode)config.getField(WorldTypeNode.Fields.SEED)).getValue().equals("")) {
                        server.setProperty("level-seed", ((StringTypeNode) config.getField(WorldTypeNode.Fields.SEED)).getValue());
                    }

                    QuadStateTypeNode.States state;
                    state =((QuadStateTypeNode)config.getField(WorldTypeNode.Fields.BONUS_CHEST_STATE)).getValue();

                    if(state == QuadStateTypeNode.States.STATE_ENABLED || state == QuadStateTypeNode.States.STATE_FORCED) {
                        server.canCreateBonusChest(true);
                    }

                    state =((QuadStateTypeNode)config.getField(WorldTypeNode.Fields.STRUCTURE_STATE)).getValue();

                    if(state == QuadStateTypeNode.States.STATE_ENABLED || state == QuadStateTypeNode.States.STATE_FORCED) {
                        server.setProperty("generate-structures",true);
                    }
                    else{
                        server.setProperty("generate-structures",false);
                    }

                    //make sure the selectedLevel is set
                    DefaultWorldGenerator.selectedLevel =config;
                }
                else{
                    Log.info("World exists, not updating server.properties");
                }
            }
        }
    }

    public WorldTypeNode getWorldConfig(UUID type,DedicatedServer server){
        Map<UUID,WorldTypeNode> possibleNodes= new HashMap<>();
        List<UUID> DisplayOrder = new LinkedList<>();

        WorldTypeNode first = null;
        boolean usePrimary=true;

        for(WorldTypeNode n : DefaultWorldGenerator.modConfig.getSettings().getWorldList()){
            UUID key = ((UuidTypeNode)n.getField(WorldTypeNode.Fields.UUID)).getValue();
            possibleNodes.put(key,n);

            if(first == null){
                first = n;
            }
            if(((BooleanTypeNode)n.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).getValue()){
                usePrimary = false;
                DisplayOrder.add(key);
            }
        }
        if(type != null && possibleNodes.containsKey(type)){
            return(possibleNodes.get(type));
        }
        if(usePrimary){
            return first;
        }
        StringBuilder prompt = new StringBuilder();
        prompt.append("\n\nChoose A default world type:\n\n");
        int i = 0;
        for(UUID u : DisplayOrder){
            i++;
            prompt.append(i);
            prompt.append(") ");
            prompt.append(((StringTypeNode)possibleNodes.get(u).getField(
                    WorldTypeNode.Fields.CONFIGURATION_NAME)).getValue());
            prompt.append("   [");
            prompt.append(u);
            prompt.append("]\n");
        }
        prompt.append("\n");
        prompt.append("Type \"/" + Reference.MOD_ID + " <the number for the server's worldtype>\n" );
        prompt.append("Alternativly add the uuid as the value of \"" + Reference.MOD_ID + "\" in server.properties" );
        prompt.append("\n\n");

        try{
            int idx = Integer.parseInt(queryUser(prompt.toString(),server)) - 1;

            if(idx < 0 || idx >= DisplayOrder.size()){
                throw new InvalidWorldTypeException("Invalid Selection");
            }
            return(possibleNodes.get(DisplayOrder.get(idx)));
        }
        catch (InterruptedException e){
            throw new InvalidWorldTypeException("User Prompt was interrupted");
        }
    }

    public String queryUser(String text,DedicatedServer server) throws InterruptedException
    {
        String retval = "";
        String commandPrefix = "/" + Reference.MOD_ID + " ";

        Log.warn(text);

        boolean done = false;

        while (!done && server.isServerRunning())
        {
            if (Thread.interrupted()) throw new InterruptedException();

            // rudimentary command processing, check for fml confirm/cancel and stop commands
            synchronized (server.pendingCommandList)
            {
                for (Iterator<PendingCommand> it = server.pendingCommandList.iterator(); it.hasNext(); )
                {
                    String cmd = it.next().command.trim().toLowerCase();

                    if (cmd.startsWith(commandPrefix))
                    {
                        retval = cmd.substring(commandPrefix.length()).trim();

                        done = true;
                        it.remove();
                    }
                    else if (cmd.equals("/stop"))
                    {
                        throw new InvalidWorldTypeException("Stopping per user request");
                    }
                }
            }
            Thread.sleep(10L);
        }
        return retval;
    }

    public static class InvalidWorldTypeException extends RuntimeException{
        public InvalidWorldTypeException(String message){
            super(message);
        }
    }
}
