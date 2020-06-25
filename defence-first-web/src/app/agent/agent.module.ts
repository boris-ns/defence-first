import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CrsComponent } from './crs/crs.component';
import { DownloadComponent } from './download/download.component';
import { AgentRoutingModule } from './agent-routing.module';



@NgModule({
  declarations: [
    CrsComponent,
    DownloadComponent],
  imports: [
    CommonModule,
    AgentRoutingModule
  ]
})
export class AgentModule { }
