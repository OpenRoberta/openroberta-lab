package testVisitor.combineVisitor;

import java.util.List;

public interface ICollector<V> {
    V run(List<V> collected);
}
