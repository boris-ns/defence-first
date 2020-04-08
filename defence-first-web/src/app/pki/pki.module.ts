import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PkiRoutingModule } from './pki-routing.module';
import { CertificatesComponent } from './certificates/certificates.component';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';
import { CertificatesRequestsComponent } from './certificates-requests/certificates-requests.component';
import { MaterialModule } from '../material/material.module';


@NgModule({
  declarations: [
    CertificatesComponent,
    AddCertificateComponent,
    CertificatesRequestsComponent],
  imports: [
    CommonModule,
    PkiRoutingModule,
    MaterialModule
  ]
})
export class PkiModule { }