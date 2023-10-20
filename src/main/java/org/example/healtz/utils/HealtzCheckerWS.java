/*
 *
 *  * Copyright (c) Xenoss
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 *
 */

package org.example.healtz.utils;

import oap.http.Http;
import oap.ws.Response;
import oap.ws.WsMethod;


import static oap.http.server.nio.HttpServerExchange.HttpMethod.GET;
import static org.apache.http.HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE;
import static org.example.healtz.utils.GCInfoBlock.Payloads.HIGH_LOAD;

public class HealtzCheckerWS {

    private GCInfoCollector collector = GCInfoCollector.getGCInfoCollector();

    public HealtzCheckerWS() {
        collector.setMaxEventsCount( 50 );
    }

    @WsMethod( path = "/", method = GET )
    public Response healtz() {
        if ( collector.getLastGcState() != null && collector.getLastGcState().ordinal() >= HIGH_LOAD.ordinal() ) {
            return new Response( SC_INSUFFICIENT_SPACE_ON_RESOURCE )
                .withBody( collector.getLastGcState() )
                .withContentType( Http.ContentType.TEXT_PLAIN );
        }
        return Response.ok()
            .withBody( collector.getLastGcState(), true )
            .withContentType( Http.ContentType.TEXT_PLAIN );
    }

    @WsMethod( path = "/times", method = GET )
    public Response status() {
        StringBuilder result = new StringBuilder( 128 );
        for ( GCInfoBlock block : collector.getAll() ) {
            result.append( block.getDuration() ).append( " | " );
        }
        return Response.ok()
            .withBody( result.toString() )
            .withContentType( Http.ContentType.TEXT_PLAIN );
    }

    @WsMethod( path = "/used", method = GET )
    public Response used() {
        StringBuilder result = new StringBuilder( 128 );
        for ( GCInfoBlock block : collector.getAll() ) {
            result.append( block.getMemoryUsage().getUsed() / 1024 / 1024 ).append( "MiB | " );
        }
        return Response.ok()
            .withBody( result.toString() )
            .withContentType( Http.ContentType.TEXT_PLAIN );
    }
}
