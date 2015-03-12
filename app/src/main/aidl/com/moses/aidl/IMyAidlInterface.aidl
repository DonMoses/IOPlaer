// IMyAidlInterface.aidl
package com.moses.aidl;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void play(String path);
    void pause();
}
