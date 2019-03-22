package calculator.model;

import calculator.model.numbers.Number;

public class Processor {

    public static final Processor INSTANCE = new Processor();
    private CalculatorMode calculatorMode;
    private CalculatorOperation operation;
    private CalculatorState state;

    private Number leftResultOperand;
    private Number rightOperand;

    private Processor() {

    }

    void Reset() {

    }

    public void operationRun() {
        //todo
    }

    public CalculatorMode getCalculatorMode() {
        return calculatorMode;
    }

    public void setCalculatorMode(CalculatorMode calculatorMode) {
        this.calculatorMode = calculatorMode;
    }

    public Number getLeftResultOperand() {
        return leftResultOperand;
    }

    public void setLeftResultOperand(Number leftResultOperand) {
        this.leftResultOperand = leftResultOperand;
    }

    public Number getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Number rightOperand) {
        this.rightOperand = rightOperand;
    }

    public CalculatorOperation getOperation() {
        return operation;
    }

    public void setOperation(CalculatorOperation operation) {
        this.operation = operation;
    }

    public CalculatorState getState() {
        return state;
    }

    public void setState(CalculatorState state) {
        this.state = state;
    }
}
