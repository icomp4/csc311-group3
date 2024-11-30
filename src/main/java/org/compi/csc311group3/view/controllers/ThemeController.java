package org.compi.csc311group3.view.controllers;

import javafx.scene.Scene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThemeController {

    public enum Theme {
        THEME_ONE("/theme1.css"),
        THEME_TWO("/theme2.css"),
        THEME_THREE("/theme3.css"),
        THEME_FOUR("/theme4.css");

        private final String stylesheet;

        Theme(String stylesheet) {
            this.stylesheet = stylesheet;
        }

        public String getStylesheet() {
            return stylesheet;
        }
    }

    private static Theme currentTheme = Theme.THEME_ONE; // Default theme
    private static final List<Scene> registeredScenes = new ArrayList<>();

    // Register scenes to be updated
    public static void registerScene(Scene scene) {
        registeredScenes.add(scene);
        applyCurrentTheme(scene);
    }

    // Apply a specific theme
    public static void applyTheme(Theme theme) {
        currentTheme = theme;
        System.out.println("Applying theme: " + currentTheme);  // Debugging output
        // Apply the theme to all registered scenes
        for (Scene scene : registeredScenes) {
            applyCurrentTheme(scene);
        }
    }

    // Apply the current theme to a given scene
    private static void applyCurrentTheme(Scene scene) {
        System.out.println("Clearing and applying theme to scene...");  // Debugging output
        scene.getStylesheets().clear(); // Clear previous styles
        String stylesheet = currentTheme.getStylesheet();
        URL cssURL = ThemeController.class.getResource("/org/compi/csc311group3/styling" + stylesheet);
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm()); // Add the new stylesheet
            System.out.println("Applied stylesheet: " + cssURL.toExternalForm());  // Debugging output
        } else {
            System.out.println("Failed to load stylesheet: " + stylesheet);  // Debugging output
        }
    }

    // Get the current theme
    public static Theme getCurrentTheme() {
        return currentTheme;
    }

}
