package calculator;

import calculator.controller.Controller;
import calculator.model.CalculatorModel;
import calculator.view.scene.CalculatorScene;
import calculator.view.window.CalculatorWindow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;

public class MainTestComplex extends ApplicationTest {
    CalculatorModel calculatorModel;
    Controller controller;
    CalculatorScene calculatorScene;
    Stage calculatorWindow;

    @BeforeClass
    public static void setupSpec() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Override
    public void start(Stage stage) throws Exception {
        calculatorModel = new CalculatorModel();
        controller = new Controller(calculatorModel);

        calculatorScene = new CalculatorScene();
        calculatorScene.setControllerListener(controller);

        calculatorModel.setCalculatorObserver(calculatorScene);
        calculatorModel.readLanguageFromConfig();

        calculatorScene.initializeScene();

        calculatorWindow = new CalculatorWindow();
        calculatorWindow.setScene(calculatorScene);
        calculatorModel.readConfigInformation();

        calculatorWindow.show();
    }

    @Before
    public void setUp() throws Exception {
        clickOn("#menuMode");
//        menuItemFraction.setId("menuItemFraction");
//        menuItemComplex.setId("menuItemComplex");
//        menuItemPNumber.setId("menuItemPNumber");
        clickOn("#menuItemComplex");
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testCalc2() {
        clickOn("#textFieldValue");
        press(KeyCode.DIGIT5);
        press(KeyCode.DIGIT3);
        press(KeyCode.DIGIT4);
        press(KeyCode.DIGIT7);
        clickOn("#multiply");
        press(KeyCode.DIGIT1);
        press(KeyCode.DIGIT2);
        press(KeyCode.DIGIT9);
        press(KeyCode.DIGIT0);
        clickOn("#equals");
        assertEquals("6897630+0i", calculatorModel.getValue());
    }

    @Test
    public void testCalc3() {
        clickOn("#textFieldValue");
        press(KeyCode.DIGIT5);
        press(KeyCode.DIGIT3);
        press(KeyCode.DIGIT4);
        press(KeyCode.DIGIT7);
        clickOn("#divide");
        press(KeyCode.DIGIT1);
        press(KeyCode.DIGIT2);
        press(KeyCode.DIGIT9);
        press(KeyCode.DIGIT0);
        clickOn("#equals");
        assertEquals("4,1449612486+0i", calculatorModel.getValue());
    }

    @Test
    public void testCalc4() {
        clickOn("#textFieldValue");
        press(KeyCode.DIGIT1);
        press(KeyCode.DIGIT2);
        clickOn("#switch");
        press(KeyCode.DIGIT3);
        clickOn("#multiply");
        press(KeyCode.DIGIT5);
        clickOn("#switch");
        press(KeyCode.DIGIT9);
        clickOn("#equals");
        assertEquals("33+123i", calculatorModel.getValue());
    }
}
