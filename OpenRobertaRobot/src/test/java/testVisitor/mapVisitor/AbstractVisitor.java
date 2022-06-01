package testVisitor.mapVisitor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import testVisitor.ITestVisitor;
import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public interface AbstractVisitor<V> extends ITestVisitor<V> {
    default List<V> visitAImpl(A<V> a) {
        return Arrays.asList(a.b.accept(this), a.c.accept(this));
    }

    default List<V> visitBImpl(B<V> b) {
        return Arrays.asList(b.c.accept(this));
    }

    default List<V> visitCImpl(C<V> c) {
        return Collections.emptyList();
    }
}
