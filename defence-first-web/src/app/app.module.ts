import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { KeycloakService, KeycloakAngularModule } from 'keycloak-angular';
import { keycloakInitializer } from './utils/app-init';
import { HomeComponent } from './components/home/home.component';
import { NotAuthorizedComponent } from './components/error-pages/not-authorized/not-authorized.component';
import { NotFoundComponent } from './components/error-pages/not-found/not-found.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { MenuComponent } from './components/menu/menu.component';
import { AddRulesComponent } from './components/shared/add-rules/add-rules.component';
import { ToastrModule } from 'ngx-toastr';
import { ErrorDialogService } from './services/error-dialog-service/error-dialog.service';
import { ErrorHandlingInterceptor } from './interceptors/error.interceptor';
import { Router } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NotAuthorizedComponent,
    NotFoundComponent,
    MenuComponent,
    AddRulesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    KeycloakAngularModule,
    MaterialModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 10000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true,
    }),
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: keycloakInitializer,
      multi: true,
      deps: [KeycloakService]
    },
    ErrorDialogService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlingInterceptor,
      multi: true,
      deps: [ErrorDialogService, Router]
    }
  ],
  exports: [
    ToastrModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
