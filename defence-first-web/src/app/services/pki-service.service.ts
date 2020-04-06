import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PkiServiceService {

  private urlOCSP: string;
  private urlCertificates: string;

  constructor(private http: HttpClient) {
    this.urlOCSP = environment.PKIServiceConfig.url + '/ocsp';
    this.urlCertificates = environment.PKIServiceConfig.url + '/certificates';
  }

  getCertificates() {
    return this.http.get(this.urlCertificates + '/all');
  }

  checkCrlList( id: number) {
    return this.http.get(this.urlOCSP + '/' + id);
  }

  generateCertificat() {
    return this.http.get(this.urlCertificates + '/generate');
  }

  getCertificatByAlias(alias: string) {
    return this.http.get(this.urlCertificates + '/alias/' + alias);
  }

}
