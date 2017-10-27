package de.fhg.iais.roberta.testutil.test;

public class BeanTesterFail2 {
    @SuppressWarnings("unused")
    // is unused to trigger an error (the field has to be used to guarantee immutability ...
    private boolean readOnly = false;
    private int i;
    private String s;
    private BeanTesterSuccess t;

    public BeanTesterFail2() {
        super();
    }

    public BeanTesterFail2(int i, String s, BeanTesterSuccess t) {
        super();
        this.i = i;
        this.s = s;
        this.t = t;
    }

    public final void setReadOnly() {
        this.readOnly = true;
    }

    public final int getI() {
        return this.i;
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
        BeanTesterFail2 other = (BeanTesterFail2) obj;
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
        return "BeanTesterFail2 [i=" + this.i + ", s=" + this.s + ", t=...]";
    }
}
