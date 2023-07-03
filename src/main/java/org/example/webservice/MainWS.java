package org.example.webservice;

import lombok.ToString;
import oap.http.Cookie;
import oap.http.Http;
import oap.util.Dates;
import oap.util.Lists;
import oap.ws.Response;
import oap.ws.WsMethod;
import oap.ws.WsParam;
import oap.ws.validate.WsValidateJson;
import org.example.NotNull;
import org.example.linking.MessageProducer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Consumer;

import static oap.http.server.nio.HttpServerExchange.HttpMethod.GET;
import static oap.http.server.nio.HttpServerExchange.HttpMethod.POST;
import static oap.ws.WsParam.From.BODY;
import static oap.ws.WsParam.From.PATH;

@ToString
public class MainWS {
    private final NotNull simple;
    private final NotNull full;
    private MessageProducer producer;

    public MainWS(NotNull simple, NotNull full, MessageProducer producer){
        this.simple = simple;
        this.full = full;
        this.producer = producer;
    }

    @WsMethod( path = "/ok"  )
    public String getMessage() {
        return "Hello world!" + simple.hashCode() + "|" + full.hashCode();
    }

    @WsMethod( path = "/substitute" )
    public String getRealValue() {
        return "real value is: " + producer.toBeResolved.value + ", should be 2";
    }

    @WsMethod( path = "/raw1", raw = true, description = "this method returns a file with name 'raw1.tsw'" )
    public Response getRawResponse() {
        Consumer<OutputStream> body = outputStream -> {
            try {
                outputStream.write("This\tis\ta\traw\tresponse\tdemo".getBytes());
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        return Response.ok()
                .withStatusCode(Http.StatusCode.ACCEPTED)
                .withBody(body, false)
                .withContentType(Http.ContentType.TEXT_TSV)
                .withCookie(new Cookie( "X_RAY", "palm" ))
                .withHeader("x-file-date", Dates.nowUtcDate().toString());
    }

    @WsMethod( method = POST, path = "/addData", produces = "text/plain" )
    public Response addData(@WsParam( from = BODY )
                          @WsValidateJson( schema = "/schemas/data.conf" )
                          Data data,
                          Optional<String> bucket ) {
        return Response.jsonOk().withBody( data, false );
    }

    @WsMethod(method = GET, path = "/getData/{id}")
    public Data generateData( @WsParam( from = PATH )
                              String id ) {
        return new Data( id, Lists.of( "Obi-Wan", "Kenobi"  ), Data.StatusType.PASS_THROUGH );
    }
}