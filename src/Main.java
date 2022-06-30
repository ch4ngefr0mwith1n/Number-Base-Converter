package converter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ");
            String[] parameters = scanner.nextLine().split("\\s+");
            if ("/exit".equalsIgnoreCase(parameters[0])) {
                return;
            }
            // pretvaranje iz niza Stringova u niz Integera:
            int[] bases = Arrays.stream(parameters).mapToInt(Integer::parseInt).toArray();

            while (true) {
                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back) ",
                        bases[0], bases[1]);

                String number = scanner.nextLine();
                if ("/back".equalsIgnoreCase(number)) {
                    break;
                }
                System.out.println("Conversion result: " + fromToRadix(number, bases[0], bases[1]));
            }
        }
    }

    public static String fromToRadix(String number, int sourceBase, int targetBase) {
        // dijelimo String na cjelobrojni i decimalni dio, preko tačke:
        int dotIndex = number.indexOf('.');

        // ukoliko nema decimalnog dijela, automatski odraditi konverziju preko BigInteger:
        if (dotIndex == -1) {
            return new BigInteger(number, sourceBase).toString(targetBase);
        }

        // String koji sadrži cjelobrojni dio:
        String integerPart = number.substring(0, dotIndex);
        // String koji sadrži decimalni dio:
        String fractionPart = number.substring(dotIndex + 1);

        // konverzija cjelobrojnog dijela:
        String convertedIntegerPart = new BigInteger(integerPart, sourceBase).toString(targetBase);

        // početna vrijednost kad prebacijemo decimale u decimalni brojni sistem (10):
        double decimalFraction = 0.0;
        // dijeljenje vršimo sa vrijednošću izvorne baze:
        double divider = (double) sourceBase;

        // prebacivanje decimalnog dijela iz izvornog brojnog sistema u decimalni brojni sistem (10):
        for (char digit : fractionPart.toCharArray()) {
            // kod svih brojnih sistema sa bazom većom od 10, koriste se slova "abcd..."
            // metoda "indexOf" će da vadi cjelobrojnu vrijednost iz Stringa "DIGITS" za zadata slova
            // recimo ("F" -> "15")
            decimalFraction += (DIGITS.indexOf(digit) / divider);
            // nakon dodavanja, stepenujemo izvornu bazu i nakon toga se pomjeramo udesno:
            divider *= sourceBase;
        }

        StringBuilder convertedFraction = new StringBuilder();

        // prebacivanje decimalnog dijela iz decimalnog brojnog sistema (10) u zadati brojni sistem (2-36)
        // takođe, zaokružujemo na 5 cifara:
        for (int i = 0; i < 5; i++) {
            // množimo početnu vrijednost sa bazom zadatog sistema:
            decimalFraction *= targetBase;

            // dobićemo određenu vrijednost, zavisno od rezultata
            // preko indeksa vadimo njen ekvivalent:
            int index = (int) decimalFraction;
            convertedFraction.append(DIGITS.charAt(index));

            // uklanjanje cjelobrojne vrijednosti i onda se opet vrši množenje :
            decimalFraction -= index;
        }

        return convertedIntegerPart + "." + convertedFraction;
    }
}


