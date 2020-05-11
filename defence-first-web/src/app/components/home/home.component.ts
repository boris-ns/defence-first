import { Component, OnInit } from '@angular/core';
import { PkiServiceService } from 'src/app/services/pki-service/pki-service.service';
import { AuthService } from 'src/app/services/auth.service';
import { SiemCentarService } from 'src/app/services/siem-centar/siem-centar.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  name: string;
  message: any;

  constructor(private authService: AuthService,
              private siemCentarService: SiemCentarService) {
  }

  ngOnInit() {
    this.name = this.authService.getUsername();
    this.siemCentarService.helloWorld().subscribe(
      data => {
        this.message = data;
      }
    );
  }
}
