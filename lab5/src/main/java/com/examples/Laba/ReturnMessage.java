package com.examples.Laba;

import akka.actor.ActorRef;

import java.util.Optional;

public class ReturnMessage {
    TestResult result;

    ReturnMessage(TestResult result, ActorRef actorRef){
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
