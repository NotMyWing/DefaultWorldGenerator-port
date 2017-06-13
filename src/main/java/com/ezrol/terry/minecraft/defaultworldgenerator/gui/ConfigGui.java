package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigGeneralSettings;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.ConfigurationFile;
import com.ezrol.terry.minecraft.defaultworldgenerator.helper.ConfigurationHelper;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Reference;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.ButtonEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Gui for the configuration screen including custom widgets
 **/

@SuppressWarnings("WeakerAccess")
public class ConfigGui extends GuiConfig {
    protected String CurrentWorldType = "";

    public ConfigGui(GuiScreen parent) {
        super(parent, initList(), Reference.MOD_ID, true, false, I18n.format(Reference.MOD_ID + ".config.gui.title"));
        CurrentWorldType = ConfigGeneralSettings.cfgWorldGenerator;
    }

    public static List<IConfigElement> initList() {
        List<IConfigElement> lst = new ArrayList<>();
        Configuration configuration = ConfigurationFile.configuration;
        Property prop;

        prop = ConfigurationHelper.getProp(configuration, "Seed", "general");
        lst.add(new ConfigElement(prop));

        prop = ConfigurationHelper.getProp(configuration, "World Generator", "general");
        prop.setConfigEntryClass(WorldTypeEntries.class);
        lst.add(new ConfigElement(prop));

        prop = ConfigurationHelper.getProp(configuration, "CustomizationJson", "general");
        prop.setConfigEntryClass(CustomizeButton.class);
        lst.add(new ConfigElement(prop));

        prop = ConfigurationHelper.getProp(configuration, "Lock Worldtype", "general");
        lst.add(new ConfigElement(prop));

        prop = ConfigurationHelper.getProp(configuration, "Bonus Chest State", "general");
        prop.setConfigEntryClass(BonusChestState.class);
        lst.add(new ConfigElement(prop));

        prop = ConfigurationHelper.getProp(configuration, "Copy DefaultWorldData", "general");
        lst.add(new ConfigElement(prop));

        return lst;
    }

    public static class BonusChestState extends GuiConfigEntries.SelectValueEntry {
        public BonusChestState(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
                                IConfigElement configElement) {

            super(owningScreen, owningEntryList, configElement, getTypeMap());
        }

        /**
         * Get the list of world types to choose between
         **/
        private static Map<Object, String> getTypeMap() {
            Map m = new ConcurrentSkipListMap<Integer, String>();

            m.put(0,I18n.format("defaultworldgenerator-port.config.gui.bc_disable"));
            m.put(1,I18n.format("defaultworldgenerator-port.config.gui.bc_enabled"));
            m.put(2,I18n.format("defaultworldgenerator-port.config.gui.bc_blocked"));
            m.put(3,I18n.format("defaultworldgenerator-port.config.gui.bc_forced"));

            return (m);
        }

        @Override
        public void updateValueButtonText() {
            super.updateValueButtonText();
            if (this.owningScreen instanceof ConfigGui) {
                ((ConfigGui) this.owningScreen).CurrentWorldType = this.currentValue.toString();
            }
        }
    }

    /**
     * class to list the world types
     **/
    public static class WorldTypeEntries extends GuiConfigEntries.SelectValueEntry {
        public WorldTypeEntries(GuiConfig owningScreen, GuiConfigEntries owningEntryList,
                                IConfigElement configElement) {

            super(owningScreen, owningEntryList, configElement, getTypeMap());
        }

        /**
         * Get the list of world types to choose between
         **/
        private static Map<Object, String> getTypeMap() {
            Map m = new ConcurrentSkipListMap<String, String>();
            String s;
            for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
                if (WorldType.WORLD_TYPES[i] != null && WorldType.WORLD_TYPES[i].canBeCreated()) {
                    s = I18n.format("selectWorld.mapType") + " "
                            + I18n.format(WorldType.WORLD_TYPES[i].getTranslateName());
                    //noinspection unchecked
                    m.put(WorldType.WORLD_TYPES[i].getName(), s);
                }
            }
            return (m);
        }

