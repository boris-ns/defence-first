import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ShowLogsComponent } from './show-logs/show-logs.component';
import { SHOW_LOGS_PATH, SEARCH_LOGS_PATH, SHOW_ALARMS_PATH, REPORTS_PATH } from '../config/router-paths';
import { LogsSearchComponent } from './logs-search/logs-search.component';
import { ShowAlarmsComponent } from './show-alarms/show-alarms.component';
import { ReportsComponent } from './reports/reports.component';


const routes: Routes = [
    {
        path: SHOW_LOGS_PATH,
        component: ShowLogsComponent,
      },
      {
        path: SEARCH_LOGS_PATH,
        component: LogsSearchComponent,
      },
      {
        path: SHOW_ALARMS_PATH,
        component: ShowAlarmsComponent,
      },
      {
        path: REPORTS_PATH,
        component: ReportsComponent,
      },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SiemCentarRouting { }
