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
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import org.asynchttpclient.AsyncHttpClient;
import akka.http.javadsl.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Tester {
    private static int PARALLELISM = 10;
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
                .mapAsync(PARALLELISM,this::TestExecution)
                .map(this::CreateResponse);
    }

    private HttpResponse CreateResponse(TestResult result) throws JsonProcessingException {
        storage.tell(result, ActorRef.noSender());
        return HttpResponse.create()
                .withStatus(StatusCodes.OK)
                .withEntity(ContentTypes.APPLICATION_JSON, ByteString.fromString(
                        new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(result)
                ));
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
                        return CompletableFuture.completedFuture(res.get());
                    } else{
                        return RunTest(test);
                    }
                });
    }

    public CompletionStage<TestResult> RunTest(Url test) {
        final Sink<Url, CompletionStage<Long>> testSink = Flow.of(Url.class)
                .mapConcat(m -> Collections.nCopies(m.count, m.url))
                .mapAsync(PARALLELISM, m->{
                    Instant StartRequestTime = Instant.now();
                    return asyncHttpClient.prepareGet(m).execute()
                            .toCompletableFuture()
                            .thenCompose(r -> CompletableFuture.completedFuture(
                                    Duration.between(StartRequestTime, Instant.now()).getSeconds()
                            ));
                })
                .toMat(Sink.fold(0L,Long::sum), Keep.right());

        return Source.from(Collections.singleton(test))
                .toMat(testSink, Keep.right())
                .run(materializer)
                .thenApply(s -> new TestResult(test, s/test.count));
    }
}
