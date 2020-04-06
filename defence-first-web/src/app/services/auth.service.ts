import { ROLE_ADMIN, ROLE_OPERATOR } from './../config/user-roles';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { HOME_PATH } from '../config/router-paths';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private keycloakService: KeycloakService) {
  }

  getUsername(): string {
    return this.keycloakService.getUsername();
  }
  getUserRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }

  isUserAdmin(): boolean {
    return this.getUserRoles().includes(ROLE_ADMIN);
  }

  isUserOperator(): boolean {
    return this.getUserRoles().includes(ROLE_OPERATOR);
  }

  logOut() {
    this.keycloakService.logout(HOME_PATH);
  }
}
