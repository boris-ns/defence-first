import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CertificatesComponent } from './certificates/certificates.component';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';



@NgModule({
  declarations: [
    CertificatesComponent,
    AddCertificateComponent
  ],
  imports: [
    CommonModule
  ]
})
export class PkiModuleModule { }
