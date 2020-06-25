import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrsComponent } from './crs/crs.component';
import { DownloadComponent } from './download/download.component';
import { AgentRoutingModule } from './agent-routing.module';
import { MaterialModule } from '../material/material.module';



@NgModule({
  declarations: [
    CrsComponent,
    DownloadComponent],
  imports: [
    CommonModule,
    AgentRoutingModule,
    MaterialModule
  ]
})
export class AgentModule { }
