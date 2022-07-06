package testVisitor.combineVisitor;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public interface IStructureRunner<V> {
    default V visitA(A a, ITraverser<V> traverser, ICollector<V> collector) {
        return traverser.step(a.b).step(a.c).yield(collector);
    }

    default V visitB(B b, ITraverser<V> traverser, ICollector<V> collector) {
        return traverser.step(b.c).yield(collector);
    }

    default V visitC(C c, ITraverser<V> traverser, ICollector<V> collector) {
        return traverser.yield(collector);
    }
}
