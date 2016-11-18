package com.ezrol.terry.minecraft.defaultworldgenerator.helper;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@SuppressWarnings("unused")
public class ConfigurationHelper {
    public static String getString(Configuration configuration, String name, String category, String defaultValue,
                                   String comment, String[] validValues) {
        Property property = configuration.get(category, name, defaultValue);
        property.setValidValues(validValues);
        property.setComment(comment + " [default: " + defaultValue + "]");
        String value = property.getString();

        for (String validValue : validValues) {
            if (value.equalsIgnoreCase(validValue)) {
                return validValue;
            }
        }

        return defaultValue;
    }

    public static String getString(Configuration configuration, String name, String category, String defaultValue,
                                   String comment, boolean show) {
        Property property = configuration.get(category, name, defaultValue);
        property.setComment(comment + " [default: " + defaultValue + "]");
        property.setShowInGui(show);
        return property.getString();
    }

    public static String[] getString(Configuration configuration, String name, String category, String[] defaultValue,
                                     String comment) {
        Property property = configuration.get(category, name, defaultValue);
        property.setComment(comment);
        return property.getStringList();
    }

    public static boolean getBoolean(Configuration configuration, String name, String category, boolean defaultValue,
                                     String comment) {
        Property property = configuration.get(category, name, defaultValue);
        property.setComment(comment + " [default: " + defaultValue + "]");
        return property.getBoolean(defaultValue);
    }

    public static int getInt(Configuration configuration, String name, String category, int defaultValue,
                             String comment) {
        Property property = configuration.get(category, name, defaultValue);
        property.setComment(comment + " [default: " + defaultValue + "]");
        return property.getInt(defaultValue);
    }

    public static int[] getInt(Configuration configuration, String name, String category, int[] defaultValue,
                               String comment) {
        Property property = configuration.get(category, name, defaultValue);
        property.setComment(comment);
        return property.getIntList();
    }

    public static Property getProp(Configuration configuration, String name, String category) {
        return configuration.getCategory(category).get(name);
    }
}
