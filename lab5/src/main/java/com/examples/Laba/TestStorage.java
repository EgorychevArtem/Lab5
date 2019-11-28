package com.examples.Laba;

import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestStorage extends AbstractActor {
    Map<Url, Long> storage = new HashMap<Url, Long>();

    @Override
    public Receive createReceive() {
        
    }

}
