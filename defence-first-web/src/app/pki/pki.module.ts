import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PkiRoutingModule } from './pki-routing.module';
import { CertificatesComponent } from './certificates/certificates.component';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';


@NgModule({
  declarations: [
    CertificatesComponent,
    AddCertificateComponent],
  imports: [
    CommonModule,
    PkiRoutingModule
  ]
})
export class PkiModule { }
