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
//        createOrUpdateProgress("123", Optional.of( 1.0), Optional.of( 100.0), Optional.of( "Creating..." ));
    }

    private Optional<ProgressHolder> get(String id ) {
        ProgressHolder progressHolder = allProgresses.get(id);
        return Optional.ofNullable( progressHolder );
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
}
