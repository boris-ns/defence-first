import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';
import { CertificatesComponent } from './certificates/certificates.component';



@NgModule({
  declarations: [AddCertificateComponent, CertificatesComponent],
  imports: [
    CommonModule
  ]
})
export class PkiModule { }
