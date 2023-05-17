package org.example.webservice;

import lombok.ToString;
import oap.util.Lists;
import oap.ws.WsMethod;
import oap.ws.WsParam;
import oap.ws.validate.WsValidateJson;
import org.example.NotNull;
import org.example.linking.MessageProducer;

import java.util.Optional;

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

    @WsMethod( method = POST, path = "/addData", produces = "text/plain" )
    public String addData(@WsParam( from = BODY )
                          @WsValidateJson( schema = "/schemas/data.conf" )
                          Data data,
                          Optional<String> bucket ) {
        return "Got data: " + data + " and bucket: " + bucket;
    }

    @WsMethod(method = GET, path = "/getData/{id}")
    public Data generateData( @WsParam( from = PATH )
                              String id ) {
        return new Data( id, Lists.of( "Obi-Wan", "Kenobi"  ), Data.StatusType.PASS_THROUGH );
    }
}