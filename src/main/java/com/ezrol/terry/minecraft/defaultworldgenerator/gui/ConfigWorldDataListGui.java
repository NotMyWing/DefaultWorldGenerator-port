package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.BooleanTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.SettingsRoot;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;


/**
 * Class to configure the new JSON formatted preferences from the gui configure screen.
 * Created by ezterry on 6/17/17.
 */
public class ConfigWorldDataListGui extends GuiScreen implements GuiPageButtonList.GuiResponder {
    private enum ButtonIds{
        SAVE (85),
        CANCEL (86),
        MOVE_UP (87),
        MOVE_DOWN (88),
        NEW (89),
        RESTORE (90),
        EDIT (91),
        DELETE (92);

        private final int btnId;

        ButtonIds(int id){
            btnId = id;
        }

        /**
         * Get the button id
         * @return id
         */
        public int getBtnId() {
            return btnId;
        }

        private GuiButton getButton(ConfigWorldDataListGui window){
            for(GuiButton b : window.buttonList){
                if(b.id == btnId){
                    return b;
                }
            }
            return null;
        }
    }

    private GuiScreen parent;
    private SettingsRoot tree;
    private String title="DWG Config";
    private WorldSlotList slotList=null;
    private int nodeBeingEdited=-1;

    public ConfigWorldDataListGui(GuiScreen parent){
        this.parent = parent;
        tree = DefaultWorldGenerator.modConfig.getSettings();
    }

    protected void saveEdit(WorldTypeNode value){
        if(nodeBeingEdited == -1) {
            tree.getWorldList().add(value);
        }
        else {
            tree.getWorldList().set(nodeBeingEdited, value);
        }
    }

