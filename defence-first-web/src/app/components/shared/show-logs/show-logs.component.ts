import { LogFilterDTO } from './../../../models/log-filter.model';
import { Log } from './../../../models/log.model';
import { LogService } from './../../../services/siem-centar/log.service';
import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-show-logs',
  templateUrl: './show-logs.component.html',
  styleUrls: ['./show-logs.component.css']
})
export class ShowLogsComponent implements OnInit {

  data: Log[];
  displayedColumns: string[] = ['id', 'type', 'date', 'source', 'agent', 'message'];

  serverUrl = 'https://localhost:8082/websockets';
  stompClient: any;

  constructor(private logService: LogService) {
    this.initializeWebSocketConnection();
  }

  ngOnInit() {
    this.getLogs();
  }

  private getLogs() {
    this.logService.getAllLogs().subscribe((data: Log[]) => {
      this.data = data;
    }, error => {
      // @TODO: dodati toastr
      console.log(error);
    });
  }

  initializeWebSocketConnection() {
    // const ws = new SockJS(this.serverUrl);
    // this.stompClient = Stomp.over(ws);
    // const that = this;
    // this.stompClient.connect({}, () => {
    //   that.stompClient.subscribe('/topic', (message) => {
    //     console.log(message);
    //     if (message.body) {
    //       console.log(message.body);
    //     }
    //   });
    // });
  }

  checkClass(row: any) {
    console.log(row);
    return '';
  }
}
