package com.ezrol.terry.minecraft.defaultworldgenerator;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringListTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.datapack.WorldWrapper;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Run these commands when the server starts (play joins Single pLayer, or a dedicated server starts)
 *
 *
 * Created by ezterry on 7/16/17.
 */
public class WorldInitCommands {
    private WorldWrapper worldInfo;
    private boolean initDone;

    public WorldInitCommands(WorldWrapper worldInfo){
        this.worldInfo=worldInfo;
        initDone=false;
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * If the server stops lets us explicitly unload to we don't run on the next instance
     */
    public void unload(){
        if(!initDone){
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    /**
     * Run the actual init commands (will be on the first tick of the server startting/restarting)
     */
    private void runInitCommands(ICommandManager manager,World w){
        StringListTypeNode commands = worldInfo.getWorldType().getField(WorldTypeNode.Fields.WORLD_LOAD_COMMANDS);

        for(String command : commands.getValue()){

            manager.executeCommand(new VCommandSender(w),command);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onServerWorldTick(TickEvent.WorldTickEvent event){
        if(event.phase != TickEvent.Phase.END){
            return; //We don't want the start of the tick not the end of one
        }
        if(event.type != TickEvent.Type.WORLD){
            //this ought to be assumable, but we want the world tick
            return;
        }

        //ensure dim 0
        int dim = event.world.provider.getDimension();
        if(dim != 0){
            return;
        }

        if(initDone){
            //we presumably cancelled but an extra event was in queue ignore
            return;
        }

        ICommandManager manager = Optional.ofNullable(event.world.getMinecraftServer())
                .flatMap(s -> Optional.of(s.getCommandManager())).orElse(null);

        if(manager!=null) {
            runInitCommands(manager,event.world);
            initDone = true;
            //we don't need any future tick events, unregister so we don't slow down the game
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    /**
     * A local command sender for our initial commands
     */
    private static class VCommandSender implements ICommandSender{
        private static final String name="DWG_INIT";
        private World world;

        private VCommandSender(World w){
            world = w;
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public void sendMessage(ITextComponent component){
            Log.info(String.format("%s: %s",name,component.getUnformattedText()));
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public String getName() {
            return name;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return permLevel <= 2;
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public BlockPos getPosition() {
            return world.getSpawnPoint();
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public Vec3d getPositionVector()
        {
            BlockPos pos = getPosition();
            return new Vec3d((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public World getEntityWorld() {
            return world;
        }

        @Nullable
        @Override
        public Entity getCommandSenderEntity() {
            return null;
        }

        @Override
        public boolean sendCommandFeedback() {
            return false;
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public void setCommandStat(CommandResultStats.Type type, int amount) {
            //nop
        }

        @Nullable
        @Override
        public MinecraftServer getServer() {
            return world.getMinecraftServer();
        }
    }
}
