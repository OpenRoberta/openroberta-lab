package de.fhg.iais.roberta.syntax.functions.mbed;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "IMAGE_INVERT", category = "FUNCTION", blocklyNames = {"mbedImage_invert"})
public final class ImageInvertFunction<V> extends Function<V> {
    @NepoValue(name = "VAR", type = BlocklyType.PREDEFINED_IMAGE)
    public final Expr<V> image;

    public ImageInvertFunction(BlocklyProperties properties, Expr<V> image) {
        super(properties);
        Assert.notNull(image);

        this.image = image;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.VOID;
    }

}
