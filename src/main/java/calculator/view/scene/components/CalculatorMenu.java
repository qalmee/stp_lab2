package calculator.view.scene.components;

import calculator.controller.ControllerListener;
import calculator.model.stats.CalculatorMode;
import calculator.view.localization.Language;
import calculator.view.localization.LanguageProperties;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import static calculator.view.localization.LanguageProperties.getProperty;

public class CalculatorMenu extends MenuBar {

    private ControllerListener controllerListener;
    private CalculatorMode calculatorMode;

    private Language selectedLanguage;

    public CalculatorMenu(ControllerListener controllerListener, CalculatorMode calculatorMode) {
        this.controllerListener = controllerListener;
        if (calculatorMode == CalculatorMode.BASIC) {
            calculatorMode = CalculatorMode.P_NUMBER;
        }
        this.calculatorMode = calculatorMode;
        setupMenu();
    }

    private void setupMenu() {
        setupMenuFile();
        setupMenuEdit();
        setupModeMenu();
        setupMenuHelp();
    }

    private Menu generateLanguageMenu() {
        ToggleGroup languageToggleGroup = new ToggleGroup();
        Menu languageMenu = new Menu(getProperty("calculator_scene.menu_language"));
        for (Language language : Language.values()) {
            RadioMenuItem languageMenuItem = new RadioMenuItem(language.getLanguageName());
            languageMenuItem.setToggleGroup(languageToggleGroup);
            languageMenu.getItems().add(languageMenuItem);
            if (LanguageProperties.getLanguage() == language) {
                languageMenuItem.setSelected(true);
                selectedLanguage = language;
            }

            languageMenuItem.setOnAction(event -> {
                RadioMenuItem currentLanguageMenuItem = (RadioMenuItem) event.getSource();
                String languageName = currentLanguageMenuItem.getText();
                if (!languageName.equals(selectedLanguage.getLanguageName())) {
                    changeLanguage(language);
                    selectedLanguage = Language.getLanguageFromLanguageName(languageName);
                }
            });
        }
        return languageMenu;
    }

    private void setupMenuFile() {
        Menu menuFile = new Menu(getProperty("calculator_scene.menu_file"));
        Menu menuLanguage = generateLanguageMenu();
        MenuItem separator = new SeparatorMenuItem();
        MenuItem menuItemExit = new MenuItem(getProperty("calculator_scene.menu_item_exit"));
        menuFile.getItems().add(menuLanguage);
        menuFile.getItems().add(separator);
        menuFile.getItems().addAll(menuItemExit);
        this.getMenus().add(menuFile);

        menuItemExit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        menuItemExit.setOnAction(event -> Platform.exit());
    }

    private void setupMenuEdit() {
        Menu menuEdit = new Menu(getProperty("calculator_scene.menu_edit"));
        MenuItem menuItemCopy = new MenuItem(getProperty("calculator_scene.menu_item_copy"));
        MenuItem menuItemPaste = new MenuItem(getProperty("calculator_scene.menu_item_paste"));
        menuEdit.getItems().addAll(menuItemCopy, menuItemPaste);
        this.getMenus().add(menuEdit);

        menuItemCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        menuItemPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
        menuItemCopy.setOnAction(event -> controllerListener.buttonCopyClicked());
        menuItemPaste.setOnAction(event -> controllerListener.buttonPasteClicked());
    }

    private void setupModeMenu() {
        ToggleGroup modeToggleGroup = new ToggleGroup();
        Menu menuMode = new Menu(getProperty("calculator_scene.menu_mode"));
        menuMode.setId("menuMode");
//        RadioMenuItem menuItemBasic = new RadioMenuItem(getProperty("calculator_scene.menu_item_mode_basic"));
        RadioMenuItem menuItemFraction = new RadioMenuItem(getProperty("calculator_scene.menu_item_mode_fraction"));
        menuItemFraction.setId("menuItemFraction");
        RadioMenuItem menuItemComplex = new RadioMenuItem(getProperty("calculator_scene.menu_item_mode_complex"));
        menuItemComplex.setId("menuItemComplex");
        RadioMenuItem menuItemPNumber = new RadioMenuItem(getProperty("calculator_scene.menu_item_mode_p-value"));
        menuItemPNumber.setId("menuItemPNumber");
        menuMode.getItems().addAll(/*menuItemBasic,*/ menuItemFraction, menuItemComplex, menuItemPNumber);
        this.getMenus().add(menuMode);

//        menuItemBasic.setToggleGroup(modeToggleGroup);
        menuItemFraction.setToggleGroup(modeToggleGroup);
        menuItemComplex.setToggleGroup(modeToggleGroup);
        menuItemPNumber.setToggleGroup(modeToggleGroup);

//        menuItemBasic.setOnAction(event -> controllerListener.updateCalculatorMode(CalculatorMode.BASIC));
        menuItemFraction.setOnAction(event -> controllerListener.updateCalculatorMode(CalculatorMode.FRACTION));
        menuItemComplex.setOnAction(event -> controllerListener.updateCalculatorMode(CalculatorMode.COMPLEX));
        menuItemPNumber.setOnAction(event -> controllerListener.updateCalculatorMode(CalculatorMode.P_NUMBER));

        switch (calculatorMode) {
//            case BASIC:
//                menuItemBasic.setSelected(true);
//                break;
            case FRACTION:
                menuItemFraction.setSelected(true);
                break;
            case COMPLEX:
                menuItemComplex.setSelected(true);
                break;
            case P_NUMBER:
                menuItemPNumber.setSelected(true);
                break;
            default:
                break;
        }
    }

    private void setupMenuHelp() {
        Menu menuHelp = new Menu(getProperty("calculator_scene.menu_help"));
        MenuItem menuItemHelp = new MenuItem(getProperty("calculator_scene.menu_item_help"));
        MenuItem menuItemAbout = new MenuItem(getProperty("calculator_scene.menu_item_about"));
        MenuItem separator = new SeparatorMenuItem();
        menuHelp.getItems().addAll(menuItemHelp, separator, menuItemAbout);
        this.getMenus().add(menuHelp);

        menuItemHelp.setOnAction(event -> createInformationAlert(getProperty("calculator_scene.help_alert_title"),
                getProperty("calculator_scene.help_alert_message")));
        menuItemAbout.setOnAction(event -> createInformationAlert(getProperty("calculator_scene.about_alert_title"),
                getProperty("calculator_scene.about_alert_message")));
    }

    private void changeLanguage(Language language) {
        controllerListener.updateLanguage(language);
        createInformationAlert(getProperty("calculator_scene.restart_alert_title"),
                getProperty("calculator_scene.restart_alert_message"));
    }

    private void createInformationAlert(String title, String contentText) {
        Alert needRestartAlert = new Alert(Alert.AlertType.INFORMATION);
        needRestartAlert.setTitle(title);
        needRestartAlert.setHeaderText(null);
        needRestartAlert.setContentText(contentText);
        needRestartAlert.showAndWait();
    }
}
