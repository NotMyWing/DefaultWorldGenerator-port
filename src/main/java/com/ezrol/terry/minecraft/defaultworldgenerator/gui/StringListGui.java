package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringListTypeNode;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.function.Function;

/**
 * Created by ezterry on 7/9/17.
 */
public class StringListGui extends GuiScreen {
    private StringListTypeNode activeNode;
    private GuiScreen parent;
    private StringSlotList list=null;
    private String title;
    private Function<String,Boolean> filter;
    private final static int BTN_OK = 400;
    private final static int BTN_ADD = 401;
    private final static int BTN_REMOVE = 402;
    private final static int TXT_EDITBOX = 403;
    private String currentText;
    private GuiTextField newStringField;

    public StringListGui(GuiScreen par, StringListTypeNode node, String title, Function<String,Boolean> filter){
        activeNode=node;
        parent=par;
        this.title=title;
        if(filter == null){
            this.filter = s -> true;
        } else {
            this.filter = filter;
        }
        currentText="";
    }

    @Override
    public void initGui() {
        int idx = (list == null)? -1 : list.selected;
        list = new StringSlotList(width,height);
        list.selected=idx;

        this.buttonList.add(new GuiButton(BTN_OK,
                width/4+20,
                height-28,
                width/2-40,
                18,
                I18n.format("defaultworldgenerator-port.config.btn.ok")));
        this.buttonList.add(new GuiButton(BTN_ADD,
                width/2 +10,
                height-48,
                100,
                18,
                I18n.format("defaultworldgenerator-port.config.btn.strings.add")));
        this.buttonList.add(new GuiButton(BTN_REMOVE,
                width/2 - 110,
                height-48,
                100,
                18,
                I18n.format("defaultworldgenerator-port.config.btn.strings.remove")));

        newStringField = new GuiTextField(TXT_EDITBOX, fontRenderer, 50,26,width-100,18);
        newStringField.setMaxStringLength(65535);
        newStringField.setText(currentText);
        newStringField.setGuiResponder(new GuiResponder() {
            @Override
            public void setEntryValue(int id, boolean value) {

            }

            @Override
            public void setEntryValue(int id, float value) {

            }

            @Override
            public void setEntryValue(int id, String value) {
                currentText=value;
                updateButtons();
            }
        });
        newStringField.setCursorPositionZero();
        updateButtons();
    }

    private void updateButtons(){
        for(GuiButton b : this.buttonList){
            if(b.id == BTN_ADD){
                b.enabled = currentText.length() > 0 && filter.apply(currentText);
                if(list.selected != -1){
                    b.displayString = I18n.format("defaultworldgenerator-port.config.btn.strings.replace");
                }
                else {
                    b.displayString = I18n.format("defaultworldgenerator-port.config.btn.strings.add");
                }
            }
            if(b.id == BTN_REMOVE){
                b.enabled = list.selected >= 0 && list.selected < activeNode.getValue().size();
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case BTN_OK:
                exitScreen();
                break;
            case BTN_ADD:
                //Add the string from the text box if valid
                if(currentText.length() > 0 && filter.apply(currentText)){
                    if(list.selected < 0 || list.selected >= activeNode.getValue().size()) {
                        activeNode.getValue().add(currentText);
                    }
                    else{
                        activeNode.getValue().remove(list.selected);
                        activeNode.getValue().add(list.selected,currentText);
                    }
                    list.selected = -1;
                    currentText = "";
                    newStringField.setText("");
                }
                updateButtons();
                break;
            case BTN_REMOVE:
                //remove the select string if valid
                if(list.selected >= 0 && list.selected < activeNode.getValue().size()){
                    activeNode.getValue().remove(list.selected);
                    list.selected = -1;
                    currentText = "";
                    newStringField.setText("");
                }
                updateButtons();
                break;
        }
    }



    private void exitScreen(){
        this.mc.displayGuiScreen(parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        list.drawScreen(mouseX,mouseY,partialTicks);
        Gui.drawRect(0 ,0,width,50,0xff000000);
        Gui.drawRect(0 ,height-50,width,height,0xff000000);

        drawCenteredString(fontRenderer, title, width / 2, 8, 0xffffff);
        newStringField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen(){
        newStringField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        newStringField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (newStringField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        if(keyCode == 1){
            exitScreen();
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        list.handleMouseInput();
    }

    private class StringSlotList extends GuiSlot {

        int selected = -1;

        private StringSlotList(int screen_width, int screen_height){
            super(StringListGui.this.mc, screen_width, screen_height-50, 50, 50, 18);
            this.left=8;
            this.right=screen_width-8;
            this.width=screen_width-16;
            this.top=50;
            this.bottom=screen_height-50;
            this.height=bottom;
        }

        @Override
        protected int getSize() {
            return activeNode.getValue().size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selected = slotIndex;
            updateButtons();
            currentText=activeNode.getValue().get(slotIndex);
            newStringField.setText(currentText);
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return(slotIndex == selected);
        }

        @Override
        protected void drawBackground() {
            StringListGui.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
            String s = activeNode.getValue().get(entryID);
            fontRenderer.drawString(s,insideLeft + 10, yPos + 4, 0xeeeeee);
        }

        @Override
        public int getListWidth(){
            if(this.getMaxScroll() > 0){
                return(width-8);
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
