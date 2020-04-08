package rs.ac.uns.ftn.pkiservice.mapper;

import rs.ac.uns.ftn.pkiservice.dto.response.CertificateSigningRequestDTO;
import rs.ac.uns.ftn.pkiservice.models.CertificateSigningRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CertificateSigningRequestsMapper {

    public static List<CertificateSigningRequestDTO> toListDto(List<CertificateSigningRequest> requests) {
        return requests.stream()
                .map(CertificateSigningRequestDTO::new).collect(Collectors.toList());
    }

    private CertificateSigningRequestsMapper() {
    }
}
