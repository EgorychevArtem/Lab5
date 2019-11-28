package com.examples.Laba;

import akka.actor.ActorRef;

import java.util.Optional;

public class ReturnMessage {
    TestResult result;

    public ReturnMessage(TestResult result){
        this.result = result;
    }

    public TestResult getResult(){
        return result;
    }

    public Optional<TestResult> get(){
        if(result.res != null){
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }
}
