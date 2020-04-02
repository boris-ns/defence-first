import { ROLE_ADMIN, ROLE_OPERATOR } from './../config/user-roles';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private keycloakService: KeycloakService) {
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
}
