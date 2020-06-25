import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgentRoutingModule } from './agent-routing.module';
import { MaterialModule } from '../material/material.module';
import { DownloadComponent } from './download/download.component';
import { CrsComponent } from './crs/crs.component';



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
