package org.compi.csc311group3.view.controllers;

import javafx.scene.Scene;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class ThemeController {

    //all of the stylesheets
    public enum Theme {
        THEME_ONE("/theme1.css"),
        THEME_TWO("/theme2.css"),
        THEME_THREE("/theme3.css"),
        THEME_FOUR("/theme4.css");

        private final String stylesheet; //current stylesheet applied to all scenes

        Theme(String stylesheet) {
            this.stylesheet = stylesheet;
        }

        public String getStylesheet() {
            return stylesheet;
        }
    }

    private static Theme currentTheme = Theme.THEME_ONE; // Default theme
    private static final List<Scene> registeredScenes = new ArrayList<>();

    //registers the scenes to have their stylesheets changed
    public static void registerScene(Scene scene) {
        registeredScenes.add(scene); //adds scene to list
        applyCurrentTheme(scene); //changes stylesheet
    }

    //applies theme
    public static void applyTheme(Theme theme) {
        currentTheme = theme; //updates current theme variable

        //loops through all scenes in registered scenes list
        for (Scene scene : registeredScenes) {
            applyCurrentTheme(scene); //changes stylesheet of scene
        }
    }

    // Apply the current theme to a given scene
    private static void applyCurrentTheme(Scene scene) {
        scene.getStylesheets().clear(); //clears previous stylesheet
        String stylesheet = currentTheme.getStylesheet();
        //gets the location of the new stylesheet
        URL cssURL = ThemeController.class.getResource("/org/compi/csc311group3/styling" + stylesheet);
        if (cssURL != null) { //makes sure stylesheet is accessible
            scene.getStylesheets().add(cssURL.toExternalForm()); //adds the new stylesheet
        } else {
            System.out.println("Failed to load stylesheet: " + stylesheet); //if stylesheet fails to load, print message.
        }
    }

    //gets the current theme
    public static Theme getCurrentTheme() {
        return currentTheme;
    }

}
