package de.fhg.iais.roberta.testutil.test;

public class BeanTesterFail1 {
    private int i;
    private String s;
    private BeanTesterSuccess t;

    public BeanTesterFail1() {
        super();
    }

    public BeanTesterFail1(int i, String s, BeanTesterSuccess t) {
        super();
        this.i = i;
        this.s = s;
        this.t = t;
    }

    public final int getI() {
        return this.i + 1;
    }

    public final void setI(int i) {
        this.i = i;
    }

    public final String getS() {
        return this.s;
    }

    public final void setS(String s) {
        this.s = s;
    }

    public final BeanTesterSuccess getT() {
        return this.t;
    }

    public final void setT(BeanTesterSuccess t) {
        this.t = t;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.i;
        result = prime * result + ((this.s == null) ? 0 : this.s.hashCode());
        result = prime * result + ((this.t == null) ? 0 : this.t.hashCode());
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
        BeanTesterFail1 other = (BeanTesterFail1) obj;
        if ( this.i != other.i ) {
            return false;
        }
        if ( this.s == null ) {
            if ( other.s != null ) {
                return false;
            }
        } else if ( !this.s.equals(other.s) ) {
            return false;
        }
        if ( this.t == null ) {
            if ( other.t != null ) {
                return false;
            }
        } else if ( !this.t.equals(other.t) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BeanTesterFail1 [i=" + this.i + ", s=" + this.s + ", t=...]";
    }
}
