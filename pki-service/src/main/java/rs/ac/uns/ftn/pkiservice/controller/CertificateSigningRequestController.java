package rs.ac.uns.ftn.pkiservice.controller;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateSigningRequestDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateSigningRequestsMapper;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/csr")
public class CertificateSigningRequestController {

    @Autowired
    private CertificateSigningRequestService csrService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<CertificateSigningRequestDTO>> getAllRequests() {
        List<CertificateSigningRequest> requests = csrService.findAllWaitingRequests();
        return new ResponseEntity<>(CertificateSigningRequestsMapper.toListDto(requests), HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity approveRequest(@PathVariable Long id) throws Exception{
        csrService.changeStatus(id, CSRStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/decline/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity declineRequest(@PathVariable Long id) throws Exception{
        csrService.changeStatus(id, CSRStatus.DECLINED);
        return ResponseEntity.ok().build();
    }


    @PostMapping(path = "/generate", consumes = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity generate(@RequestBody String csr) throws IOException, OperatorCreationException, PKCSException {
        csrService.addRequest(csr);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/renewal", consumes = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasRole('agent')")
    public ResponseEntity renewAgentCertRequest(@RequestBody String csr) throws IOException, OperatorCreationException, PKCSException {
        csrService.addRenewRequest(csr);
        return ResponseEntity.ok().build();
    }
}
