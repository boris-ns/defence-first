import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service.service';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.css']
})
export class CertificatesComponent implements OnInit {

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.pkiService.getCertificates().subscribe(
      data => {
        console.log(data);
      }
    )
  }

}
