package testVisitor.combineVisitor;

import testVisitor.ast.Phrase;

interface ITraverser<V> {
    ITraverser<V> step(Phrase p);

    V yield(ICollector<V> collector);
}