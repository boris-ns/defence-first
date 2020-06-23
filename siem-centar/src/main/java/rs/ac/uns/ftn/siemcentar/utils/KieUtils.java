package rs.ac.uns.ftn.siemcentar.utils;

import org.apache.maven.shared.invoker.*;
import rs.ac.uns.ftn.siemcentar.constants.KieConstants;

import java.io.File;
import java.util.Collections;

public class KieUtils {

    public static void installKjar() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        File f = new File(KieConstants.KJAR_POM_PATH );
        request.setPomFile(f);
        request.setGoals(Collections.singletonList( "install" ));

        Invoker invoker = new DefaultInvoker();
        // dok ne namestim M2_HOME da radi..
        invoker.setMavenHome(new File(KieConstants.MAVEN_HOME));
        InvocationResult result =  invoker.execute( request );
        if (result.getExitCode() != 0) {
            System.out.println(result.getExecutionException().toString());
            System.out.println(result.getExitCode());
        }
    }
}
