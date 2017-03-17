package de.fhg.iais.roberta.searchMsg;

public class MsgHits {
    final private String fileName;
    final private int lineNumber;
    final private String content;

    public MsgHits(String fileName, int lineNumber, String content) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.content = content;

    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public String toString() {
        return "MsgHits [fileName=" + this.fileName + ", lineNumber=" + this.lineNumber + ", content=" + this.content + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
        result = prime * result + ((this.fileName == null) ? 0 : this.fileName.hashCode());
        result = prime * result + this.lineNumber;
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
        MsgHits other = (MsgHits) obj;
        if ( this.content == null ) {
            if ( other.content != null ) {
                return false;
            }
        } else if ( !this.content.equals(other.content) ) {
            return false;
        }
        if ( this.fileName == null ) {
            if ( other.fileName != null ) {
                return false;
            }
        } else if ( !this.fileName.equals(other.fileName) ) {
            return false;
        }
        if ( this.lineNumber != other.lineNumber ) {
            return false;
        }
        return true;
    }
}