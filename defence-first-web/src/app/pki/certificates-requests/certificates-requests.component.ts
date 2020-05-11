import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service/pki-service.service';
import { CertificateRequest } from 'src/app/models/certificate-request.model';

@Component({
  selector: 'app-certificates-requests',
  templateUrl: './certificates-requests.component.html',
  styleUrls: ['./certificates-requests.component.css']
})
export class CertificatesRequestsComponent implements OnInit {

  certificates: CertificateRequest[];
  displayedColumns: string[] = ['id', 'subjectName', 'issuerName', 'type', 'approve', 'decline'];

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.getAllRequests();
  }

  private getAllRequests() {
    this.pkiService.getCertificatesRequests().subscribe((data: CertificateRequest[]) => {
        this.certificates = data;
      }
    );
  }

  approveRequest(request: CertificateRequest) {
    this.pkiService.approveCertificateRequest(request.id).subscribe(data => {
      this.getAllRequests();
    }, error => {
      console.log(error);
    });
  }

  declineRequest(request: CertificateRequest) {
    this.pkiService.declineCertificateRequest(request.id).subscribe(data => {
      this.getAllRequests();
    }, error => {
      console.log(error);
    });
  }
}
