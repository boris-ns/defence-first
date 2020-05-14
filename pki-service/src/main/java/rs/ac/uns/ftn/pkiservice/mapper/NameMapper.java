package rs.ac.uns.ftn.pkiservice.mapper;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import rs.ac.uns.ftn.pkiservice.dto.request.CreateCertificateDTO;

public class NameMapper {

    public static X500Name toX500Name(CreateCertificateDTO certificateDTO) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.C, certificateDTO.getCountryCode());
        builder.addRDN(BCStyle.ST, certificateDTO.getState());
        builder.addRDN(BCStyle.L, certificateDTO.getLocality());
        builder.addRDN(BCStyle.O, certificateDTO.getOrganization());
        builder.addRDN(BCStyle.OU, certificateDTO.getOrganizationalUnit());
        builder.addRDN(BCStyle.CN, certificateDTO.getCommonName());
        return builder.build();
    }

}
