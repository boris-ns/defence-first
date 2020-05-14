export class CertificateCreate {

    countryCode: string;
    state: string;
    locality: string;
    organization: string;
    organizationalUnit: string;
    commonName: string;
    issuerAlias: string;

    constructor(countryCode: string, state: string, locality: string, organization: string, organizationalUnit: string,
                commonName: string, issuerAlias: string) {
        this.countryCode = countryCode;
        this.state = state;
        this.locality = locality;
        this.organization = organization;
        this. organizationalUnit = organizationalUnit;
        this.commonName = commonName;
        this.issuerAlias = issuerAlias;
    }
}
