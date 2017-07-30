package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ezterry on 7/9/17.
 */
public class ConfigChooseWorldType extends GuiScreen {
    private WorldTypeNode activeNode;
    private GuiScreen parent;
    private WorldSlotList list;
    private String original;
    private String title;
    private static int BTN_OK = 400;

    public ConfigChooseWorldType(GuiScreen par, WorldTypeNode node){
        activeNode=node;
        parent=par;
        original = ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue();
    }

    @Override
    public void initGui() {
        title = I18n.format("defaultworldgenerator-port.config.gui.worldtype.title");
        this.buttonList.add(new GuiButton(BTN_OK,
                width/4+20,
                height-28,
                width/2-40,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.ok")));
        this.list = new WorldSlotList(width,height,((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue());
        parent.setGuiSize(width,height);
        parent.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == BTN_OK){
            exitScreen();
        }
    }

    private void exitScreen(){
        if(!list.selected.equals("")){
            ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).setValue(list.selected);
            if(list.selected != original){
                ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CUSTOMIZATION_STRING)).setValue("");
            }
            this.mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        parent.drawScreen(-1,-1,partialTicks);
        Gui.drawRect(0,0,width,height,0x44000000);
        Gui.drawRect(width/4 - 8 ,0,(width/4)*3+8,height,0xff000000);
        list.drawScreen(mouseX,mouseY,partialTicks);
        Gui.drawRect(width/4 - 8 ,0,(width/4)*3+8,28,0xff000000);
        Gui.drawRect(width/4 - 8 ,height-34,(width/4)*3+8,height,0xff000000);

        drawCenteredString(fontRenderer, title, width / 2, 8, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        list.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1){
            mc.displayGuiScreen(parent);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private class WorldSlotList extends GuiSlot {

        String selected = "";
        Map<String,WorldType> types;
        List<String> keys;

        private WorldSlotList(int screen_width, int screen_height,String current){
            super(ConfigChooseWorldType.this.mc, screen_width/2, screen_height-34, 0, 0, 18);
            this.left=screen_width/4;
            this.right=(screen_width/4)*3;
            this.width=screen_width/2;
            this.top=28;
            this.bottom=screen_height-34;
            this.height=bottom-28;

            selected=current;

            types= new HashMap<>();
            keys= new LinkedList<>();
            for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
                if (WorldType.WORLD_TYPES[i] != null && WorldType.WORLD_TYPES[i].canBeCreated()) {

                    types.put(WorldType.WORLD_TYPES[i].getName(), WorldType.WORLD_TYPES[i]);
                    keys.add(WorldType.WORLD_TYPES[i].getName());
                }
            }
        }

        @Override
        protected int getSize() {
            return types.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selected = keys.get(slotIndex);
            if(isDoubleClick){
                exitScreen();
            }
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return(keys.get(slotIndex).equals(selected));
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
            String s = I18n.format(types.get(keys.get(entryID)).getTranslationKey());
            fontRenderer.drawString(s,insideLeft + 10, yPos + 4, 0xeeeeee);
        }

        @Override
        public int getListWidth(){
            if(this.getMaxScroll() > 0){
                return(width-12);
            }
            return(width);
        }
        @Override
        protected int getScrollBarX()
        {
            return right-4;
        }
    }
}
