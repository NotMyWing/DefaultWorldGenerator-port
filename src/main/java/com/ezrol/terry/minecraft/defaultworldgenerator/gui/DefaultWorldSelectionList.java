package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import com.ezrol.terry.minecraft.defaultworldgenerator.DefaultWorldGenerator;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.StringTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.config.WorldTypeNode;
import com.ezrol.terry.minecraft.defaultworldgenerator.lib.Log;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Choose your default world settings (on new world button)
 *
 * Created by ezterry on 7/9/17.
 */
public class DefaultWorldSelectionList extends GuiScreen {
    private GuiScreen parent;
    private StringSlotList list=null;
    private String title;
    private final static int BTN_OK = 400;
    private List<WorldTypeNode> choices;

    //the user provided icon cache
    private static HashMap<String,ResourceLocation> userIcons=null;

    public DefaultWorldSelectionList(GuiScreen par, List<WorldTypeNode> validNodes){
        parent=par;
        choices=validNodes;
        title = I18n.format("defaultworldgenerator-port.chooseworld.gui.title");
    }

    @Override
    public void initGui() {
        int idx = (list == null)? 0 : list.selected;
        list = new StringSlotList(width,height);
        list.selected=idx;

        this.buttonList.add(new GuiButton(BTN_OK,
                width/4+20,
                height-28,
                width/2-40,
                18,
                I18n.format("defaultworldgenerator-port.chooseworld.gui.select")));

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case BTN_OK:
                if(list.selected >=0 && list.selected < choices.size()){
                    mc.displayGuiScreen(new GuiCreateCustomWorld(parent,choices.get(list.selected)));
                }
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        list.drawScreen(mouseX,mouseY,partialTicks);

        drawCenteredString(fontRenderer, title, width / 2, 8, 0xffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1){
            mc.displayGuiScreen(parent);
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
            super(DefaultWorldSelectionList.this.mc, screen_width, screen_height-52, 26, 36, 48);
            this.left=0;
            this.right=screen_width;
            this.width=this.right-this.left;
            this.top=26;
            this.bottom=screen_height-36;
            this.height= bottom - (36+26);
        }

        @Override
        protected int getSize() {
            return choices.size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selected = slotIndex;
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return(slotIndex == selected);
        }

        @Override
        protected void drawBackground() {
            DefaultWorldSelectionList.this.drawDefaultBackground();
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        private void internalIcon(int xPos, int yPos, int index) {
            xPos = xPos + 5;
            drawHorizontalLine(xPos - 1, xPos + 32, yPos - 1, 0xffe0e0e0);
            drawHorizontalLine(xPos - 1, xPos + 32, yPos + 32, 0xffa0a0a0);
            drawVerticalLine(xPos - 1, yPos - 1, yPos + 32, 0xffe0e0e0);
            drawVerticalLine(xPos + 32, yPos - 1, yPos + 32, 0xffa0a0a0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            String[] filename = { "grass_side.png", "cobblestone.png", "iron_ore.png", "diamond_ore.png"};


            this.mc.getTextureManager().bindTexture(
                    new ResourceLocation("minecraft",
                            "textures/blocks/" + filename[index % 4]));

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double) (xPos + 0), (double) (yPos + 32), 0.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 32), (double) (yPos + 32), 0.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 32), (double) (yPos + 0), 0.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 0), (double) (yPos + 0), 0.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        private void userIcon(int xPos, int yPos, String image) {
            xPos = xPos + 5;
            drawHorizontalLine(xPos - 1, xPos + 32, yPos - 1, 0xffe0e0e0);
            drawHorizontalLine(xPos - 1, xPos + 32, yPos + 32, 0xffa0a0a0);
            drawVerticalLine(xPos - 1, yPos - 1, yPos + 32, 0xffe0e0e0);
            drawVerticalLine(xPos + 32, yPos - 1, yPos + 32, 0xffa0a0a0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            ResourceLocation drawable;
            if(userIcons == null){
                userIcons = new HashMap<>();
            }

            if(userIcons.containsKey(image)){
                drawable=userIcons.get(image);
            }
            else {
                try {
                    BufferedImage img = ImageIO.read(new File(DefaultWorldGenerator.modSettingsDir, image));
                    drawable = this.mc.getTextureManager().getDynamicTextureLocation(
                            image, new DynamicTexture(img));
                    userIcons.put(image,drawable);
                } catch (Exception e) {
                    Log.error("Unable to read in icon file: " + e);
                    return;
                }
            }

            this.mc.getTextureManager().bindTexture(drawable);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos((double) (xPos + 0), (double) (yPos + 32), 0.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 32), (double) (yPos + 32), 0.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 32), (double) (yPos + 0), 0.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos((double) (xPos + 0), (double) (yPos + 0), 0.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
        }

        @Override
        protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
            String name = ((StringTypeNode)choices.get(entryID).getField(
                    WorldTypeNode.Fields.CONFIGURATION_NAME)).getValue();
            String desc = ((StringTypeNode)choices.get(entryID).getField(
                    WorldTypeNode.Fields.CONFIGURATION_DESC)).getValue();
            String icon = ((StringTypeNode)choices.get(entryID).getField(
                    WorldTypeNode.Fields.CONFIGURATION_IMAGE)).getValue();
            fontRenderer.drawString(name,insideLeft + 50, yPos + 4, 0xeeeeee);
            fontRenderer.drawSplitString(desc,insideLeft + 50, yPos + 14, getListWidth() - 58,0xaaaaaa);
            if(icon == null || icon.equals("")) {
                internalIcon(insideLeft + 4, yPos + 6, entryID);
            }
            else{
                userIcon(insideLeft + 4, yPos + 6, icon);
            }
        }

        @Override
        public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
            super.drawScreen(mouseXIn, mouseYIn, partialTicks);
            overlayBackground(bottom-1,DefaultWorldSelectionList.this.height,255,255);
        }

        @Override
        public int getListWidth(){
            return (width/6)*4;
        }
        @Override
        protected int getScrollBarX()
        {
            return ((width/6)*5)+4;
        }
    }
}
