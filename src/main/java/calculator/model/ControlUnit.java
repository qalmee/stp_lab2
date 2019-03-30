package calculator.model;

import calculator.model.numbers.Number;

public class ControlUnit {
    public static final ControlUnit INSTANCE = new ControlUnit();
    private Processor processor;
    private CalculatorState state;
    private Number resultValue;
    private CalculatorMode calculatorMode;

    private ControlUnit() {
        resetCalculator();
        calculatorMode = CalculatorMode.BASIC;
    }

    public void resetCalculator() {
        processor.reset();
        Memory.INSTANCE.memoryClear();
        state = CalculatorState.START;
    }

    public void equalsPressed(Number valueOnDisplay) {
        switch (state) {
            case START:
                break;
            case FIRST_OPERAND_INPUT:
                break;
            case OPERATOR_SET:
                processor.setRightOperand(valueOnDisplay);
                processor.operationRun();
                state = CalculatorState.EQUALS_PRESSED;
                break;
            case SECOND_OPERAND_INPUT:
                processor.setRightOperand(valueOnDisplay);
                processor.operationRun();
                state = CalculatorState.EQUALS_PRESSED;
                break;
            case EQUALS_PRESSED:
                processor.operationRun();
                break;
        }
        resultValue = processor.getLeftResultOperand();
    }

    public void operatorPressed(Number valueOnDisplay, CalculatorOperation operation) {
        switch (state) {
            case START:
                processor.setLeftResultOperand(valueOnDisplay);
                processor.setOperation(operation);
                state = CalculatorState.OPERATOR_SET;
                break;
            case FIRST_OPERAND_INPUT:
                processor.setLeftResultOperand(valueOnDisplay);
                processor.setOperation(operation);
                state = CalculatorState.OPERATOR_SET;
                break;
            case OPERATOR_SET:
                processor.setOperation(operation);
                break;
            case SECOND_OPERAND_INPUT:
                processor.setRightOperand(valueOnDisplay);
                processor.operationRun();
                processor.setOperation(operation);
                processor.resetRightOperand();
                state = CalculatorState.OPERATOR_SET;
                break;
            case EQUALS_PRESSED:
                processor.setOperation(operation);
                processor.resetRightOperand();
                break;
        }
        resultValue = processor.getLeftResultOperand();
    }

    public void memoryOperationPressed(Number valueOnDisplay, MemoryOperation operation) {
        switch (operation) {
            case MEMORY_ADD:
                Memory.INSTANCE.memoryAdd(valueOnDisplay);
                break;
            case MEMORY_READ:
                resultValue = Memory.INSTANCE.memoryRead();
                break;
            case MEMORY_SAVE:
                Memory.INSTANCE.memorySave(valueOnDisplay);
                break;
            case MEMORY_CLEAR:
                Memory.INSTANCE.memoryClear();
                break;
        }
    }

    public Number getResultValue() {
        return resultValue;
    }

    public CalculatorMode getCalculatorMode() {
        return calculatorMode;
    }

    public void setCalculatorMode(CalculatorMode calculatorMode) {
        this.calculatorMode = calculatorMode;
    }
}
