package com.examples.Laba;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
//import akka.http.javadsl.model.HttpRequest;
//import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import org.asynchttpclient.AsyncHttpClient;
import akka.http.javadsl.model.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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
                .mapAsync(10,this::TestExecution)
    }

    public Url parseRequest(HttpRequest req){
        Query query = req.getUri().query();
        Optional<String> testUrl = query.get("testUrl");
        Optional<String> count = query.get("count");
        return new Url(testUrl.get(), Integer.parseInt(count.get()));
    }

    public CompletionStage<TestResult> TestExecution(Url test){
        return Patterns.ask(storage, test, Duration.ofSeconds(5))
                .thenApply(o -> (ReturnMessage) o)
                .thenCompose(result -> {
                    Optional<TestResult> res = result.get();
                    if(res.isPresent()){
                        CompletableFuture.completedFuture(res.get());
                    } else{
                        RunTest(test);
                    }
                });
    }

    private CompletionStage<TestResult> RunTest(Url test) {
    }
}
