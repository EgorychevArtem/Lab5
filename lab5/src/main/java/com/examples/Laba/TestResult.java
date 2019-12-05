package com.examples.Laba;

import java.util.Optional;

public class TestResult {
    Url test;
    Long avgResult;

    TestResult(Url test, Long avgResult){
        this.avgResult = avgResult;
        this.test = test;
    }

    public  Url getTest(){
        return test;
    }

    public  Long getAvgResult(){
        return avgResult;
    }
}