        @Override
        public void updateValueButtonText() {
            super.updateValueButtonText();
            if (this.owningScreen instanceof ConfigGui) {
                ((ConfigGui) this.owningScreen).CurrentWorldType = this.currentValue.toString();
            }
        }
    }

    /**
     * class for editing the customization string
     **/
    @SuppressWarnings("WeakerAccess")
    public static class CustomizeButton extends ButtonEntry {
        protected final String beforeValue;
        protected Object currentValue;
        @SuppressWarnings("CanBeFinal")
        protected ConfigGui ourOwner;

        public CustomizeButton(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
            ourOwner = (ConfigGui) owningScreen;
            beforeValue = configElement.get().toString();
            currentValue = configElement.get().toString();
            updateValueButtonText();
        }

        @Override
        public void updateValueButtonText() {
            this.btnValue.displayString = currentValue.toString();
        }

        @Override
        public void valueButtonPressed(int slotIndex) {
            String type = ourOwner.CurrentWorldType;
            WorldType worldtype = null;

            for (int i = 0; i < WorldType.WORLD_TYPES.length; i++) {
                if (WorldType.WORLD_TYPES[i] != null && WorldType.WORLD_TYPES[i].canBeCreated()) {
                    if (WorldType.WORLD_TYPES[i].getName().equalsIgnoreCase(type)) {
                        worldtype = WorldType.WORLD_TYPES[i];
                        break;
                    }
                }
            }

            if (worldtype != null && worldtype.isCustomizable()) {
                GuiFakeNewWorld fakegui = new GuiFakeNewWorld(ourOwner, this);

                fakegui.chunkProviderSettingsJson = currentValue.toString();
                //noinspection ConstantConditions
                worldtype.onCustomizeButton(mc, fakegui);
            } else {
                setValueFromChildScreen("");
            }

        }

        public void setValueFromChildScreen(Object newValue) {
            if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null) {
                currentValue = newValue;
                updateValueButtonText();
            }
        }

        @Override
        public boolean isDefault() {
            if (configElement.getDefault() != null)
                return configElement.getDefault().equals(currentValue);
            else
                return currentValue == null;
        }

        @Override
        public void setToDefault() {
            if (enabled()) {
                this.currentValue = configElement.getDefault().toString();
                updateValueButtonText();
            }
        }

        @Override
        public boolean isChanged() {
            if (beforeValue != null)
                return !beforeValue.equals(currentValue);
            else
                return currentValue == null;
        }

        @Override
        public void undoChanges() {
            if (enabled()) {
                currentValue = beforeValue;
                updateValueButtonText();
            }
        }

        @Override
        public boolean saveConfigElement() {
            if (enabled() && isChanged()) {
                this.configElement.set(currentValue);
                return configElement.requiresMcRestart();
            }
            return false;
        }

        @Override
        public String getCurrentValue() {
            return this.currentValue.toString();
        }

        @Override
        public String[] getCurrentValues() {
            return new String[]{getCurrentValue()};
        }
    }

    /**
     * Class for a fake new world screen.. needed due to how customization is
     * implemented :/
     **/

    public static class GuiFakeNewWorld extends GuiCreateWorld {
        @SuppressWarnings("CanBeFinal")
        private GuiConfig par;
        @SuppressWarnings("CanBeFinal")
        private CustomizeButton btn;

        public GuiFakeNewWorld(GuiConfig owningScreen, CustomizeButton btn) {
            super(owningScreen);

            this.par = owningScreen;
            this.btn = btn;
        }

        @Override
        public void initGui() {

            btn.setValueFromChildScreen(this.chunkProviderSettingsJson);
            this.mc.displayGuiScreen(par);
        }
    }
}
