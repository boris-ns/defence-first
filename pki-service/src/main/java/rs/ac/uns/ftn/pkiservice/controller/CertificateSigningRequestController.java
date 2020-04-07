package rs.ac.uns.ftn.pkiservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.pkiservice.dto.response.CertificateSigningRequestDTO;
import rs.ac.uns.ftn.pkiservice.mapper.CertificateSigningRequestsMapper;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;
import rs.ac.uns.ftn.pkiservice.models.enums.CSRStatus;
import rs.ac.uns.ftn.pkiservice.service.CertificateSigningRequestService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/csr")
public class CertificateSigningRequestController {

    @Autowired
    private CertificateSigningRequestService csrService;

    @GetMapping
    public ResponseEntity<List<CertificateSigningRequestDTO>> getAllRequests() {
        List<CertificateSigningRequest> requests = csrService.findAllWaitingRequests();
        return new ResponseEntity<>(CertificateSigningRequestsMapper.toListDto(requests), HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity approveRequest(@PathVariable Long id) throws Exception{
        csrService.changeStatus(id, CSRStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/decline/{id}")
    public ResponseEntity declineRequest(@PathVariable Long id) throws Exception{
        csrService.changeStatus(id, CSRStatus.DECLINED);
        return ResponseEntity.ok().build();
    }
}
