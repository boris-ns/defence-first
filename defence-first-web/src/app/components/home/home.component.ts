import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { PkiServiceService } from 'src/app/services/pki-service.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  name: string;

  constructor(private keycloakAngular: KeycloakService,
              private pkiService: PkiServiceService) {
  }

  ngOnInit() {
    const userDetails = this.keycloakAngular.getKeycloakInstance().tokenParsed;
    this.name = userDetails['name'];
  }

  getSertificates() {
    this.pkiService.getCertificatByAlias('df.pki.root').subscribe(
      res => {
        console.log(res);
      }
    );
    // this.pkiService.generateCertificat().subscribe(
    //   res => {
    //     console.log(res);
    //   }
    // );

    // this.pkiService.checkCrlList(1).subscribe(res => {
    //   console.log(res);
    // },
    // (err: HttpErrorResponse) => {
    //   console.log(err.message);
    // });
  }

  logOut() {
    this.keycloakAngular.logout();
  }

}
