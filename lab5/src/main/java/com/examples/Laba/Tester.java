package com.examples.Laba;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
//import akka.http.javadsl.model.HttpRequest;
//import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;
import akka.http.javadsl.model.*;
import java.util.Optional;

public class Tester {
    AsyncHttpClient asyncHttpClient;
    ActorMaterializer materializer;
    ActorRef storage;

    public Tester(AsyncHttpClient asyncHttpClient, ActorSystem system, ActorMaterializer materializer){
        this.asyncHttpClient = asyncHttpClient;
        this.materializer = materializer;
        this.storage = system.actorOf(Props.create(TestStorage.class));
    }

    public Flow<HttpRequest, HttpResponse, NotUsed> createRoute(){
        return Flow.of(HttpRequest.class)
                .map(this::parseRequest)
    }

    public Url parseRequest(HttpRequest req){
        Query query = req.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new Url(testUrl.get(), Integer.parseInt(count.get()));
    }
}
