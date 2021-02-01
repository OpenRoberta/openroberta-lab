package testVisitor.combineVisitor;

import testVisitor.ast.Phrase;

interface ITraverser<V> {
    ITraverser<V> step(Phrase<V> p);

    V yield(ICollector<V> collector);
}