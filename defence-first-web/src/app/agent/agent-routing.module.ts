import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DOWNLOAD_PATH } from '../config/router-paths';
import { CrsComponent } from './crs/crs.component';
import { DownloadComponent } from './download/download.component';

const routes: Routes = [
  {
    path: '',
    component: CrsComponent
  },
  {
    path: DOWNLOAD_PATH,
    component: DownloadComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AgentRoutingModule { }
