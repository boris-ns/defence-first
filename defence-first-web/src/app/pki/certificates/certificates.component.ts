import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service.service';
import { SimpleCertificate } from 'src/app/models/simple-certificate.model';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.css']
})
export class CertificatesComponent implements OnInit {

  certificates: SimpleCertificate[];

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.pkiService.getCertificates().subscribe(
      (data: SimpleCertificate[]) => {
        this.certificates = data;
      }
    );
  }

}
