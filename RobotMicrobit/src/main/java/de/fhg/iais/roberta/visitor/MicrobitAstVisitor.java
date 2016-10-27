package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.microbit.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.microbit.DisplayTextAction;
import de.fhg.iais.roberta.syntax.expr.Image;
import de.fhg.iais.roberta.syntax.expr.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensor.microbit.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.microbit.TemperatureSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface MicrobitAstVisitor<V> extends AstVisitor<V> {
    /**
     * visit a {@link DisplayTextAction}.
     *
     * @param displayTextAction phrase to be visited
     */
    public V visitDisplayTextAction(DisplayTextAction<V> displayTextAction);

    /**
     * visit a {@link PredefinedImage}.
     *
     * @param predefinedImage phrase to be visited
     */
    public V visitPredefinedImage(PredefinedImage<V> predefinedImage);

    /**
     * visit a {@link DisplayImageAction}.
     *
     * @param displayImageAction phrase to be visited
     */
    public V visitDisplayImageAction(DisplayImageAction<V> displayImageAction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    public V visitImageShiftFunction(ImageShiftFunction<V> imageShiftFunction);

    /**
     * visit a {@link ImageShiftFunction}.
     *
     * @param imageShiftFunction phrase to be visited
     */
    public V visitImageInvertFunction(ImageInvertFunction<V> imageInvertFunction);

    /**
     * visit a {@link Image}.
     *
     * @param image phrase to be visited
     */
    public V visitImage(Image<V> image);

    /**
     * visit a {@link GestureSensor}.
     *
     * @param gestureSensor phrase to be visited
     */
    public V visitGestureSensor(GestureSensor<V> gestureSensor);

    /**
     * visit a {@link TemperatureSensor}.
     *
     * @param temperatureSensor phrase to be visited
     */
    public V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);
}
