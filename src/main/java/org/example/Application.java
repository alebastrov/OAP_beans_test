package org.example;

import lombok.extern.slf4j.Slf4j;
import oap.application.ApplicationConfiguration;
import oap.application.Boot;
import oap.application.Kernel;
import oap.application.ModuleConfiguration;
import oap.application.module.Module;
import oap.application.remote.RemoteInvocationException;
import oap.testng.Fixtures;

import java.nio.file.Path;

@Slf4j
public class Application extends Fixtures {

    public static void main(String[] args) throws Exception {

        Path config = Path.of("/Users/mac/IdeaProjects/OAP_beans_test/target/classes/application.conf");

        Boot.start( config, config.getParent().resolve( "conf.d" ) );

        log.info(" Server is started...");
        Thread.currentThread().sleep(1800);
    }
}
