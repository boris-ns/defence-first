export interface CertificateRequest {
    subjectData: string;
    extensions: Map<string, string>;
}
