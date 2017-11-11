package com.ezrol.terry.minecraft.defaultworldgenerator.integration;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.IOException;

public class WrongPackModeGui extends GuiScreen {
    private String newMode;

    public WrongPackModeGui(String newMode)
    {
        this.newMode = newMode;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 38, I18n.format("gui.done")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled && button.id == 0)
        {
            FMLClientHandler.instance().haltGame("Restart required to activate pack mode: " + newMode,
                    new RuntimeException("Please Restart Minecraft"));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1){
            FMLClientHandler.instance().haltGame("Restart required to activate pack mode: " + newMode,
                    new RuntimeException("Please Restart Minecraft"));
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        String query = I18n.format("defaultworldgenerator-port.newworld.gui.wrongpackmode");
        query +="\n" + newMode;

        String[] lines = query.split("\n");

        int spaceAvailable = this.height - 38 - 20;
        int spaceRequired = Math.min(spaceAvailable, 10 + 10 * lines.length);

        int offset = 10 + (spaceAvailable - spaceRequired) / 2; // vertically centered

        for (String line : lines)
        {
            if (offset >= spaceAvailable)
            {
                this.drawCenteredString(this.fontRenderer, "...", this.width / 2, offset, 0xFFFFFF);
                break;
            }
            else
            {
                if (!line.isEmpty()) this.drawCenteredString(this.fontRenderer, line, this.width / 2, offset, 0xFFFFFF);
                offset += 10;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
