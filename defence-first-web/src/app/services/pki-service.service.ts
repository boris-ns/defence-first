import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PkiServiceService {

  private urlOCSP: string;
  private urlCertificates: string;
  private urlCSR: string;

  constructor(private http: HttpClient) {
    this.urlOCSP = environment.PKIServiceConfig.url + '/ocsp';
    this.urlCSR = environment.PKIServiceConfig.url + '/csr';
    this.urlCertificates = environment.PKIServiceConfig.url + '/certificates';
  }

  getCertificates() {
    return this.http.get(this.urlCertificates + '/all');
  }

  getIssuer() {
    return this.http.get(this.urlCertificates + '/all/intermediate');
  }

  getCertificatesRequests() {
    return this.http.get(this.urlCSR);
  }

  approveCertificateRequest(id: number) {
    return this.http.put(`${this.urlCSR}/approve/${id}`, {});
  }

  declineCertificateRequest(id: number) {
    return this.http.put(`${this.urlCSR}/decline/${id}`, {});
  }

  checkCrlList( id: number) {
    return this.http.get(this.urlOCSP + '/' + id);
  }

  generateCertificat() {
    return this.http.get(this.urlCertificates + '/generate');
  }

  generateCertificatIntermediate(certificate: any) {
    return this.http.post(this.urlCertificates + '/generate/intermediate', certificate);
  }

  replaceCertificate(serialNumber: string) {
    return this.http.put(this.urlCertificates + '/replace/' + serialNumber, {});
  }

  getCertificatByAlias(alias: string) {
    return this.http.get(this.urlCertificates + '/alias/' + alias);
  }

  revokeCertificate(serialNumber: number) {
    return this.http.post(`${this.urlOCSP}/${serialNumber}`, {});
  }

}
