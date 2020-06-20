import { SHOW_LOGS_PATH, SEARCH_LOGS_PATH, SHOW_ALARMS_PATH, REPORTS_PATH } from './../../config/router-paths';
import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { CERTIFICATES_PATH, ADD_PATH, REQUEST_PATH } from 'src/app/config/router-paths';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  admin: boolean;
  operator: boolean;

  constructor(private keycloakAngular: KeycloakService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.admin = this.authService.isUserAdmin();
    this.operator = this.authService.isUserOperator();
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
    this.router.navigate([SHOW_LOGS_PATH]);
  }

  searchLogs() {
    this.router.navigate([SEARCH_LOGS_PATH]);
  }

  showAlarms() {
    this.router.navigate([SHOW_ALARMS_PATH]);
  }

  showReports() {
    this.router.navigate([REPORTS_PATH]);
  }

  logOut() {
    this.keycloakAngular.logout();
  }

}
