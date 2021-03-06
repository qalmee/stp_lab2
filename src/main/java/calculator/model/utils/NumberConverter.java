package calculator.model.utils;

import calculator.model.numbers.Complex;
import calculator.model.numbers.Fraction;
import calculator.model.numbers.Number;
import calculator.model.numbers.Real;
import calculator.model.stats.CalculatorMode;
import calculator.model.stats.CalculatorOverflow;
import calculator.model.utils.exceptions.OverflowException;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter {

    private NumberConverter() {

    }

    private static final int OVERFLOW_LENGTH = CalculatorOverflow.REAL_OVERFLOW.getLength();
    private static final int OVERFLOW_LENGTH_COMPLEX = CalculatorOverflow.COMPLEX_OVERFLOW.getLength();

    private static String fromScientific(String value, CalculatorMode calculatorMode) {
        switch (calculatorMode) {
            case BASIC:
            case P_NUMBER:
                return realFromScientific(value);
            case FRACTION:
                Pair<String, String> fraction = stringToNumeratorAndDenominator(value);
                String numerator = realFromScientific(fraction.getKey());
                String denominator = realFromScientific(fraction.getValue());
                return numerator + "/" + denominator;
            default:
                return value;
        }
    }

    private static String realFromScientific(String value) {
        String[] arr = value.split("exp");
        if (arr.length > 2) {
            throw new IllegalArgumentException("Value has more than one Exp");
        }
        if (arr.length == 1) {
            return value;
        }
        String isNegative = "";
        if (arr[0].startsWith("-")) {
            isNegative = "-";
            arr[0] = arr[0].substring(1);
        }
        int power = Integer.parseInt(arr[1]);
        if (arr[0].length() > 1 && arr[0].charAt(1) != '.' && arr[0].contains(".")) {
            throw new IllegalArgumentException("Value must be in 0.00exp00 format");
        }
        if (arr[0].length() == 1 && arr[0].charAt(0) == '.') {
            throw new IllegalArgumentException("Value must be in 0.00exp00 format");
        }
        if (!arr[0].contains(".")) {
            power += arr[0].length() - 1;
        } else {
            arr[0] = arr[0].replace(".", "");
        }
        if (power >= 0) {
            return isNegative + moreThanOneFractionFromScintific(arr[0], power);
        }
        return isNegative + lessThanOneFractionFromScintific(arr[0], power);
    }

    private static String lessThanOneFractionFromScintific(String value, int power) {
        return "0." + zerosString(-power - 1) + value;
    }

    private static String moreThanOneFractionFromScintific(String value, int power) {
        if (power + 1 >= value.length()) {
            return value + zerosString(power - value.length() + 1);
        }
        return value.substring(0, power + 1) + "." + value.substring(power + 1);
    }

    private static String zerosString(int length) {
        if (length <= 0) return "";
        return new String(new char[length]).replace('\0', '0');
    }

    private static String toScientific(String value, int fractionLength, CalculatorMode calculatorMode) {
        switch (calculatorMode) {
            case BASIC:
            case P_NUMBER:
                return realToScientific(value, fractionLength);
            case FRACTION:
                Fraction fraction = (Fraction) stringToFraction(value);
                value = realToScientific(fraction.numeratorToString(), fractionLength)
                        + "/"
                        + realToScientific(fraction.denominatorToString(), fractionLength);
                return value;
            default:
                return value;
        }
    }

    private static String realToScientific(String value, int fractionLength) {
        if (value.length() - (value.startsWith("-") ? 1 : 0) <= fractionLength) {
            return value;
        }
        String[] arr = value.split("\\.");
        if (arr.length > 2) {
            throw new IllegalArgumentException("Value must be in 0.0 format");
        }
        if (arr[0].equals("0") || arr[0].equals("-0")) {
            return lessThanOneFractionToScientific(value, fractionLength);
        }
        return moreThanOneFractionToScientific(value, fractionLength);
    }

    private static String lessThanOneFractionToScientific(String value, int fractionLength) {
        boolean isNegative = value.startsWith("-");
        value = value.split("\\.")[1];
        int leadingZeros = 1;
        if (value.startsWith("0")) {
            String tmp = value.replaceFirst("0+", "");
            leadingZeros += value.length() - tmp.length();
            value = tmp;
        }
        if (value.length() > 1) {
            value = value.substring(0, 1) + "." + value.substring(1, Math.min(value.length(), fractionLength + 1));
        } else if (value.isEmpty()) {
            return "0";
        }
        value += "exp-" + leadingZeros;
        if (isNegative) {
            value = "-" + value;
        }
        return value;
    }

    private static String moreThanOneFractionToScientific(String value, int fractionLength) {
        int intSize = value.indexOf('.');
        if (intSize == -1) {
            intSize = value.length();
        }
        int dotPos = 1;
        if (value.startsWith("-")) {
            dotPos = 2;
            fractionLength++;
            intSize--;
        }
        value = value.replaceAll("\\.", "").substring(0, Math.min(fractionLength, value.length() - 1));
        int count = 0;
        for (int i = value.length() - 1; i >= 0; i--) {
            if (value.charAt(i) == '0') {
                count++;
            } else {
                break;
            }
        }
        value = value.substring(0, value.length() - count);
        if (intSize == 1) {
            if (value.length() == 1) {
                return value;
            }
            return value.substring(0, dotPos) + "." + value.substring(dotPos);
        }
        if (value.length() == 1) {
            return value + "exp" + (intSize - 1);
        }
        return value.substring(0, dotPos) + "." + value.substring(dotPos) + "exp" + (intSize - 1);
    }

    public static Number stringToNumber(String value, CalculatorMode mode, int base) {
        value = commasToDots(value);
        value = fromScientific(value, mode);
        switch (mode) {
            case BASIC:
            case P_NUMBER:
                value = value.toUpperCase();
                value = ConverterPToP.convertPTo10Adaptive(value, base);
                return stringToReal(value);
            case COMPLEX:
                return stringToComplex(value);
            case FRACTION:
                return stringToFraction(value);
            default:
                return stringToReal("0");
        }
    }

    private static Number stringToReal(String value) {
        return new Real(new BigDecimal(value));
    }

    //format : x + yi,
    private static Number stringToComplex(String value) {
        value = value.replaceAll("\\s+", "");
        boolean realIsNegative = false;
        boolean imIsNegative = false;
        if (value.startsWith("-")) {
            realIsNegative = true;
            value = value.replaceFirst("-", "");
        } else if (value.startsWith("+")) {
            value = value.replaceFirst("\\+", "");
        }

        if (value.contains("-")) {
            imIsNegative = true;
        }
        value = value.replaceFirst("[-+]", " ");
        String[] arr = value.split("\\s");
        if (arr.length > 2) {
            throw new IllegalArgumentException("Incorrect complex number");
        }
        BigDecimal real = BigDecimal.ZERO;
        BigDecimal im = BigDecimal.ZERO;
        if (arr[0].endsWith("i")) {
            im = new BigDecimal(arr[0].replaceFirst("i", ""));
        } else {
            real = new BigDecimal(arr[0]);
        }
        if (arr.length > 1) {
            im = new BigDecimal(arr[1].replaceFirst("i", ""));
        }

        if (realIsNegative) {
            real = real.negate();
        }
        if (imIsNegative) {
            im = im.negate();
        }
        return new Complex(real, im);
    }

    //format : a/b
    private static Number stringToFraction(String value) {
        Pair<String, String> fraction = stringToNumeratorAndDenominator(value);
        BigInteger numerator = new BigInteger(fraction.getKey());
        BigInteger denominator = new BigInteger(fraction.getValue());
        return new Fraction(numerator, denominator);
    }

    private static Pair<String, String> stringToNumeratorAndDenominator(String value) {
        value = value.replaceAll("\\s+", "");
        String[] values = value.split("/");
        if (values.length != 2) {
            throw new IllegalArgumentException("Incorrect fraction number");
        }
        return new Pair<>(values[0], values[1]);
    }

    public static String cutTrailingZeros(String value) {
        if (!value.contains(".")) {
            return value;
        }
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '0') {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (sb.charAt(sb.length() - 1) == '.') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static CalculatorMode fromNumberToCalculatorMode(Number number) {
        if (number instanceof Real) {
            return CalculatorMode.P_NUMBER;
        }
        if (number instanceof Complex) {
            return CalculatorMode.COMPLEX;
        }
        if (number instanceof Fraction) {
            return CalculatorMode.FRACTION;
        }
        return null;
    }

    public static String dotsToCommas(String s) {
        return s.replaceAll("\\.", ",");
    }

    public static String commasToDots(String s) {
        return s.replaceAll(",", ".");
    }

    public static String toScientificIfNeeded(String data, CalculatorMode calculatorMode, int maxLen, int maxLenFract) {
        if (calculatorMode.equals(CalculatorMode.COMPLEX)) {
            Complex complex = (Complex) stringToNumber(data, calculatorMode, 10);
            if (complex.getReal().toPlainString().length() > OVERFLOW_LENGTH_COMPLEX
                    || complex.getImaginary().toPlainString().length() > OVERFLOW_LENGTH_COMPLEX) {
                throw new OverflowException("Overflow");
            }
        }
        if (data.length() > OVERFLOW_LENGTH) {
            throw new OverflowException("Overflow");
        }
        if (data.length() > maxLen) {
            if (calculatorMode.equals(CalculatorMode.BASIC) || calculatorMode.equals(CalculatorMode.P_NUMBER)) {
                return NumberConverter.toScientific(data, maxLen, calculatorMode);
            } else if (calculatorMode.equals(CalculatorMode.FRACTION)) {
                return NumberConverter.toScientific(data, maxLenFract, calculatorMode);
            }
        }
        return data;
    }

}
