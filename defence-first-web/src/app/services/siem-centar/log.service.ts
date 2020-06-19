import { LogFilterDTO } from './../../models/log-filter.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  private url: string;

  constructor(private http: HttpClient) {
    this.url = environment.SIEMCentarConfig.url + '/log';
  }

  getAllLogs() {
    return this.http.get(`${this.url}/findAll`);
  }

  filterLogs(filter: LogFilterDTO) {
    return this.http.post(`${this.url}/filter`, filter);
  }
}
