package prr.utilities;

public interface Visitable {
    <V> V accept(Visitor<V> visitor);
}
