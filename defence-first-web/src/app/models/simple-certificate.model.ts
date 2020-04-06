export interface SimpleCertificate {
    subjectData: string;
    issuerData: string;
    serialNumber: number;
    notBefore: Date;
    notAfter: Date;
}
