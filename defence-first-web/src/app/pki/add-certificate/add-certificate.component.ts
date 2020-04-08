import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service.service';
import { CertificateCreate, Name } from 'src/app/models/certificate-create.model';
import { CERTIFICATES_PATH } from 'src/app/config/router-paths';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-certificate',
  templateUrl: './add-certificate.component.html',
  styleUrls: ['./add-certificate.component.css']
})
export class AddCertificateComponent implements OnInit {

  certificate: CertificateCreate = new CertificateCreate( new Name('', '', '', '', '', ''), '');
  countries: object[];
  aliases: string[];
  issuers: object[];

  constructor(
    private pkiService: PkiServiceService,
    private router: Router
  ) { }

  ngOnInit() {
    const countriesQuery = require('countries-code');
    this.countries = countriesQuery.allCountriesList();
    this.pkiService.getIssuer().subscribe(
      (data: object[]) => {
        this.issuers = data;
      }
    );
  }

  onSubmit() {
    this.pkiService.generateCertificatIntermediate(this.certificate).subscribe(
      data => {
        this.router.navigate([CERTIFICATES_PATH]);
      }
    );
  }

}
