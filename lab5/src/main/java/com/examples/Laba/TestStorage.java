package com.examples.Laba;

import akka.actor.AbstractActor;

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

                    );
                })
                .build();
    }

}
