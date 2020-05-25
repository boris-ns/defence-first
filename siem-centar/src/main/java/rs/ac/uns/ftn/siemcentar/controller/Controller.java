package rs.ac.uns.ftn.siemcentar.controller;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.siemcentar.model.facts.Item;

@RestController
@RequestMapping(value = "/api")
public class Controller {

    @Autowired
    private KieContainer kieContainer;

    @GetMapping(path = "/helloworld")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> helloWorld() {
        // Test drools
        Item item = new Item();
        getClassifiedItem(item);

        return new ResponseEntity<>("Hello world", HttpStatus.OK);
    }

    public Item getClassifiedItem(Item i) {
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(i);
        kieSession.fireAllRules();
        kieSession.dispose();
        return i;
    }
}
