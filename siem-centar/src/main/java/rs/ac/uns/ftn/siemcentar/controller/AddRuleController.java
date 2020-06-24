package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.service.AddRuleService;

@RestController
@RequestMapping(value = "/api/rules")
public class AddRuleController {

    @Autowired
    private AddRuleService addRuleService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity addSkill(@RequestParam(("file")) MultipartFile q) throws Exception {
        addRuleService.addRuleService(q);
        return ResponseEntity.ok().build();
    }

}
