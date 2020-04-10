export interface SimpleCertificate {
    subjectData: string;
    issuerData: string;
    serialNumber: number;
    notBefore: Date;
    notAfter: Date;
    revoked: boolean;
    type: Type;
}

enum Type {
    ROOT_CERT, INTERMEDIATE_CERT, LEAF_CERT
}
