import { AddRulesComponent } from './components/shared/add-rules/add-rules.component';
import { ReportsComponent } from './siem-centar/reports/reports.component';
import { AdminAuthorizationGuard } from './guards/admin-authorization.guard';
import { NotAuthorizedComponent } from './components/error-pages/not-authorized/not-authorized.component';
import { NOT_FOUND, NOT_AUTHORIZED, HOME_PATH, CERTIFICATES_PATH, SHOW_LOGS_PATH, SEARCH_LOGS_PATH,
   SHOW_ALARMS_PATH, REPORTS_PATH, ADD_RULES_PATH, SIEM_PATH, AGENT_PATH} from './config/router-paths';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { NotFoundComponent } from './components/error-pages/not-found/not-found.component';
import { HomeComponent } from './components/home/home.component';
import { ShowLogsComponent } from './siem-centar/show-logs/show-logs.component';
import { LogsSearchComponent } from './siem-centar/logs-search/logs-search.component';
import { ShowAlarmsComponent } from './siem-centar/show-alarms/show-alarms.component';
import { OperatorAndAdimnAuthorizationGuard } from './guards/operator-admin-authorization.guard';
import { AgentAuthorizationGuard } from './guards/agent-authorization.guard';

const routes: Routes = [
  {
    path: HOME_PATH,
    component: HomeComponent
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
  {
    path: SIEM_PATH,
    loadChildren: () => import('./siem-centar/siem-centar.module').then(m => m.SiemCentarModule),
    canActivate: [OperatorAndAdimnAuthorizationGuard]
  },
  {
    path: AGENT_PATH,
    loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
    canActivate: [AgentAuthorizationGuard]
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
