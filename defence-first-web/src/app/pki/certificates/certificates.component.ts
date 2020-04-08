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
  displayedColumns: string[] = ['serialNumber', 'subjectData', 'issuerData', 'notBefore', 'notAfter', 'revoke'];

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.getCertificates();
  }

  private getCertificates() {
    this.pkiService.getCertificates().subscribe(
      (data: SimpleCertificate[]) => {
        this.certificates = data;
      }
    );
  }

  onClickRevoke(certificate: SimpleCertificate) {
    this.pkiService.revokeCertificate(certificate.serialNumber).subscribe(data => {
      this.getCertificates();
    }, error => {
      console.log(error);
    });
  }
}
