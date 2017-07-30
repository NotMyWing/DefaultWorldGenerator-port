package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.lib.huffstruct.Huffstruct;
import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

/**
 * Configuration of the default world settings
 *
 * Created by ezterry on 7/6/17.
 */
public class ConfigWorldDataGui extends GuiScreen implements GuiPageButtonList.GuiResponder{
    private ConfigWorldDataListGui parent;
    private WorldTypeNode activeNode;
    private final byte[] origData; //original data when screen was opened (for revert)

    //Button IDs for the screen
    private static final int BTN_REVERT_ID = 101;
    private static final int BTN_DONE_ID = 102;
    private static final int TXT_WORLD_NAME_ID = 201;
    private static final int TXT_DESCRIPTION_ID = 202;
    private static final int TXT_UUID_ID = 203;
    private static final int TOGGLE_LOCKTYPE_ID = 204;
    private static final int TOGGLE_IN_LIST_ID = 205;
    private static final int TXT_WORLD_SEED_ID = 206;
    private static final int TOGGLE_STRUCTURES_ID = 207;
    private static final int TOGGLE_BONUSCHEST_ID = 208;
    private static final int TOGGLE_ICON_ID = 209;
    private static final int BTN_SET_WORLD_TYPE_ID = 210;
    private static final int BTN_CUSTOMIZE_WORLD_ID = 211;
    private static final int BTN_LOAD_COMMANDS_ID = 212;
    private static final int BTN_DATA_PACKS_ID = 213;

    //Screen title
    private final String title;

    //toggle lists
    private String[] booleanValues;
    private String[] stateValues;
    private String[] pngFiles;

    //position references
    private int leftRow=0;
    private int rightRow=0;
    private int rowWidth=10;

    //on screen text fields
    private GuiTextField configNameTextField;
    private GuiTextField configDescriptionField;
    private GuiTextField configUUIDField;
    private GuiTextField configSeedField;

    private String newUUID=null;

    public ConfigWorldDataGui(WorldTypeNode node, ConfigWorldDataListGui par){
        parent=par;
        title = I18n.format("defaultworldgenerator-port.config.gui.world.title");
        origData= Huffstruct.dumpData(node);
        activeNode = new WorldTypeNode(Huffstruct.loadData(origData));

        //boolean toggle choices
        booleanValues = new String[2];
        booleanValues[0]=I18n.format("defaultworldgenerator-port.config.gui.boolean.false");
        booleanValues[1]=I18n.format("defaultworldgenerator-port.config.gui.boolean.true");

        //quad state toggle choices
        stateValues = new String[4];
        stateValues[0]=I18n.format("defaultworldgenerator-port.config.gui.state.disabled");
        stateValues[1]=I18n.format("defaultworldgenerator-port.config.gui.state.enabled");
        stateValues[2]=I18n.format("defaultworldgenerator-port.config.gui.state.blocked");
        stateValues[3]=I18n.format("defaultworldgenerator-port.config.gui.state.forced");

        //png file toggle choices
        pngFiles=DefaultWorldGenerator.modSettingsDir.list((dir, name) -> {
            return name != null && name.endsWith(".png");
        });
        pngFiles=ArrayUtils.add(pngFiles,0,"NONE");
    }

    private String quadStateToBtn(QuadStateTypeNode n){
        switch(n.getValue()){
            case STATE_DISABLED:
                return stateValues[0];
            case STATE_ENABLED:
                return stateValues[1];
            case STATE_BLOCKED:
                return stateValues[2];
            case STATE_FORCED:
                return stateValues[3];
        }
        return("");
    }
    private void setQuadStateFromBtn(QuadStateTypeNode n, String val){
        if(val.equals(stateValues[0])){
            n.setValue(QuadStateTypeNode.States.STATE_DISABLED);
        }
        else if(val.equals(stateValues[1])){
            n.setValue(QuadStateTypeNode.States.STATE_ENABLED);
        }
        else if(val.equals(stateValues[2])){
            n.setValue(QuadStateTypeNode.States.STATE_BLOCKED);
        }
        else if(val.equals(stateValues[3])){
            n.setValue(QuadStateTypeNode.States.STATE_FORCED);
        }
    }

