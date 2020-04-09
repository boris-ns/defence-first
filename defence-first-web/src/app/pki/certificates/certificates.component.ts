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
  data: SimpleCertificate[];
  displayedColumns: string[] = ['serialNumber', 'subjectData', 'issuerData', 'notBefore', 'notAfter', 'revoke'];
  checked: boolean;

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
        this.filter();
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

  filter() {
    if (this.checked) {
      const date = new Date();
      date.setMonth(date.getMonth() + 1);
      this.data = this.certificates.filter(x => date > new Date(x.notAfter));
    } else {
      this.data = this.certificates;
    }
  }

  checkClass(row: SimpleCertificate) {
    const date = new Date();
    date.setMonth(date.getMonth() + 1);
    if (date > new Date(row.notAfter)) {
      return 'warning';
    }
    return '';
  }

  cao() {
    this.pkiService.replaceCertificate('1').subscribe(
      data => {
        console.log(data);
      } 
    );
  }
}
