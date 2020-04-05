import { AdminAuthorizationGuard } from './guards/admin-authorization.guard';
import { OperatorAuthorizationGuard } from './guards/operator-authorization.guard';
import { NotAuthorizedComponent } from './components/error-pages/not-authorized/not-authorized.component';
import { NOT_FOUND, NOT_AUTHORIZED, HOME_PATH, CERTIFICATES_PATH, ADD_CERTIFICATES_PATH } from './config/router-paths';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NotFoundComponent } from './components/error-pages/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';
import { CertificatesComponent } from './pki/certificates/certificates.component';
import { AddCertificateComponent } from './pki/add-certificate/add-certificate.component';


// !!! Guards !!!
// canActivate: [OperatorAuthorizationGuard],
// dodati ovo u objekat za putanju ako zelis da samo Operator ima pristup toj stranici
// drugi guard koji postoji je AdminAuthorizationGuard

const routes: Routes = [
  {
    path: HOME_PATH,
    component: HomeComponent
  },
  {
    path: CERTIFICATES_PATH,
    component: CertificatesComponent
  },
  {
    path: ADD_CERTIFICATES_PATH,
    component: AddCertificateComponent
  },

  {
    path: NOT_FOUND,
    component: NotFoundComponent
  },
  {
    path: NOT_AUTHORIZED,
    component: NotAuthorizedComponent
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  // This must be last!
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
