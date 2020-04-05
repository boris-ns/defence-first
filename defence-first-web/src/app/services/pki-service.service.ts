import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PkiServiceService {

  private url: string;
  constructor(private http: HttpClient) {
    this.url = environment.PKIServiceConfig.url;
  }

  checkCrlList( id: number) {
    return this.http.get(this.url + '/crl/' + id);
  }

  generateCertificat() {
    return this.http.get(this.url + '/certificates/generate');
  }

  getCertificatByAlias(alias: string) {
    return this.http.get(this.url + '/certificates/alias/' + alias);
  }

}
