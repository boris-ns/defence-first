package rs.ac.uns.ftn.siemcentar.service.impl;

import org.drools.template.ObjectDataCompiler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.constants.KieConstants;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeMessageTemplateDTO;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeOccursMessageTemplateDTO;
import rs.ac.uns.ftn.siemcentar.model.template.TypeMessageTemplate;
import rs.ac.uns.ftn.siemcentar.dto.request.templates.TypeOccursTemplateDTO;
import rs.ac.uns.ftn.siemcentar.model.template.TypeOccursMessageTemplate;
import rs.ac.uns.ftn.siemcentar.model.template.TypeOccursTemplate;
import rs.ac.uns.ftn.siemcentar.service.RulesService;
import rs.ac.uns.ftn.siemcentar.utils.FileUtils;
import rs.ac.uns.ftn.siemcentar.utils.KieUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class RulesServiceImpl implements RulesService {

    @Override
    public void addRuleService(MultipartFile rules) throws Exception {
        FileUtils.writeToKjarFile(rules);
        KieUtils.installKjar();
    }

    @Override
    public void createTypeMessageTemplateRule(TypeMessageTemplateDTO templateDto) throws Exception {
        InputStream templateFile = RulesServiceImpl.class
                .getResourceAsStream(KieConstants.TYPE_MESSAGE_TEMPLATE_PATH);

        List<TypeMessageTemplate> data = new ArrayList<>();
        TypeMessageTemplate template = new TypeMessageTemplate(
                templateDto.getType(), templateDto.getMessageRegex(), templateDto.getAlarmMessage()
        );
        data.add(template);

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateFile);

        System.out.print(drl);
        FileUtils.writeToKjarFile(drl);
        KieUtils.installKjar();
    }

    @Override
    public void createTypeOccursTemplateRule(TypeOccursTemplateDTO templateDto) throws Exception {
        InputStream templateFile = RulesServiceImpl.class
                .getResourceAsStream(KieConstants.TYPE_OCCURS_TEMPLATE_PATH);

        List<TypeOccursTemplate> data = new ArrayList<>();
        TypeOccursTemplate template = new TypeOccursTemplate(
                templateDto.getType(), templateDto.getOccurs(), templateDto.getAlarmMessage(), templateDto.getTime()
        );
        data.add(template);

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateFile);

        System.out.print(drl);
        FileUtils.writeToKjarFile(drl);
        KieUtils.installKjar();
    }

    @Override
    public void createTypeOccursMessageTemplateRule(TypeOccursMessageTemplateDTO templateDto) throws Exception {
        InputStream templateFile = RulesServiceImpl.class
                .getResourceAsStream(KieConstants.TYPE_OCCURS_MESSAGE_TEMPLATE_PATH);

        List<TypeOccursMessageTemplate> data = new ArrayList<>();
        TypeOccursMessageTemplate template = new TypeOccursMessageTemplate(
                templateDto.getType(), templateDto.getOccurs(), templateDto.getMessageRegex(),
                templateDto.getAlarmMessage(), templateDto.getTime()
        );
        data.add(template);

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateFile);

        System.out.print(drl);
        FileUtils.writeToKjarFile(drl);
        KieUtils.installKjar();
    }
}
