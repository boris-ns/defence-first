import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SiemCentarRouting } from './siem-centar-routing.module';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { ShowLogsComponent } from './show-logs/show-logs.component';
import { LogsSearchComponent } from './logs-search/logs-search.component';
import { ShowAlarmsComponent } from './show-alarms/show-alarms.component';
import { ReportsComponent } from './reports/reports.component';
import { ChartsModule } from 'ng2-charts';



@NgModule({
  declarations: [
    ShowLogsComponent,
    LogsSearchComponent,
    ShowAlarmsComponent,
    ReportsComponent,
  ],
  imports: [
    CommonModule,
    SiemCentarRouting,
    FormsModule,
    MaterialModule,
    ChartsModule
  ]
})
export class SiemCentarModule { }
