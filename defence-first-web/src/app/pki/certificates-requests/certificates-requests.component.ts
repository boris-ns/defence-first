import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service.service';
import { CertificateRequest } from 'src/app/models/certificate-request.model';

@Component({
  selector: 'app-certificates-requests',
  templateUrl: './certificates-requests.component.html',
  styleUrls: ['./certificates-requests.component.css']
})
export class CertificatesRequestsComponent implements OnInit {

  certificates: CertificateRequest[];
  displayedColumns: string[] = ['subjectData', 'view'];

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.pkiService.getCertificatesRequests().subscribe(
      (data: CertificateRequest[]) => {
        this.certificates = data;
      }
    );
  }

  view(certificate: CertificateRequest) {
    console.log(certificate);
  }
}
