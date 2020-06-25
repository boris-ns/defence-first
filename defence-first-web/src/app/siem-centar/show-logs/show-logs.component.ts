import { LogFilterDTO } from '../../models/log-filter.model';
import { Log } from '../../models/log.model';
import { LogService } from '../../services/siem-centar/log.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-show-logs',
  templateUrl: './show-logs.component.html',
  styleUrls: ['./show-logs.component.css']
})
export class ShowLogsComponent implements OnInit {

  data: Log[];
  displayedColumns: string[] = ['id', 'type', 'sevetiy', 'date', 'source', 'agent', 'message'];

  serverUrl = 'https://localhost:8082/websockets';
  stompClient: any;

  constructor(private logService: LogService) {
  }

  ngOnInit() {
    this.getLogs();
  }

  private getLogs() {
    this.logService.getAllLogs().subscribe((data: Log[]) => {
      this.data = data;
    });
  }

  checkClass(row: any) {
    console.log(row);
    return '';
  }
}
