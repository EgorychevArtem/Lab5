import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.asynchttpclient.AsyncHttpClient;

import java.util.concurrent.CompletionStage;

public class App {
    ActorSystem system = ActorSystem.create("routes");
    final Http http = Http.get(system);
    final ActorMaterializer materializer = ActorMaterializer.create(system);

    AsyncHttpClient httpClient = asyncHtt

    Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute(system, router).flow(system, materializer);
    CompletionStage<ServerBinding> binding = http.bindAndHandle(
            routeFlow,
            ConnectHttp.toHost(HOST, PORT),
            materializer
    );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding
                .thenCompose(ServerBinding::unbind)
            .thenAccept(unbound -> system.terminate());

}
