package testVisitor.combineVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import testVisitor.ITestVisitor;
import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;
import testVisitor.ast.Phrase;

public class AccVisitor implements ITestVisitor<String>, IStructureRunner<String> {
    @Override
    public String visitA(A a) {
        return IStructureRunner.super.visitA(a, new ListTraverser(), new ICollector<String>() {
            @Override
            public String run(List<String> collected) {
                return collected.stream().collect(Collectors.joining(",", "TypeA [", "]"));
            }
        });
    }

    @Override
    public String visitB(B b) {
        return IStructureRunner.super.visitB(b, new ListTraverser(), new ICollector<String>() {
            @Override
            public String run(List<String> collected) {
                return collected.stream().collect(Collectors.joining(",", "TypeB [", "]"));
            }
        });
    }

    @Override
    public String visitC(C c) {
        return IStructureRunner.super.visitC(c, new ListTraverser(), new ICollector<String>() {
            @Override
            public String run(List<String> collected) {
                return collected.stream().collect(Collectors.joining(",", "TypeC [", "]"));
            }
        });
    }

    class ListTraverser implements ITraverser<String> {
        List<String> collected = new ArrayList<>();

        @Override
        public ITraverser<String> step(Phrase p) {
            String subcollected = p.accept(AccVisitor.this);
            collected.add(subcollected);
            return this;
        }

        @Override
        public String yield(ICollector<String> collector) {
            return collector.run(collected);
        }
    }
}
