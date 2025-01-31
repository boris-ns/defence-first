import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service/pki-service.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-crs',
  templateUrl: './crs.component.html',
  styleUrls: ['./crs.component.css']
})
export class CrsComponent implements OnInit {

  file: File;
  renewalFile: File;

  constructor(
    private pkiService: PkiServiceService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  onUpload() {
    if (this.file === undefined) {
      this.toastr.warning('Choose file');
    } else {
      this.pkiService.sendCSR(this.file).subscribe(
        () => {
          this.toastr.success('Sucessful sent');
        }
      );
    }
  }

  onFileSelected2(event: any) {
    this.renewalFile = event.target.files[0];
  }

  onUpload2() {
    if (this.renewalFile === undefined) {
      this.toastr.warning('Choose file');
    } else {
      this.pkiService.renewalCSR(this.renewalFile).subscribe(
        () => {
          this.toastr.success('Sucessful sent');
        }
      );
    }
  }

}
