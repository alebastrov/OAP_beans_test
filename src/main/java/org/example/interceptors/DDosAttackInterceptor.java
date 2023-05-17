package org.example.interceptors;

import lombok.ToString;
import oap.ws.InvocationContext;
import oap.ws.Response;
import oap.ws.interceptor.Interceptor;

import java.util.Optional;

import static oap.http.Http.StatusCode.FORBIDDEN;

@ToString
public class DDosAttackInterceptor implements Interceptor {
    private int delay = 1;

    public DDosAttackInterceptor(int delay) {
        this.delay = delay;
    }

    public Optional<Response> before(InvocationContext context) {
        Long lastClick = (Long) context.session.get("lastClick").orElse(null);
        if (lastClick == null) {
            lastClick = System.currentTimeMillis();
            context.session.set("lastClick", lastClick);
            return Optional.empty();
        }
        long previousClick = System.currentTimeMillis();
        if (Math.abs( lastClick - previousClick) > delay * 1000 ) {
            previousClick = System.currentTimeMillis();
            context.session.set("lastClick", previousClick);
            return Optional.empty();
        }
        return Optional.of( new Response( FORBIDDEN, "Please wait " + delay + " seconds before next attempt" ) );
    }
}
