package calculator.view.scene;

import calculator.model.observer.ComplexCalculatorObserver;
import calculator.model.stats.CalculatorMode;
import calculator.model.stats.CalculatorOperation;
import calculator.view.scene.components.CalculatorButtons;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

import java.util.List;
import java.util.regex.Pattern;

import static calculator.view.localization.LanguageProperties.getProperty;

class ComplexCalculatorScene extends CalculatorScene implements ComplexCalculatorObserver {

    private static final int TEXT_FIELD_VALUE_MAX_INPUT_TEXT_LENGTH = 25;
    private static final String I_BUTTON_ACTIVE_STYLE = "i_button_active";

    private static final String START_REAL_PART;
    private static final String START_IMAGINARY_PART;
    private static final String I_SYMBOL;

    static {
        START_REAL_PART = CalculatorMode.COMPLEX.getStartValue().split(Pattern.quote("+"))[0];
        START_IMAGINARY_PART = CalculatorMode.COMPLEX.getStartValue().split(Pattern.quote("+"))[1];
        I_SYMBOL = CalculatorButtons.BUTTON_I.getButton().getText();
    }

    private ComplexCaretPosition caretPosition;

    ComplexCalculatorScene() {
        super(CalculatorMode.COMPLEX);
    }

    @Override
    public void setCaretToRealPart() {
        caretPosition = ComplexCaretPosition.REAL;
        Button iButton = CalculatorButtons.BUTTON_I.getButton();
        iButton.getStyleClass().remove(I_BUTTON_ACTIVE_STYLE);
    }

    @Override
    public void initializeScene() {
        super.initializeScene();
        setupIButton();
        setupComplexActionButtons();
        addTooltipsToComplexButtons();
        controllerListener.setComplexCalculatorObserver(this);
        caretPosition = ComplexCaretPosition.REAL;
    }

    @Override
    void appendComaDigitToTextFieldValue() {
        String currentRealPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[0];
        String currentImaginaryPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[1];
        String commaSymbol = CalculatorButtons.BUTTON_COMMA.getButton().getText();

        if (caretPosition == ComplexCaretPosition.REAL) {
            if (!currentRealPart.contains(commaSymbol)) {
                currentRealPart = currentRealPart + commaSymbol;
            }
        } else {
            if (!currentImaginaryPart.contains(commaSymbol)) {
                String imaginaryPartWithoutI = currentImaginaryPart.substring(0, currentImaginaryPart.length() - 1);
                currentImaginaryPart = imaginaryPartWithoutI + commaSymbol + I_SYMBOL;
            }
        }
        textFieldValueSetText(currentRealPart + "+" + currentImaginaryPart);
    }

    @Override
    void appendDigitToTextFieldValue(String digitText) {
        String currentRealPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[0];
        String currentImaginaryPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[1];

        if (currentRealPart.length() + currentImaginaryPart.length() > TEXT_FIELD_VALUE_MAX_INPUT_TEXT_LENGTH) {
            textFieldValueSetText(currentRealPart + "+" + currentImaginaryPart);
            return;
        }

        if (caretPosition == ComplexCaretPosition.REAL) {
            if (currentRealPart.equals(START_REAL_PART)) {
                currentRealPart = digitText;
            } else {
                currentRealPart = currentRealPart + digitText;
            }
        } else {
            if (currentImaginaryPart.equals(START_IMAGINARY_PART)) {
                currentImaginaryPart = digitText + I_SYMBOL;
            } else {
                String imaginaryPartWithoutI = currentImaginaryPart.substring(0, currentImaginaryPart.length() - 1);
                currentImaginaryPart = imaginaryPartWithoutI + digitText + I_SYMBOL;
            }
        }
        textFieldValueSetText(currentRealPart + "+" + currentImaginaryPart);
    }

    @Override
    void backspaceClickedAction() {
        String currentRealPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[0];
        String currentImaginaryPart = getValueFromTextFieldValue().split(Pattern.quote("+"))[1];

        if (caretPosition == ComplexCaretPosition.REAL) {
            currentRealPart = currentRealPart.substring(0, currentRealPart.length() - 1);
            if (currentRealPart.isEmpty()) {
                currentRealPart = START_REAL_PART;
            }
        } else {
            currentImaginaryPart = currentImaginaryPart.substring(0, currentImaginaryPart.length() - 2) + I_SYMBOL;
            if (currentImaginaryPart.equals(I_SYMBOL)) {
                currentImaginaryPart = START_IMAGINARY_PART;
            }
        }
        textFieldValueSetText(currentRealPart + "+" + currentImaginaryPart);
    }

    private void setupIButton() {
        Button iButton = CalculatorButtons.BUTTON_I.getButton();
        iButton.setId("switch");
        iButton.setOnAction(event -> {
            if (caretPosition == ComplexCaretPosition.REAL) {
                caretPosition = ComplexCaretPosition.IMAGINARY;
                iButton.getStyleClass().add(I_BUTTON_ACTIVE_STYLE);
            } else {
                caretPosition = ComplexCaretPosition.REAL;
                iButton.getStyleClass().remove(I_BUTTON_ACTIVE_STYLE);
            }
        });
    }

    private void setupComplexActionButtons() {
        List<CalculatorButtons> actionButtons = CalculatorButtons.getComplexActionButtons();
        actionButtons.forEach(calculatorButton -> {
            Button button = calculatorButton.getButton();
            button.setOnAction(event -> {
                String number = getValueFromTextFieldValue();
                CalculatorOperation calculatorOperation =
                        CalculatorButtons.getCalculatorOperationFromComplexButton(button);
                controllerListener.actionButtonClicked(number, calculatorOperation, calculatorMode);
            });
        });
    }

    private void addTooltipsToComplexButtons() {
        Button buttonModule = CalculatorButtons.BUTTON_MODULE.getButton();
        Button buttonArgumentRad = CalculatorButtons.BUTTON_ARGUMENT_RAD.getButton();
        Button buttonArgumentDeg = CalculatorButtons.BUTTON_ARGUMENT_DEG.getButton();
        Button buttonPow = CalculatorButtons.BUTTON_POW.getButton();
        Button buttonSqr = CalculatorButtons.BUTTON_SQR.getButton();

        buttonModule.setTooltip(new Tooltip(getProperty("complex_calculator_scene.tooltip_button_module")));
        buttonArgumentRad.setTooltip(new Tooltip(getProperty("complex_calculator_scene.tooltip_button_argument_rad")));
        buttonArgumentDeg.setTooltip(new Tooltip(getProperty("complex_calculator_scene.tooltip_button_argument_deg")));
        buttonPow.setTooltip(new Tooltip(getProperty("complex_calculator_scene.tooltip_button_pow")));
        buttonSqr.setTooltip(new Tooltip(getProperty("complex_calculator_scene.tooltip_button_sqr")));
    }

    private enum ComplexCaretPosition {REAL, IMAGINARY}
}
