import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CertificatesComponent } from './certificates/certificates.component';
import { AddCertificateComponent } from './add-certificate/add-certificate.component';
import { ADD_PATH } from '../config/router-paths';

const routes: Routes = [
  {
    path: '',
    component: CertificatesComponent
  },
  {
    path: ADD_PATH,
    component: AddCertificateComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PkiRoutingModule { }
