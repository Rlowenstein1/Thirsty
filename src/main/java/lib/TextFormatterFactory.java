/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import javafx.scene.control.TextFormatter;

/**
 *
 * @author tybrown
 */
public class TextFormatterFactory {

    /**
     * Returns a TextFormatter which only permits numerical input
     * @param integerOnly True if only integers, false if decimals are acceptable
     * @return the TextFormatter
     */
    public static TextFormatter numericOnlyTextFormatter(boolean integerOnly) {
        DecimalFormat format = new DecimalFormat("#.0");
        format.setParseIntegerOnly(integerOnly);
        return (new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if ((object == null) || (parsePosition.getIndex() < c.getControlNewText().length())) {
                return null;
            } else {
                return c;
            }
        }));
    }

    /**
     * Shortcut for numericOnlyTextFormatter(false)
     * @return The TextFormatter
     */
    public static TextFormatter decimalOnlyTextFormatter() {
        return (numericOnlyTextFormatter(false));
    }

    /**
     * Shortcut for numericOnlyTextFormatter(true)
     * @return The TextFormatter
     */
    public static TextFormatter integerOnlyTextFormatter() {
        return (numericOnlyTextFormatter(true));
    }
 
}
