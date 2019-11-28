package com.examples.Laba;

public class ReturnMessage {
    TestResult result;

    ReturnMessage(TestResult result){
        this.result = result;
    }

    public TestResult getResult(){
        return result;
    }
}