    public void initGui() {
        this.buttonList.clear();

        Keyboard.enableRepeatEvents(true);

        leftRow = this.width/7;
        rightRow = (this.width/2)+8;
        rowWidth = ((this.width/7)*5)/2-8;

        this.buttonList.add(new GuiButton(BTN_REVERT_ID,
                leftRow,
                (this.height)-28,
                rowWidth,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.revert")));
        this.buttonList.add(new GuiButton(BTN_DONE_ID,
                rightRow,
                (this.height)-28,
                rowWidth,
                20,
                I18n.format("defaultworldgenerator-port.config.btn.done")));

        //boolean toggle buttons
        this.buttonList.add(new GuiConfigStateButton(TOGGLE_IN_LIST_ID,
                leftRow,
                185,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.toggle.inlist"),
                booleanValues,
                booleanValues[((BooleanTypeNode)activeNode.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).getValue() ? 1 : 0]));
        this.buttonList.add(new GuiConfigStateButton(TOGGLE_LOCKTYPE_ID,
                rightRow,
                185,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.toggle.locked"),
                booleanValues,
                booleanValues[((BooleanTypeNode)activeNode.getField(WorldTypeNode.Fields.LOCK_WORLD_TYPE)).getValue() ? 1 : 0]));

        //The icon button
        //toggle icon (if current entry is missing just add it)
        String icon=((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_IMAGE)).getValue();
        if(icon.equals("")){
            icon="NONE";
        }
        if(!ArrayUtils.contains(pngFiles,icon)){
            pngFiles=ArrayUtils.add(pngFiles,icon);
        }

        this.buttonList.add(new GuiConfigStateButton(TOGGLE_ICON_ID,
                rightRow,
                122,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.toggle.worldicon"),
                pngFiles,
                icon));

        //The structure/bonus chest state values
        this.buttonList.add(new GuiConfigStateButton(TOGGLE_BONUSCHEST_ID,
                leftRow,
                143,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.toggle.bonuschest"),
                stateValues,
                quadStateToBtn(activeNode.getField(WorldTypeNode.Fields.BONUS_CHEST_STATE))));
        this.buttonList.add(new GuiConfigStateButton(TOGGLE_STRUCTURES_ID,
                rightRow,
                143,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.toggle.structures"),
                stateValues,
                quadStateToBtn(activeNode.getField(WorldTypeNode.Fields.STRUCTURE_STATE))));

        //special configuration buttons
        this.buttonList.add(new GuiButton(BTN_SET_WORLD_TYPE_ID,
                leftRow,
                90,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.btn.worldtype",
                        ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR))
                                .getValue())
        ));
        this.buttonList.add(new GuiButton(BTN_CUSTOMIZE_WORLD_ID,
                rightRow,
                90,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.btn.customize")
        ));
        this.buttonList.add(new GuiButton(BTN_LOAD_COMMANDS_ID,
                leftRow,
                164,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.lst.commands",
                        ((StringListTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_LOAD_COMMANDS))
                                .getValue().size()
                        )));
        this.buttonList.add(new GuiButton(BTN_DATA_PACKS_ID,
                rightRow,
                164,
                rowWidth,
                18,
                I18n.format("defaultworldgenerator-port.config.lst.datapacks",
                        ((StringListTypeNode)activeNode.getField(WorldTypeNode.Fields.DATA_PACKS))
                                .getValue().size()
                )));

        configNameTextField = new GuiTextField(TXT_WORLD_NAME_ID, fontRenderer, leftRow,37,rowWidth,18);
        configNameTextField.setMaxStringLength(255);
        configNameTextField.setText(((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_NAME)).getValue());
        configNameTextField.setGuiResponder(this);
        configNameTextField.setCursorPositionZero();

        configDescriptionField = new GuiTextField(TXT_DESCRIPTION_ID, fontRenderer, leftRow,69,rowWidth * 2 + 16,18);
        configDescriptionField.setMaxStringLength(512);
        configDescriptionField.setText(((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_DESC)).getValue());
        configDescriptionField.setGuiResponder(this);
        configDescriptionField.setCursorPositionZero();

        configUUIDField = new GuiTextField(TXT_UUID_ID, fontRenderer, rightRow,37,rowWidth,18);
        configUUIDField.setMaxStringLength(64);
        configUUIDField.setText(((UuidTypeNode)activeNode.getField(WorldTypeNode.Fields.UUID)).getValue().toString());
        configUUIDField.setGuiResponder(this);
        configUUIDField.setCursorPositionZero();

        configSeedField = new GuiTextField(TXT_WORLD_SEED_ID, fontRenderer, leftRow,122,rowWidth,18);
        configSeedField.setMaxStringLength(64);
        configSeedField.setText(((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.SEED)).getValue());
        configSeedField.setGuiResponder(this);
        configSeedField.setCursorPositionZero();

        setCustomizeState();
    }

    private void setCustomizeState(){
        boolean enabled = false;
        for(GuiButton b : buttonList){
            if(b.id == BTN_CUSTOMIZE_WORLD_ID){
                String generator = ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue();

                for(WorldType w : WorldType.WORLD_TYPES){
                    if(w != null && w.getName().equals(generator)){
                        enabled = w.isCustomizable();
                    }
                }
                b.enabled = enabled;
            }
        }
    }

    private void launchCustomizeView(){
        String generator = ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.WORLD_GENERATOR)).getValue();

        class GuiFakeNewWorld extends GuiCreateWorld {
            public GuiFakeNewWorld() {
                super(ConfigWorldDataGui.this);
                this.chunkProviderSettingsJson=((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CUSTOMIZATION_STRING)).getValue();
            }

            @Override
            public void initGui() {
                ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CUSTOMIZATION_STRING)).setValue(this.chunkProviderSettingsJson);
                this.mc.displayGuiScreen(ConfigWorldDataGui.this);
            }
        }

        for(WorldType w : WorldType.WORLD_TYPES){
            if(w != null && w.getName().equals(generator)){
                if(w.isCustomizable()) {
                    w.onCustomizeButton(mc, new GuiFakeNewWorld());
                }
            }
        }
    }

    @Override
    public void updateScreen(){
        configNameTextField.updateCursorCounter();
        configDescriptionField.updateCursorCounter();

        if(newUUID != null && !configUUIDField.isFocused()){
            newUUID = null;
            configUUIDField.setText(((UuidTypeNode)activeNode.getField(WorldTypeNode.Fields.UUID)).getValue().toString());
            configUUIDField.setTextColor(0xffffff);
        }

        configUUIDField.updateCursorCounter();
        configSeedField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        configNameTextField.mouseClicked(mouseX, mouseY, mouseButton);
        configDescriptionField.mouseClicked(mouseX, mouseY, mouseButton);
        configUUIDField.mouseClicked(mouseX, mouseY, mouseButton);
        configSeedField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (configNameTextField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        if (configDescriptionField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        if (configUUIDField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        if (configSeedField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        if(keyCode == 1){
            activeNode = new WorldTypeNode(Huffstruct.loadData(origData));
            parent.saveEdit(activeNode);
            mc.displayGuiScreen(parent);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        GuiConfigStateButton toggle;
        switch (button.id){
            case BTN_REVERT_ID:
                activeNode = new WorldTypeNode(Huffstruct.loadData(origData));
                initGui();
                break;
            case BTN_DONE_ID:
                parent.saveEdit(activeNode);
                this.mc.displayGuiScreen(parent);
                break;
            case TOGGLE_IN_LIST_ID:
                toggle=((GuiConfigStateButton)button);
                toggle.itterValue();
                if(toggle.getStateValue().equals(booleanValues[1])) {
                    ((BooleanTypeNode) activeNode.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).setValue(true);
                } else {
                    ((BooleanTypeNode) activeNode.getField(WorldTypeNode.Fields.SHOW_IN_LIST)).setValue(false);
                }
                break;
            case TOGGLE_LOCKTYPE_ID:
                toggle=((GuiConfigStateButton)button);
                toggle.itterValue();
                if(toggle.getStateValue().equals(booleanValues[1])) {
                    ((BooleanTypeNode) activeNode.getField(WorldTypeNode.Fields.LOCK_WORLD_TYPE)).setValue(true);
                } else {
                    ((BooleanTypeNode) activeNode.getField(WorldTypeNode.Fields.LOCK_WORLD_TYPE)).setValue(false);
                }
                break;
            case TOGGLE_ICON_ID:
                toggle=((GuiConfigStateButton)button);
                toggle.itterValue();
                if(!toggle.getStateValue().equals("NONE")){
                    ((StringTypeNode) activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_IMAGE)).setValue(toggle.getStateValue());
                }
                else{
                    ((StringTypeNode) activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_IMAGE)).setValue("");
                }
                break;
            case TOGGLE_STRUCTURES_ID:
                toggle=((GuiConfigStateButton)button);
                toggle.itterValue();
                setQuadStateFromBtn(activeNode.getField(WorldTypeNode.Fields.STRUCTURE_STATE),toggle.getStateValue());
                break;
            case TOGGLE_BONUSCHEST_ID:
                toggle=((GuiConfigStateButton)button);
                toggle.itterValue();
                setQuadStateFromBtn(activeNode.getField(WorldTypeNode.Fields.BONUS_CHEST_STATE),toggle.getStateValue());
                break;
            case BTN_SET_WORLD_TYPE_ID:
                this.mc.displayGuiScreen(
                        new ConfigChooseWorldType(this,activeNode));
                break;
            case BTN_CUSTOMIZE_WORLD_ID:
                launchCustomizeView();
                break;
            case BTN_DATA_PACKS_ID:
                this.mc.displayGuiScreen(
                        new StringListGui(this, activeNode.getField(WorldTypeNode.Fields.DATA_PACKS),
                                I18n.format("defaultworldgenerator-port.config.gui.list.datapacks"),
                                ConfigurationFile::safeFileName));
                break;
            case BTN_LOAD_COMMANDS_ID:
                this.mc.displayGuiScreen(
                        new StringListGui(this, activeNode.getField(WorldTypeNode.Fields.WORLD_LOAD_COMMANDS),
                                I18n.format("defaultworldgenerator-port.config.gui.list.commands"),
                                null));
                break;
        }
    }

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {

    }

    @Override
    public void setEntryValue(int id, String value) {
        switch(id){
            case TXT_WORLD_NAME_ID:
                ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_NAME)).setValue(value);
                break;
            case TXT_DESCRIPTION_ID:
                ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.CONFIGURATION_DESC)).setValue(value);
                break;
            case TXT_UUID_ID:
                newUUID=value;
                try{
                    UUID inputed = UUID.fromString(newUUID);
                    ((UuidTypeNode)activeNode.getField(WorldTypeNode.Fields.UUID)).setValue(inputed);
                    configUUIDField.setTextColor(0xffffff);
                } catch (Exception e){
                    configUUIDField.setTextColor(0xff8888);
                }
                break;
            case TXT_WORLD_SEED_ID:
                ((StringTypeNode)activeNode.getField(WorldTypeNode.Fields.SEED)).setValue(value);
                break;
        }
    }

    private void drawI18n(int x,int y,int color,String key){
        drawString(
                fontRenderer,
                I18n.format(key),
                x,
                y,
                color
                );
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        Gui.drawRect(0,24,width,height,0x55000000);
        drawHorizontalLine(0,width,24,0xff000000);
        drawCenteredString(fontRenderer, title, width / 2, 8, 0xffffff);

        drawI18n(leftRow,26,0xffffff,"defaultworldgenerator-port.config.gui.world.name");
        configNameTextField.drawTextBox();
        drawI18n(leftRow,58,0xffffff,"defaultworldgenerator-port.config.gui.world.desc");
        configDescriptionField.drawTextBox();
        drawI18n(rightRow,26,0xffffff, "defaultworldgenerator-port.config.gui.world.uuid");
        configUUIDField.drawTextBox();
        drawI18n(leftRow,111,0xffffff, "defaultworldgenerator-port.config.gui.world.seed");
        drawI18n(rightRow,111,0xffffff, "defaultworldgenerator-port.config.gui.world.icon");
        configSeedField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
