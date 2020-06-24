import { Component, AfterViewInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ErrorDialogService } from './services/error-dialog-service/error-dialog.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {

  title = 'defence-first-web';

  constructor(
    private toastr: ToastrService
  ) {}

  ngAfterViewInit(): void {
    ErrorDialogService.get().subscribe(
      (value: string) => {
        this.toastr.error(value);
      }
    );
  }
}
