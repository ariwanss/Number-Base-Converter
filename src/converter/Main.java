package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Map<Integer, String> decToHex = new HashMap<>();
    private static Map<String, Integer> hexToDec = new HashMap<>();

    public static void main(String[] args) {

        for (int i = 0; i < 26; i++) {
            String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            decToHex.put(i + 10, String.valueOf(letters.charAt(i)));
            hexToDec.put(String.valueOf(letters.charAt(i)), i + 10);
        }

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String response = scanner.nextLine();
            if ("/exit".equals(response)) {
                return;
            }
            String[] sourceAndTargetBase = response.split("\\s");
            int sourceBase = Integer.parseInt(sourceAndTargetBase[0]);
            int targetBase = Integer.parseInt(sourceAndTargetBase[1]);
            convertAnything(sourceBase, targetBase);
            System.out.println();
        }
    }

    public static void convert(int sourceBase, int targetBase) {

        while (true) {
            System.out.print("Enter number in base " + sourceBase + " to convert to base " + targetBase +
                    " (To go back type /back) ");
            String sourceNumber = scanner.nextLine();
            if ("/back".equals(sourceNumber)) {
                return;
            }

            String sourceNumberReversed = new StringBuilder(sourceNumber.toUpperCase()).reverse().toString();
            BigInteger sum = BigInteger.ZERO;
            for (int i = 0; i < sourceNumber.length(); i++) {
                int digit;
                try {
                    digit = Integer.parseInt(String.valueOf(sourceNumberReversed.charAt(i)));
                } catch (NumberFormatException e) {
                    digit = hexToDec.get(String.valueOf(sourceNumberReversed.charAt(i)));
                }

                sum = sum.add(BigInteger.valueOf(digit * ((long) Math.pow(sourceBase, i))));
            }

            StringBuilder targetNumberReversed = new StringBuilder();
            while (sum.compareTo(BigInteger.ZERO) > 0) {
                int remainder = sum.remainder(BigInteger.valueOf(targetBase)).intValue();
                //System.out.println(remainder);
                if (remainder > 9) {
                    targetNumberReversed.append(decToHex.get(remainder));
                } else {
                    targetNumberReversed.append(remainder);
                }
                sum = sum.divide(BigInteger.valueOf(targetBase));
            }
            System.out.println("Conversion result: " + targetNumberReversed.reverse());
            System.out.println();
        }
    }

    private static void convertAnything(int sourceBase, int targetBase) {
        while (true) {
            System.out.print("Enter number in base " + sourceBase + " to convert to base " + targetBase +
                    " (To go back type /back) ");
            String sourceNumber = scanner.nextLine();
            if ("/back".equals(sourceNumber)) {
                return;
            }

            String[] number = sourceNumber.split("\\.");
            if (number.length > 1) {
                System.out.println("Conversion result: " + convertWithFraction(sourceBase, targetBase, number));
            } else {
                System.out.println("Conversion result: " + convertWithoutFraction(sourceBase, targetBase, number[0]));
            }
            System.out.println();
        }

    }

    private static String convertWithoutFraction(int sourceBase, int targetBase, String number) {
        return convertIntegerPart(sourceBase, targetBase, number);
    }

    private static String convertWithFraction(int sourceBase, int targetBase, String[] number) {
        return convertIntegerPart(sourceBase, targetBase, number[0]) + "." +
                convertFractionPart(sourceBase, targetBase, number[1]);
    }

    private static String convertIntegerPart(int sourceBase, int targetBase, String number) {
        return convertIntegerFromDecimal(targetBase, convertIntegerToDecimal(sourceBase, number));
    }

    private static String convertFractionPart(int sourceBase, int targetBase, String number) {
        return convertFractionFromDecimal(targetBase, convertFractionToDecimal(sourceBase, number));
    }

    private static String convertIntegerToDecimal(int base, String number) {
        String numberReversed = new StringBuilder(number.toUpperCase()).reverse().toString();
        BigInteger sum = BigInteger.ZERO;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(numberReversed.charAt(i));
            sum = sum.add(BigInteger.valueOf(digit * ((long) Math.pow(base, i))));
        }
        return sum.toString();
    }

    private static String convertIntegerFromDecimal(int base, String number) {
        if ("0".equals(number)) {
            return "0";
        }
        StringBuilder numberReversed = new StringBuilder();
        BigInteger bigIntegerNumber = new BigInteger(number);
        while (bigIntegerNumber.compareTo(BigInteger.ZERO) > 0) {
            int remainder = bigIntegerNumber.remainder(BigInteger.valueOf(base)).intValue();
            numberReversed.append(Character.forDigit(remainder, base));
            bigIntegerNumber = bigIntegerNumber.divide(BigInteger.valueOf(base));
        }
        return numberReversed.reverse().toString().toUpperCase();
    }

    private static String convertFractionToDecimal(int base, String number) {
        //System.out.println(number);
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum = sum.add(BigDecimal.valueOf(digit / Math.pow(base, (i + 1))));
        }
        return sum.toString();
    }

    private static String convertFractionFromDecimal(int base, String number) {
        //StringBuilder numberBuilder = new StringBuilder("0.");
        String numberBuilder = "";
        //BigDecimal toReturn = BigDecimal.ZERO;
        BigDecimal bigDecimalNumber = new BigDecimal(number);
        //System.out.println(bigDecimalNumber);
        while (numberBuilder.length() < 5) {
            int remainder = bigDecimalNumber.multiply(BigDecimal.valueOf(base)).intValue();
            //System.out.println(remainder + " " + Character.forDigit(remainder, base));
            //numberBuilder.append(Character.forDigit(remainder, base));
            numberBuilder += Character.forDigit(remainder, base);
            bigDecimalNumber = bigDecimalNumber.multiply(BigDecimal.valueOf(base)).remainder(BigDecimal.ONE);
            //System.out.println(bigDecimalNumber);
        }
        return numberBuilder.toUpperCase();
    }

    public static void convertFromDecimal() {
        System.out.print("Enter number in decimal system: ");
        int number = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter target base: ");
        int base = Integer.parseInt(scanner.nextLine());

        StringBuilder numberBuilder = new StringBuilder();
        while (number > 0) {
            int remainder = number % base;
            if (remainder > 9) {
                numberBuilder.append(decToHex.get(remainder));
            } else {
                numberBuilder.append(number % base);
            }
            number /= base;
        }
        System.out.println("Conversion result: " + numberBuilder.reverse());
    }

    public static void convertToDecimal() {
        System.out.print("Enter source number: ");
        String number = scanner.nextLine().toUpperCase();
        System.out.print("Enter source base: ");
        int base = Integer.parseInt(scanner.nextLine());

        String reversedNumber = new StringBuilder(number).reverse().toString();
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit;
            try {
                digit = Integer.parseInt(String.valueOf(reversedNumber.charAt(i)));
            } catch (NumberFormatException e) {
                digit = hexToDec.get(String.valueOf(reversedNumber.charAt(i)));
            }

            sum += digit * ((int) Math.pow(base, i));
        }
        System.out.println("Conversion to decimal result: " + sum);
    }
}
