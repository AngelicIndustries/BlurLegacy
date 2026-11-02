package com.tterrag.blur.config;

import java.io.File;
import com.tterrag.blur.Blur;
import com.tterrag.blur.animation.Transition.Easing;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public final class BlurConfig {
    public static Configuration file;
    public static boolean enabled, useGradient, containers, books, signs, commandBlocks, death, title, darkenTitle;
    public static boolean reduceInGameBlur, showScreenID, rainbow;
    public static int fadeIn, fadeOut, radius, downsample, gradientStart, gradientEnd, rotation;
    public static String[] exclusions, inclusions;
    public static Easing blurCurve, backgroundCurve;
    private BlurConfig() {}
    public static void load(File path) { file = new Configuration(path); sync(); }
    public static void sync() {
        enabled = bool("general", "enabled", true, "Enable menu blur and its custom background.");
        radius = integer("general", "radius", 12, 0, 100, "Blur radius in screen pixels. Zero disables blur.");
        fadeIn = integer("general", "fadeTime", 200, 0, 2000, "Opening transition in milliseconds. Zero is instant.");
        fadeOut = integer("general", "fadeOutTime", 200, 0, 2000, "Closing transition in milliseconds. Zero is instant.");
        exclusions = file.getStringList("guiExclusions", "general", new String[] { "net.minecraft.client.gui.GuiChat", "net.coderbot.iris.gui.screen.ShaderPackScreen" },
            "Screen class names to exclude, including subclasses. Exclusions take priority over inclusions.");
        inclusions = file.getStringList("forceEnabledScreens", "screens", new String[] { "codechicken.nei.recipe.GuiRecipe" },
            "Screen class names to blur regardless of category, including subclasses.");
        containers = bool("screens", "blurContainers", true, "Blur inventories and other containers.");
        books = bool("screens", "blurBooks", true, "Blur book screens.");
        signs = bool("screens", "blurSigns", true, "Blur sign editing.");
        commandBlocks = bool("screens", "blurCommandBlocks", true, "Blur command block editing.");
        death = bool("screens", "blurDeathScreen", false, "Blur the death screen.");
        title = bool("screens", "blurTitleScreen", false, "Apply extra blur to the vanilla title panorama.");
        darkenTitle = bool("screens", "darkenTitleScreen", false, "Apply the custom gradient to the title panorama.");
        reduceInGameBlur = bool("screens", "reduceInGameBlur", false, "Halve the blur strength behind containers.");
        showScreenID = bool("screens", "showScreenID", false, "Display the current screen class for configuring exclusions.");
        useGradient = bool("style", "useGradient", true, "Replace the vanilla menu darkening with a custom gradient.");
        gradientStart = color("gradientStartColor", "75000000");
        gradientEnd = color("gradientEndColor", "75000000");
        rotation = integer("style", "gradientRotation", 0, 0, 360, "Gradient rotation in degrees.");
        rainbow = bool("style", "rainbowMode", false, "Cycle gradient hues while keeping their configured opacity.");
        blurCurve = curve("blurAnimationCurve");
        backgroundCurve = curve("backgroundAnimationCurve");
        downsample = integer("performance", "downsample", 0, 0, 4,
            "0 selects automatically, 1 uses full resolution, 2 halves it, 4 quarters it. 3 is treated as 2.");
        for (String category : file.getCategoryNames()) {
            file.setCategoryLanguageKey(category, "blur.config.category." + category);
            for (Property p : file.getCategory(category).values()) p.setLanguageKey("blur.config." + p.getName());
        }
        if (file.hasChanged()) file.save();
    }
    private static boolean bool(String category, String key, boolean value, String comment) {
        return file.getBoolean(key, category, value, comment);
    }
    private static int integer(String category, String key, int value, int min, int max, String comment) {
        return file.getInt(key, category, value, min, max, comment);
    }
    private static int color(String key, String fallback) {
        String value = file.getString(key, "general", fallback, "Background color in eight-digit ARGB hex, e.g. 75000000.");
        try { return parseColor(value); }
        catch (IllegalArgumentException invalid) {
            Blur.LOG.warn("Invalid {} value '{}'; using {}", key, value, fallback);
            return parseColor(fallback);
        }
    }
    public static int parseColor(String value) {
        String hex = value.trim().replaceFirst("^(#|0[xX])", "");
        if (!hex.matches("[0-9a-fA-F]{8}")) throw new IllegalArgumentException("Expected eight ARGB hex digits");
        return (int) Long.parseLong(hex, 16);
    }
    private static Easing curve(String key) {
        String value = file.getString(key, "style", "SINE", "Transition curve.", new String[] { "LINEAR", "SINE", "QUAD", "CUBIC" });
        try { return Easing.valueOf(value); } catch (IllegalArgumentException invalid) { return Easing.SINE; }
    }
}
