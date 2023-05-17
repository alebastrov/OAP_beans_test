package org.example.webservice;


import oap.application.Kernel;
import oap.ws.WsMethod;
import oap.ws.WsParam;
import org.example.KernelHolder;


public class KernelDemoWS {
    private Kernel kernel;

    public KernelDemoWS(KernelHolder kernelHolder) {
        this.kernel = kernelHolder.getKernel();
    }

    @WsMethod( path = "/services", produces = "text/html" )
    public String getKernel() {
        return "<html><body><table><th><tr><td><b>module name</b></td><td><b>service/bean name</b></td></tr></th>" +
                kernel.services.values().stream()
                        .map(si -> {
                            return "<tr>" +
                                    "<td>" + si.module.getName() + "</td>" +
                                    (si.module.getName().equals("simple-init-module")
                                     ? "<td><a href='/kernel/service/modules." + si.module.getName() + "." + si.implementationName + "'>" + si.implementationName + "</a></td>"
                                     : "<td>" + si.implementationName + "</td>"
                                    ) +
                                    "</tr>";
                        })
                        .toList().toString() +
                "</table></body></html>";
    }

    @WsMethod( path = "/service/{name}" )
    public String getService(@WsParam(from = WsParam.From.PATH) String name) {
        Object service = kernel.service(name).orElse(null);
        return service == null ? "-" : service.toString();
    }

}
