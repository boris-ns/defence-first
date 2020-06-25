import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';

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

  sendCSR(data: File) {
    const fd = new FormData();
    fd.append('file', data);
    return this.http.post(this.urlCSR + '/generate', fd);
  }

  renewalCSR(data: File) {
    const fd = new FormData();
    fd.append('file', data);
    return this.http.post(this.urlCSR + '/renewal', fd);
  }

  getCSR() {
    return this.http.get(this.urlCSR + '/my_certs');
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

  getCertificatByAlias(alias: string) {
    return this.http.get(this.urlCertificates + '/alias/' + alias, {responseType: 'text'});
  }

  revokeCertificate(serialNumber: number) {
    return this.http.post(`${this.urlOCSP}/${serialNumber}`, {});
  }

  replaceCertificate(serialNumber: number) {
    return this.http.put(`${this.urlCertificates}/replace/${serialNumber}`, {});
  }
}
