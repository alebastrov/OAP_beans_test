package org.example;


import lombok.ToString;
import oap.application.Kernel;

@ToString
public class KernelHolder {
    private Kernel kernel;

    public KernelHolder(Kernel kernel) {
        this.kernel = kernel;
    }

    public Kernel getKernel() {
        return kernel;
    }
}
