import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service.service';

@Component({
  selector: 'app-add-certificate',
  templateUrl: './add-certificate.component.html',
  styleUrls: ['./add-certificate.component.css']
})
export class AddCertificateComponent implements OnInit {

  constructor(
    private pkiService: PkiServiceService
  ) { }

  ngOnInit() {
    this.pkiService.generateCertificatIntermediate({subjectData:"aa", issuerAlias: 'df.pki.root'}).subscribe(
      data => {
        console.log(data);
      }
    )
  }

}
