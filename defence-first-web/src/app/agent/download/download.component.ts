import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service/pki-service.service';
import { CSR, CSRStatus } from 'src/app/models/crs.model';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.css']
})
export class DownloadComponent implements OnInit {

  csr: CSR = null;
  certificate: string;

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.pkiService.getCSR().subscribe(
      (data: CSR) => {
        this.csr = data;
        if (data != null && data.status === CSRStatus.APPROVED) {
          this.pkiService.getCertificatByAlias(this.csr.serialNumber).subscribe(
            (d: string) => {
              this.certificate = d;
            }
          );
        }
      }
    );

  }

}
