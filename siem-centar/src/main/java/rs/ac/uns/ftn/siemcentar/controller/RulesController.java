package rs.ac.uns.ftn.siemcentar.controller;

import org.drools.template.ObjectDataCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.constants.KieConstants;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeMessageTemplateDTO;
import rs.ac.uns.ftn.siemcentar.model.LogType;
import rs.ac.uns.ftn.siemcentar.model.template.TypeMessageTemplate;
import rs.ac.uns.ftn.siemcentar.service.RulesService;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity createClassifyBuyersRules(@Valid @RequestBody TypeMessageTemplateDTO templateDto) throws Exception {
        rulesService.createTypeMessageTemplateRule(templateDto);
        return ResponseEntity.ok().build();
    }
}
