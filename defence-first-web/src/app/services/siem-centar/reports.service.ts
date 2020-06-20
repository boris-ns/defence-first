import { ReportRequestDTO } from './../../models/report-request.model';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {

  private url: string;

  constructor(private http: HttpClient) {
    this.url = environment.SIEMCentarConfig.url + '/reports';
  }

  getReports(request: ReportRequestDTO): Observable<any> {
    return this.http.post(this.url, request);
  }
}
