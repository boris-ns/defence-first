import { TypeOccursMessageTemplate } from './../../models/type-occurs-message-template.model';
import { TypeOccursTemplate } from './../../models/type-occurs.model';
import { TypeMessageTemplate } from './../../models/type-message-template.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RulesService {

  private url: string;

  constructor(private http: HttpClient) {
    this.url = environment.SIEMCentarConfig.url + '/rules';
  }

  addRule(data: File) {
    const fd = new FormData();
    fd.append('file', data);
    return this.http.post(this.url, fd);
  }

  addTypeMessageRule(rule: TypeMessageTemplate) {
    return this.http.post(`${this.url}/type-message`, rule);
  }

  addTypeOccursRule(rule: TypeOccursTemplate) {
    return this.http.post(`${this.url}/type-occurs`, rule);
  }

  addTypeOccursMessageRule(rule: TypeOccursMessageTemplate) {
    return this.http.post(`${this.url}/type-occurs-message`, rule);
  }
}
