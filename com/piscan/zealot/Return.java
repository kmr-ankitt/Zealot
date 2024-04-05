package com.piscan.zealot;

class Return extends RuntimeException {
    final Object value;

    Return(Object value){

        // disables some JVM machinery that we don’t need.
        super(null, null, false, false);
        this.value = value;
    }
}
