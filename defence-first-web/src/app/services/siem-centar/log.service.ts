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
    this.url = environment.SIEMCentarConfig.url;
  }

  getAllLogs() {
    return this.http.get(`${this.url}/log/findAll`);
  }

  filterLogs(filter: LogFilterDTO) {
    return this.http.post(`${this.url}/log/filter`, filter);
  }
}
