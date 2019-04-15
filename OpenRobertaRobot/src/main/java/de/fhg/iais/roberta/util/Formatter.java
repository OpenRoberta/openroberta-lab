package de.fhg.iais.roberta.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Formatter {
    private static final Locale LOCALE_GB = new Locale("en", "UK");

    /**
     * format a double using the UK locale (decimal POINT!) and show 1 digit after the decimal point. This is used for centimeters e.g.: 198.8 makes sense,
     * finer resolution beyond millimeters is not needed
     *
     * @param toFormat to be formatted
     * @return the formatted value
     */
    public static String d2s(double toFormat) {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(LOCALE_GB);
        decimalFormat.applyPattern("0.0");
        String formatted = decimalFormat.format(toFormat);
        return formatted;
    }

    /**
     * parse a string to a double using the UK locale (decimal POINT!) and expect not more than 1 digit after the decimal point. This is used for centimeters
     * e.g.: 198.8 makes sense, finer resolution beyond millimeters is not needed
     *
     * @param toFormat String to be parsed
     * @return the double
     * @throws ParseException
     */
    public static double s2d(String toFormat) throws ParseException {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(LOCALE_GB);
        double anyNumberOfDigitsAfterPoint = decimalFormat.parse(toFormat).doubleValue();
        double oneDigitsAfterPoint = Math.floor(10 * anyNumberOfDigitsAfterPoint + 0.5);
        return 0.1 * oneDigitsAfterPoint;
    }
}
