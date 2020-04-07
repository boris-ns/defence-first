import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CertificatesComponent } from './certificates/certificates.component';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';
import { ADD_PATH, REQUEST_PATH } from '../config/router-paths';
import { CertificatesRequestsComponent } from './certificates-requests/certificates-requests.component';

const routes: Routes = [
  {
    path: '',
    component: CertificatesComponent
  },
  {
    path: ADD_PATH,
    component: AddCertificateComponent
  },
  {
    path: REQUEST_PATH,
    component: CertificatesRequestsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PkiRoutingModule { }
