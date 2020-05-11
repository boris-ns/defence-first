package rs.ac.uns.ftn.siemcentar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.siemcentar.model.Log;
import rs.ac.uns.ftn.siemcentar.service.LogService;

@SpringBootApplication
public class SiemCentarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiemCentarApplication.class, args);
    }

}
