import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { PkiServiceService } from 'src/app/services/pki-service.service';
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
