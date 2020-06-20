import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { keycloakInitializer } from './utils/app-init';
import { HomeComponent } from './components/home/home.component';
import { NotAuthorizedComponent } from './components/error-pages/not-authorized/not-authorized.component';
import { NotFoundComponent } from './components/error-pages/not-found/not-found.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { MenuComponent } from './components/menu/menu.component';
import { ShowLogsComponent } from './components/shared/show-logs/show-logs.component';
import { LogsSearchComponent } from './components/shared/logs-search/logs-search.component';
import { ShowAlarmsComponent } from './components/shared/show-alarms/show-alarms.component';
import { ReportsComponent } from './components/shared/reports/reports.component';
import { ChartsModule } from 'ng2-charts';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotAuthorizedComponent,
    NotFoundComponent,
    MenuComponent,
    ShowLogsComponent,
    LogsSearchComponent,
    ShowAlarmsComponent,
    ReportsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    KeycloakAngularModule,
    BrowserAnimationsModule,
    MaterialModule,
    ChartsModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: keycloakInitializer,
      multi: true,
      deps: [KeycloakService]
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
