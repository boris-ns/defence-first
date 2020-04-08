import { NgModule } from '@angular/core';

import { MatButtonModule,
  MatMenuModule,
  MatIconModule,
  MatCardModule,
  MatSidenavModule,
  MatFormFieldModule,
  MatInputModule,
  MatTooltipModule,
  MatToolbarModule,
  MatSliderModule,
  MatGridListModule,
  MatExpansionModule,
  MatDatepickerModule,
  MatSelectModule,
  MatListModule,
  MatCheckboxModule,
  MatTabsModule,
  MatDialogModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatTableModule,
  MatTableDataSource
 } from '@angular/material';
import { CdkTableModule } from '@angular/cdk/table';

@NgModule({
  imports: [
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatCardModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatToolbarModule,
    MatSliderModule,
    MatGridListModule,
    MatExpansionModule,
    MatDatepickerModule,
    MatSelectModule,
    MatListModule,
    MatCheckboxModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatTableModule
  ],
  exports: [
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatCardModule,
    MatSidenavModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatToolbarModule,
    MatSliderModule,
    MatGridListModule,
    MatExpansionModule,
    MatDatepickerModule,
    MatSelectModule,
    MatListModule,
    MatCheckboxModule,
    MatTabsModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatTableModule
  ]
})
export class MaterialModule { }