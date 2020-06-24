package rs.ac.uns.ftn.siemcentar.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeMessageTemplateDTO;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeOccursTemplateDTO;

public interface RulesService {

    void addRuleService(MultipartFile rules) throws Exception;
    void createTypeMessageTemplateRule(TypeMessageTemplateDTO templateDto) throws Exception;
    void createTypeOccursTemplateRule(TypeOccursTemplateDTO templateDto) throws Exception;

}
