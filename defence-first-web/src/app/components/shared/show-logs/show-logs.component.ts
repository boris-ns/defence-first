import { Log } from './../../../models/log.model';
import { LogService } from './../../../services/siem-centar/log.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-show-logs',
  templateUrl: './show-logs.component.html',
  styleUrls: ['./show-logs.component.css']
})
export class ShowLogsComponent implements OnInit {

  logs: Log[] = [];

  constructor(private logService: LogService) { 
  }

  ngOnInit() {
    this.getLogs();
  }

  private getLogs() {
    this.logService.getAllLogs().subscribe((data: Log[]) => {
      this.logs = data;
    }, error => {
      // @TODO: dodati toastr
      console.log(error);
    });
  }
}
