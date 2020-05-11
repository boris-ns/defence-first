import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { PkiServiceService } from 'src/app/services/pki-service/pki-service.service';
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

  constructor(private keycloakAngular: KeycloakService,
              private pkiService: PkiServiceService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.admin = this.authService.isUserAdmin();
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

  logOut() {
    this.keycloakAngular.logout();
  }

}
