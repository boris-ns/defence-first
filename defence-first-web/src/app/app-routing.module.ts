import { AddRulesComponent } from './components/shared/add-rules/add-rules.component';
import { ReportsComponent } from './components/shared/reports/reports.component';
import { AdminAuthorizationGuard } from './guards/admin-authorization.guard';
import { NotAuthorizedComponent } from './components/error-pages/not-authorized/not-authorized.component';
import { NOT_FOUND, NOT_AUTHORIZED, HOME_PATH, CERTIFICATES_PATH, SHOW_LOGS_PATH, SEARCH_LOGS_PATH,
   SHOW_ALARMS_PATH, REPORTS_PATH, ADD_RULES_PATH} from './config/router-paths';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NotFoundComponent } from './components/error-pages/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';
import { ShowLogsComponent } from './components/shared/show-logs/show-logs.component';
import { LogsSearchComponent } from './components/shared/logs-search/logs-search.component';
import { ShowAlarmsComponent } from './components/shared/show-alarms/show-alarms.component';
import { OperatorAndAdimnAuthorizationGuard } from './guards/operator-admin-authorization.guard';

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
    path: SHOW_LOGS_PATH,
    component: ShowLogsComponent,
    canActivate: [OperatorAndAdimnAuthorizationGuard]
  },
  {
    path: SEARCH_LOGS_PATH,
    component: LogsSearchComponent,
    canActivate: [OperatorAndAdimnAuthorizationGuard]
  },
  {
    path: SHOW_ALARMS_PATH,
    component: ShowAlarmsComponent,
    canActivate: [OperatorAndAdimnAuthorizationGuard]
  },
  {
    path: REPORTS_PATH,
    component: ReportsComponent,
    canActivate: [OperatorAndAdimnAuthorizationGuard]
  },
  {
    path: ADD_RULES_PATH,
    component: AddRulesComponent,
    canActivate: [AdminAuthorizationGuard]
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
    redirectTo: HOME_PATH,
    pathMatch: 'full'
  },
  {
    path: CERTIFICATES_PATH,
    loadChildren: () => import('./pki/pki.module').then(m => m.PkiModule),
    canActivate: [AdminAuthorizationGuard]
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
