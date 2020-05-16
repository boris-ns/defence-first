import { LogService } from './../../../services/siem-centar/log.service';
import { LogFilterDTO } from './../../../models/log-filter.model';
import { Component, OnInit } from '@angular/core';
import { Log } from 'src/app/models/log.model';

@Component({
  selector: 'app-logs-search',
  templateUrl: './logs-search.component.html',
  styleUrls: ['./logs-search.component.css']
})
export class LogsSearchComponent implements OnInit {

  searchedLogs: Log[] = [];
  filter: LogFilterDTO = {};

  constructor(private logService: LogService) { 
  }

  ngOnInit() {
  }

  onClickSearch() {
    console.log(this.filter);
    
    this.logService.filterLogs(this.filter).subscribe((data: Log[]) => {
      this.searchedLogs = data;
    }, error => {
      // @TODO: dodati toastr
      console.log(error);
    });
  }

  onClickClear() {
    this.searchedLogs = [];
    this.filter = {};
  }
}
