package org.example.webservice;


import oap.application.Kernel;
import oap.ws.WsMethod;
import oap.ws.WsParam;
import org.example.KernelHolder;
import org.example.template.HtmlTableTemplate;


public class KernelDemoWS {
    private final Kernel kernel;

    public KernelDemoWS(KernelHolder kernelHolder) {
        this.kernel = kernelHolder.getKernel();
    }

    @WsMethod( path = "/services", produces = "text/html" )
    public String getKernel() {
        var table = new HtmlTableTemplate();
        table.setTableStyle("border-collapse: collapse;border: 2px solid rgb(200,200,200);");
        table.setCellStyle("border: 1px solid;");

        kernel.services.values()
                .forEach(si -> {
                    table.addCell(si.module.getName(), si.implementationName, si.module.getName().equals("simple-init-module") ? "/kernel/service/modules."+si.module.getName()+"."+si.implementationName : "");
                } );

        return "<html><body><div style='color:red'>Do not click often, than once in 2 seconds</div>" +
                table +
                "</body></html>";
    }

    @WsMethod( path = "/service/{name}" )
    public String getService(@WsParam(from = WsParam.From.PATH) String name) {
        Object service = kernel.service(name).orElse(null);
        return service == null ? "-" : service.toString();
    }

}
