package calculator.controller;

import calculator.model.CalculatorModel;
import calculator.model.memory.MemoryOperation;
import calculator.model.observer.CalculatorObserver;
import calculator.model.observer.ComplexCalculatorObserver;
import calculator.model.observer.FractionCalculatorObserver;
import calculator.model.observer.PNumberCalculatorObserver;
import calculator.model.stats.CalculatorMode;
import calculator.model.stats.CalculatorOperation;
import calculator.view.localization.Language;

public class Controller implements ControllerListener {

    private CalculatorModel calculatorModel;

    public Controller(CalculatorModel calculatorModel) {
        this.calculatorModel = calculatorModel;
    }

    @Override
    public void setNewObserver(CalculatorObserver calculatorObserver) {
        calculatorModel.setCalculatorObserver(calculatorObserver);
    }

    @Override
    public void setFractionCalculatorObserver(FractionCalculatorObserver fractionCalculatorObserver) {
        calculatorModel.setFractionCalculatorObserver(fractionCalculatorObserver);
    }

    @Override
    public void setComplexCalculatorObserver(ComplexCalculatorObserver complexCalculatorObserver) {
        calculatorModel.setComplexCalculatorObserver(complexCalculatorObserver);
    }

    @Override
    public void setPNumberCalculatorObserver(PNumberCalculatorObserver pNumberCalculatorObserver) {
        calculatorModel.setPNumberCalculatorObserver(pNumberCalculatorObserver);
    }

    @Override
    public void setNewBase(int newBase) {
        calculatorModel.setBase(newBase);
    }

    @Override
    public void updateDigitButtons(int base) {
        calculatorModel.updateDigitButtons(base);
    }

    @Override
    public void checkPastedValue(String value, CalculatorMode calculatorMode) {
        calculatorModel.pasteFromClipboard(value, calculatorMode);
    }

    @Override
    public void convertValue(String value, int currentBase, int newBase) {
        calculatorModel.convertAll(value, currentBase, newBase);
    }

    @Override
    public void updateLanguage(Language language) {
        calculatorModel.setLanguageToConfig(language);
    }

    @Override
    public void updateCalculatorMode(CalculatorMode calculatorMode) {
        calculatorModel.setCalculatorModeToConfig(calculatorMode);
    }

    @Override
    public void actionButtonClicked(String number, CalculatorOperation operation, CalculatorMode mode) {
        calculatorModel.operationPressed(number, operation, mode);
    }

    @Override
    public void memoryButtonClicked(String number, MemoryOperation memoryOperation, CalculatorMode mode) {
        calculatorModel.memoryOperationPressed(number, memoryOperation, mode);
    }

    @Override
    public void buttonEnterClicked(String number, CalculatorMode mode) {
        calculatorModel.equalsPressed(number, mode);
    }

    @Override
    public void buttonDigitClicked() {
        calculatorModel.displayTextActionHappened();
    }

    @Override
    public void buttonClearEntryClicked(CalculatorMode calculatorMode) {
        calculatorModel.clearEntry(calculatorMode);
    }

    @Override
    public void buttonGlobalClearClicked() {
        calculatorModel.clear();
    }

    @Override
    public void buttonCopyClicked() {
        calculatorModel.copyValueToClipboard();
    }

    @Override
    public void buttonPasteClicked() {
        calculatorModel.pasteValueFromClipboard();
    }
}
