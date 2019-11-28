package com.examples.Laba;

import java.util.Optional;

public class TestResult {
    Url test;
    Long res;

    TestResult(Url test, Long res){
        this.res = res;
        this.test = test;
    }

    public  Url getTest(){
        return test;
    }

    public  Long getRes(){
        return res;
    }
}