    @Override
    public void initGui() {
        int rightOfList = (this.width / 3) * 2;
        nodeBeingEdited = -1;

        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        title = I18n.format("defaultworldgenerator-port.config.gui.title");

        this.buttonList.add(new GuiButton(ButtonIds.MOVE_UP.getBtnId(),
                rightOfList+5,
                (this.height/2)-80,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.moveup")));
        this.buttonList.add(new GuiButton(ButtonIds.NEW.getBtnId(),
                rightOfList+5,
                (this.height/2)-56,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.new")));
        this.buttonList.add(new GuiButton(ButtonIds.EDIT.getBtnId(),
                rightOfList+5,
                (this.height/2)-36,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.edit")));
        this.buttonList.add(new GuiButton(ButtonIds.DELETE.getBtnId(),
                rightOfList+5,
                (this.height/2)-16,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.delete")));
        this.buttonList.add(new GuiButton(ButtonIds.MOVE_DOWN.getBtnId(),
                rightOfList+5,
                (this.height/2)+8,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.movedown")));
        this.buttonList.add(new GuiButton(ButtonIds.RESTORE.getBtnId(),
                rightOfList+5,
                (this.height)-68,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.restore")));
        this.buttonList.add(new GuiButton(ButtonIds.SAVE.getBtnId(),
                rightOfList+5,
                (this.height)-48,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.save")));
        this.buttonList.add(new GuiButton(ButtonIds.CANCEL.getBtnId(),
                rightOfList+5,
                (this.height)-28,
                (rightOfList/2)-10,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.cancel")));

        int idx = slotList==null?-1:slotList.selected;
        slotList = new WorldSlotList(rightOfList,height,25,height,28);
        slotList.selected=idx;

        updateButtons();
    }

    @SuppressWarnings("ConstantConditions")
    private void updateButtons(){
        int selected = slotList.getSelected();
        try {
            for(GuiButton b : buttonList){
                b.enabled=true;
            }
            if (selected == 0) {
                ButtonIds.MOVE_UP.getButton(this).enabled = false;
            }
            if (selected == tree.getWorldList().size() - 1){
                ButtonIds.MOVE_DOWN.getButton(this).enabled = false;
            }
            if (selected == -1){
                ButtonIds.MOVE_UP.getButton(this).enabled = false;
                ButtonIds.MOVE_DOWN.getButton(this).enabled = false;
                ButtonIds.EDIT.getButton(this).enabled = false;
                ButtonIds.DELETE.getButton(this).enabled = false;
            }
        }
        catch(NullPointerException e){
            Log.error("Unable to update button states: "+e);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        Gui.drawRect(0,24,width,height,0x55000000);
        slotList.drawScreen(mouseX,mouseY,partialTicks);
        drawHorizontalLine(0,width,24,0xff000000);
        drawCenteredString(fontRenderer, title, width / 2, 8, 0xffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1){
            DefaultWorldGenerator.modConfig.restoreSettings();
            this.mc.displayGuiScreen(parent);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ButtonIds currentButton=null;
        //get the button as a ButtonIds enum
        for(ButtonIds id: ButtonIds.values()){
            if(id.getBtnId() == button.id){
                currentButton = id;
            }
        }
        if(currentButton == null){
            return;
        }

        //now switch over the button provided
        switch(currentButton){
            case EDIT:
                if(slotList.getSelected() != -1){
                    nodeBeingEdited = slotList.getSelected();
                    if(nodeBeingEdited >= tree.getWorldList().size()) break;
                    this.mc.displayGuiScreen(
                            new ConfigWorldDataGui(
                                    tree.getWorldList().get(nodeBeingEdited),
                                    this
                            )
                    );
                }
                break;
            case NEW:
                nodeBeingEdited = -1;
                this.mc.displayGuiScreen(
                    new ConfigWorldDataGui(new WorldTypeNode(null),this)
                );
                break;
            case MOVE_DOWN:
                {
                    int slot = slotList.getSelected();
                    if(slot >= tree.getWorldList().size()-1 || slot < 0) break;
                    tree.getWorldList().add(slot+1,tree.getWorldList().remove(slot));
                    slotList.selected++;
                    updateButtons();
                }
                break;
            case MOVE_UP:
                {
                    int slot = slotList.getSelected();
                    if(slot<1 || slot >= tree.getWorldList().size()) break;
                    tree.getWorldList().add(slot-1,tree.getWorldList().remove(slot));
                    slotList.selected--;
                    updateButtons();
                }
                break;
            case DELETE:
                {
                    int slot = slotList.getSelected();
                    if(slot <0 || slot >= tree.getWorldList().size())break;
                    tree.getWorldList().remove(slot);
                    slotList.selected=-1;
                    updateButtons();
                }
                break;
            case RESTORE:
                DefaultWorldGenerator.modConfig.restoreSettings();
                tree = DefaultWorldGenerator.modConfig.getSettings();
                slotList.selected=-1;
                updateButtons();
                break;
            case SAVE:
                DefaultWorldGenerator.modConfig.writeToDisk();
                this.mc.displayGuiScreen(parent);
                break;
            case CANCEL:
                DefaultWorldGenerator.modConfig.restoreSettings();
                this.mc.displayGuiScreen(parent);
                break;
        }
    }

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setEntryValue(int id, String value) {

    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        slotList.handleMouseInput();
    }

    private class WorldSlotList extends GuiSlot {

        int selected = -1;

        private WorldSlotList(int width, int height, int topOff, int bottomOff, int slotHeight){
            super(ConfigWorldDataListGui.this.mc, width, height, topOff, bottomOff, slotHeight);
            this.left=8;
            this.width -= 8;
        }
        @Override
        protected int getSize() {
            if(tree.getWorldList().size() == 0){
                tree.getWorldList().add(new WorldTypeNode(null));
            }
            return(tree.getWorldList().size());
        }

        private int getSelected(){
            return selected;
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selected = slotIndex;
            updateButtons();

            if(isDoubleClick){
                nodeBeingEdited = getSelected();
                if(nodeBeingEdited >= tree.getWorldList().size()) return;
                this.mc.displayGuiScreen(
                        new ConfigWorldDataGui(
                                tree.getWorldList().get(nodeBeingEdited),
                                ConfigWorldDataListGui.this
                        )
                );
            }
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return slotIndex == selected;
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
            WorldTypeNode node = tree.getWorldList().get(entryID);
            String name = ((StringTypeNode)node.getField(WorldTypeNode.Fields.CONFIGURATION_NAME)).getValue();
            boolean hidden = !((BooleanTypeNode)node.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).getValue();
            String type = ((StringTypeNode)node.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue();
            boolean primary=false;
            if(hidden && entryID==0){
                primary = true;
                for(WorldTypeNode n : tree.getWorldList()){
                    if(((BooleanTypeNode)n.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).getValue()){
                        primary=false;
                    }
                }
            }

            fontRenderer.drawStringWithShadow(name,insideLeft + 10, yPos + 3, 0xffffff);
            if(primary){
                fontRenderer.drawString(I18n.format("defaultworldgenerator-port.config.lst.primary"),
                        insideLeft + 14, yPos + 15, 0x88ff88);
            }
            else {
                fontRenderer.drawString(hidden ? I18n.format("defaultworldgenerator-port.config.lst.hidden") :
                                I18n.format("defaultworldgenerator-port.config.lst.shown"),
                        insideLeft + 14, yPos + 15, hidden ? 0xff8888 : 0x88ff88);
            }
            fontRenderer.drawString(type,
                    insideLeft + width/2, yPos + 15, 0xeeeeee);
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
