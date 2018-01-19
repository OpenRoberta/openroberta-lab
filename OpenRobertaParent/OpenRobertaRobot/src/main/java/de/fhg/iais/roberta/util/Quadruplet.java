package de.fhg.iais.roberta.util;

/**
 * a pair of objects of arbitrary types. Use the factory method {@link #of(Object, Object)} to create pairs. Pairs are <b>immuntable</b>.
 *
 * @author eovchinnik
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
public class Quadruplet<T1, T2, T3, T4> {
    private final T1 first;
    private final T2 second;
    private final T3 third;
    private final T4 fourth;

    private Quadruplet(final T1 first, final T2 second, final T3 third, final T4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    /**
     * make a quadruplet
     *
     * @param first
     * @param second
     * @param third
     * @param fourth
     * @return the quadruplet
     */
    public static <T1, T2, T3, T4> Quadruplet<T1, T2, T3, T4> of(T1 first, T2 second, T3 trird, T4 fourth) {
        return new Quadruplet<>(first, second, trird, fourth);
    }

    /**
     * get the first item of a quadruplet
     *
     * @return the first item
     */
    public T1 getFirst() {
        return this.first;
    }

    /**
     * get the second item of a quadruplet
     *
     * @return the second item
     */
    public T2 getSecond() {
        return this.second;
    }

    /**
     * get the third item of a quadruplet
     *
     * @return the third item
     */
    public T3 getThird() {
        return this.third;
    }

    /**
     * get the fourth item of a quadruplet
     *
     * @return the fourth item
     */
    public T4 getFourth() {
        return this.fourth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.first == null) ? 0 : this.first.hashCode());
        result = (prime * result) + ((this.second == null) ? 0 : this.second.hashCode());
        result = (prime * result) + ((this.third == null) ? 0 : this.third.hashCode());
        result = (prime * result) + ((this.fourth == null) ? 0 : this.third.hashCode());
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
        Quadruplet<?, ?, ?, ?> other = (Quadruplet<?, ?, ?, ?>) obj;
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
        if ( this.third == null ) {
            if ( other.third != null ) {
                return false;
            }
        } else if ( !this.second.equals(other.third) ) {
            return false;
        }
        if ( this.fourth == null ) {
            if ( other.fourth != null ) {
                return false;
            }
        } else if ( !this.second.equals(other.fourth) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.first + ", " + this.second + ", " + this.third + ")";
    }
}