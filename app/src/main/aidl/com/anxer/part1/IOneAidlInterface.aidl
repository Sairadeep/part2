// IOneAidlInterface.aidl
package com.anxer.part1;

// Declare any non-default types here with import statements

interface IOneAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     String sendName();
     void checkBack(int response);
}