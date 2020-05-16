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
  startDate: string;
  endDate: string;

  constructor(private logService: LogService) { 
  }

  ngOnInit() {
  }

  onClickSearch() {
    console.log(this.filter);

    if (this.startDate) {
      const startDate = new Date(this.startDate);
      this.filter.startDate = [
        startDate.getUTCDate() + 1, 
        startDate.getUTCMonth() + 1, 
        startDate.getFullYear()
      ];
    }

    if (this.endDate) {
      const endDate = new Date(this.endDate);
      this.filter.endDate = [
        endDate.getUTCDate() + 1,
        endDate.getUTCMonth() + 1,
        endDate.getFullYear()
      ];
    }

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
