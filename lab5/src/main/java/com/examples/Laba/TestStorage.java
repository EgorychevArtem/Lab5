package com.examples.Laba;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestStorage extends AbstractActor {
    Map<Url, Long> storage = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Url.class, m-> {
                    getSender().tell(
                            TestResult test = new TestResult(m, storage.get(m))
                            new ReturnMessage(test);
                    ActorRef.noSender();
                    );
                })
                .build();
    }

}
