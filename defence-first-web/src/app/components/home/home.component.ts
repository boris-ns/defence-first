import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  name: string;

  constructor(private keycloakAngular: KeycloakService) {
  }

  ngOnInit() {
    const userDetails = this.keycloakAngular.getKeycloakInstance().tokenParsed;
    this.name = userDetails['name'];
  }

}
