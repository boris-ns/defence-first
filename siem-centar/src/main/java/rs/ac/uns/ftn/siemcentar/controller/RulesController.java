package rs.ac.uns.ftn.siemcentar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeMessageTemplateDTO;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeOccursTemplateDTO;
import rs.ac.uns.ftn.siemcentar.service.RulesService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/rules")
public class RulesController {

    @Autowired
    private RulesService rulesService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity addRuleFromFile(@RequestParam(("file")) MultipartFile q) throws Exception {
        rulesService.addRuleService(q);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/type-message")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity createTypeMessageTemplateRule(@Valid @RequestBody TypeMessageTemplateDTO templateDto) throws Exception {
        rulesService.createTypeMessageTemplateRule(templateDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/type-occurs")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity createTypeOccursTemplate(@Valid @RequestBody TypeOccursTemplateDTO templateDto) throws Exception {
        rulesService.createTypeOccursTemplateRule(templateDto);
        return ResponseEntity.ok().build();
    }

}
