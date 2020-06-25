import { SHOW_LOGS_PATH, SEARCH_LOGS_PATH, SHOW_ALARMS_PATH, REPORTS_PATH, ADD_RULES_PATH,
  SIEM_PATH, AGENT_PATH, DOWNLOAD_PATH } from './../../config/router-paths';
import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { CERTIFICATES_PATH, ADD_PATH, REQUEST_PATH } from 'src/app/config/router-paths';
import { DownloadComponent } from 'src/app/agent/download/download.component';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  admin: boolean;
  operator: boolean;
  agent: boolean;

  constructor(private keycloakAngular: KeycloakService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.admin = this.authService.isUserAdmin();
    this.operator = this.authService.isUserOperator();
    this.agent = this.authService.isUserAgent();
  }

  certificates() {
    this.router.navigate([CERTIFICATES_PATH]);
  }

  certificatesRequests() {
    this.router.navigate([CERTIFICATES_PATH, REQUEST_PATH]);
  }

  addCertificates() {
    this.router.navigate([CERTIFICATES_PATH, ADD_PATH]);
  }

  showLogs() {
    this.router.navigate([SIEM_PATH, SHOW_LOGS_PATH]);
  }

  searchLogs() {
    this.router.navigate([SIEM_PATH, SEARCH_LOGS_PATH]);
  }

  showAlarms() {
    this.router.navigate([SIEM_PATH, SHOW_ALARMS_PATH]);
  }

  showReports() {
    this.router.navigate([SIEM_PATH, REPORTS_PATH]);
  }

  addRules() {
    this.router.navigate([ADD_RULES_PATH]);
  }

  request() {
    this.router.navigate([AGENT_PATH]);
  }

  download() {
    this.router.navigate([AGENT_PATH, DOWNLOAD_PATH]);
  }

  logOut() {
    this.keycloakAngular.logout();
  }

}
