package rs.ac.uns.ftn.siemcentar.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.siemcentar.service.AddRuleService;
import rs.ac.uns.ftn.siemcentar.utils.FileUtils;
import rs.ac.uns.ftn.siemcentar.utils.KieUtils;

@Service
public class AddRuleServiceImpl implements AddRuleService {

    @Override
    public void addRuleService(MultipartFile rules) throws Exception {
        FileUtils.writeToKjarFile(rules);
        KieUtils.installKjar();
    }
}
