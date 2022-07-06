package testVisitor.mapVisitor;

import java.util.List;
import java.util.stream.Collectors;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public class AccVisitor implements AbstractVisitor<String> {
    @Override
    public String visitA(A a) {
        return AbstractVisitor.super.visitAImpl(a).stream().collect(Collectors.joining(",", "TypeA [", "]"));
    }

    @Override
    public String visitB(B b) {
        List<String> list = AbstractVisitor.super.visitBImpl(b);
        if ( list.size() == 1 && list.get(0).equals("TypeB []") ) {
            // ok
        } else {
            System.out.println("error! " + list);
        }
        return list.stream().collect(Collectors.joining(",", "TypeB [", "]"));
    }

    @Override
    public String visitC(C c) {
        return AbstractVisitor.super.visitCImpl(c).stream().collect(Collectors.joining(",", "TypeC [", "]"));
    }
}
