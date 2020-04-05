import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { PkiServiceService } from 'src/app/services/pki-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';
import { Router } from '@angular/router';
import { CERTIFICATES_PATH, ADD_CERTIFICATES_PATH } from 'src/app/config/router-paths';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  name: string;

  constructor(private pkiService: PkiServiceService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.name = this.authService.getUsername();
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

}
