import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AlarmService {

  private url: string;

  constructor(private http: HttpClient) {
    this.url = environment.SIEMCentarConfig.url + '/alarm';
  }

  getAllAlarms() {
    return this.http.get(`${this.url}/all`);
  }

}
