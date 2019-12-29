package de.fhg.iais.roberta.util;

/**
 * a pair of objects of arbitrary types. Use the factory method {@link #of(Object, Object)} to create pairs. Pairs are <b>immutable</b>.
 *
 * @author rbudde
 * @param <X>
 * @param <Y>
 */
public class Pair<X, Y> {
    private final X first;
    private final Y second;

    private Pair(final X first, final Y second) {
        this.first = first;
        this.second = second;
    }

    /**
     * make a pair
     *
     * @param first
     * @param second
     * @return the pair
     */
    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<T, U>(first, second);
    }

    /**
     * get the first item of a pair
     *
     * @return the first item
     */
    public X getFirst() {
        return this.first;
    }

    /**
     * get the second item of a pair
     *
     * @return the second item
     */
    public Y getSecond() {
        return this.second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.first == null) ? 0 : this.first.hashCode());
        result = prime * result + ((this.second == null) ? 0 : this.second.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        if ( this.first == null ) {
            if ( other.first != null ) {
                return false;
            }
        } else if ( !this.first.equals(other.first) ) {
            return false;
        }
        if ( this.second == null ) {
            if ( other.second != null ) {
                return false;
            }
        } else if ( !this.second.equals(other.second) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ")";
    }

    public String toStringContent() {
        return this.first + ", " + this.second;
    }
}