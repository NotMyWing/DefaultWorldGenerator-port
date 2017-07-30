package com.ezrol.terry.minecraft.defaultworldgenerator.gui;

import net.minecraft.client.gui.GuiButton;

/**
 * Created by ezterry on 7/9/17.
 */
@SuppressWarnings("WeakerAccess")
public class GuiConfigStateButton extends GuiButton {
    private String[] possibleValues;
    private int displayedValue;
    private String prompt;

    public GuiConfigStateButton(int id,int x,int y,int width,int height,String prompt,String[] values,String initalValue){
        super(id,x,y,width,height,prompt);
        possibleValues=values;
        displayedValue=0;

        this.prompt=prompt;
        setStateValue(initalValue);
    }

    public void itterValue(){
        displayedValue++;
        displayedValue= displayedValue % possibleValues.length;
        displayString = prompt + " " + possibleValues[displayedValue];
    }

    public String getStateValue(){
        return possibleValues[displayedValue];
    }

    public void setStateValue(String value){
        for(String v : possibleValues){
            if(v.equals(value)){
                break;
            }
            displayedValue++;
        }
        if(displayedValue >= possibleValues.length){
            displayedValue=0;
        }
        displayString = prompt + " " + possibleValues[displayedValue];
    }
}
