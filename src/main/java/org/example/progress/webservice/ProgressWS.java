package org.example.progress.webservice;

import oap.ws.WsMethod;
import oap.ws.WsParam;
import org.example.progress.ProgressHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ProgressWS {
    private static final ProgressHolder DEFAULT = new ProgressHolder();

    private Map<String, ProgressHolder> allProgresses = new ConcurrentHashMap<>();

    public ProgressWS() {
        createOrUpdateProgress("123", Optional.of( 0.0), Optional.of( 90.0), Optional.of( "Creating..." ));
        new Thread(() -> {
            boolean plusSign = true;
            long startTime = System.currentTimeMillis();
            while (true) {
                try {
                    Thread.currentThread().sleep(100);
                    ProgressHolder progressHolder = get("123").get();
                    progressHolder.tick( plusSign ? 1.0 : -1.0 );
                    if (progressHolder.isFrozen()) {
                        plusSign = !plusSign;
                    }
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                    if (elapsed < 15) {
                        progressHolder.setFrozen(true);
                    } else {
                        progressHolder.setFrozen(false);
                    }
                    progressHolder.setMessage("working hard "+ elapsed +" sec...");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @WsMethod( path = "/createProgress/{id}"  )
    public void createOrUpdateProgress(@WsParam( from = WsParam.From.PATH ) String id,
                               Optional<Double> start,
                               Optional<Double> end,
                               Optional<String> message ) {
        allProgresses.compute( id, ( k, ph ) -> {
            if ( ph == null ) {
                ph = new ProgressHolder();
                ph.setId( id );
            }
            if ( message.isPresent() ) ph.setMessage( message.get() );
            if ( end.isPresent() ) ph.setEnd( end.get() );
            if ( start.isPresent() ) ph.setStart( start.get() );
            return ph;
        } );
    }

    @WsMethod( path = "/get/{id}" )
    public Optional<ProgressHolder> get( @WsParam( from = WsParam.From.PATH ) String id ) {
        ProgressHolder progressHolder = allProgresses.get(id);
        return Optional.ofNullable( progressHolder );
    }

    @WsMethod( path = "/deleteProgress/{id}"  )
    public void deleteProgress(@WsParam( from = WsParam.From.PATH ) String id ) {
        allProgresses.remove( id );
    }

    @WsMethod( path = "/setMessage/{id}"  )
    public String getMessage( @WsParam(from = WsParam.From.PATH ) String id, String message ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return "";
        progressHolder.get().setMessage( message );
        return get( id ).orElse( DEFAULT ).getMessage();
    }

    @WsMethod( path = "/getMessage/{id}"  )
    public String getMessage( @WsParam(from = WsParam.From.PATH ) String id ) {
        return get( id ).orElse( DEFAULT ).getMessage();
    }

    @WsMethod( path = "/setStart/{id}/{start}"  )
    public Optional<Double> setStart( @WsParam(from = WsParam.From.PATH ) String id,
                                      @WsParam(from = WsParam.From.PATH ) Double start ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        ProgressHolder holder = progressHolder.get();
        holder.setStart( start );
        return Optional.of( holder.getStart() );
    }

    @WsMethod( path = "/getStart/{id}"  )
    public Optional<Double> getStart( @WsParam(from = WsParam.From.PATH ) String id ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        return Optional.of( progressHolder.get().getStart() );
    }

    @WsMethod( path = "/setEnd/{id}/{end}"  )
    public Optional<Double> setEnd( @WsParam(from = WsParam.From.PATH ) String id,
                                    @WsParam(from = WsParam.From.PATH ) Double end ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        ProgressHolder holder = progressHolder.get();
        holder.setEnd( end );
        return Optional.of( holder.getEnd() );
    }

    @WsMethod( path = "/getEnd/{id}"  )
    public Optional<Double> getEnd( @WsParam(from = WsParam.From.PATH ) String id ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        return Optional.of( progressHolder.get().getEnd() );
    }

    @WsMethod( path = "/setCurrent/{id}/{current}"  )
    public Optional<Double> setCurrent( @WsParam(from = WsParam.From.PATH ) String id,
                                        @WsParam(from = WsParam.From.PATH ) Double current ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        ProgressHolder holder = progressHolder.get();
        holder.setCurrent( current );
        return Optional.of( holder.getCurrent() );
    }

    @WsMethod( path = "/getCurrent/{id}"  )
    public Optional<Double> getCurrent( @WsParam(from = WsParam.From.PATH ) String id ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        return Optional.of( progressHolder.get().getCurrent() );
    }

    @WsMethod( path = "/increment/{id}/{value}"  )
    public Optional<Double> increment( @WsParam(from = WsParam.From.PATH ) String id,
                                       @WsParam(from = WsParam.From.PATH ) Double value ) {

        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        progressHolder.get().tick( value );
        return Optional.of( progressHolder.get().getCurrent() );
    }

    @WsMethod( path = "/increment/{id}/{value}"  )
    public Optional<Boolean> isFrozen( @WsParam(from = WsParam.From.PATH ) String id ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return Optional.empty();
        return Optional.of( progressHolder.get().isFrozen() );
    }

    @WsMethod( path = "/all"  )
    public List<ProgressHolder> allProgressors() {
        return new ArrayList<>( allProgresses.values() );
    }

    @WsMethod( path = "/getHtml/{id}", produces = "text/html" )
    public String getHtml( @WsParam(from = WsParam.From.PATH ) String id ) {
        Optional<ProgressHolder> progressHolder = get(id);
        if ( progressHolder.isEmpty() ) return "";
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<div <<aria-busy>> aria-describedby="<<id>>"/>

<label><<message>><br/><progress id="<<id>>" <<value-max>>> <br/><<message>> <<current>> %</progress>
</label>



</body>
</html>
                              """
                .replace( "<<id>>", "" + progressHolder.get().getId() )
                .replace( "<<aria-busy>>", progressHolder.get().isFrozen() ? "aria-busy=\"true\"" : "" )
                .replace( "<<current>>", "" + (int) progressHolder.get().getCurrent() )
                .replace( "<<value-max>>", progressHolder.get().isFrozen() ? "" : "value=\""+progressHolder.get().getCurrent()+"\" max=\""+progressHolder.get().getEnd()+"\"" )
                .replace( "<<start>>", "" + (int) progressHolder.get().getStart() )
                .replace( "<<end>>", "" + (int) progressHolder.get().getEnd() )
                .replace( "<<message>>", progressHolder.get().getMessage() );
    }
}
