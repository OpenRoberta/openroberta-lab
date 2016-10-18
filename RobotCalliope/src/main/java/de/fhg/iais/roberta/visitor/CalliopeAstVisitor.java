package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.calliope.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.calliope.DisplayTextAction;
import de.fhg.iais.roberta.syntax.expr.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.ImageShiftFunction;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface CalliopeAstVisitor<V> extends AstVisitor<V> {
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
}
