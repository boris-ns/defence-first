import { LogService } from './../../../services/siem-centar/log.service';
import { LogFilterDTO } from './../../../models/log-filter.model';
import { Component, OnInit } from '@angular/core';
import { Log } from 'src/app/models/log.model';
import { createArrayFromDate } from 'src/app/utils/date-utils';

@Component({
  selector: 'app-logs-search',
  templateUrl: './logs-search.component.html',
  styleUrls: ['./logs-search.component.css']
})
export class LogsSearchComponent implements OnInit {

  searchedLogs: Log[] = [];
  displayedColumns: string[] = ['id', 'type', 'date', 'source', 'message'];
  filter: LogFilterDTO = {};
  startDate: string;
  endDate: string;

  constructor(private logService: LogService) {
  }

  ngOnInit() {
  }

  onClickSearch() {
    if (this.startDate) {
      this.filter.startDate = createArrayFromDate(this.startDate);
    }

    if (this.endDate) {
      this.filter.endDate = createArrayFromDate(this.endDate);
    }

    if (this.startDate && this.endDate && (new Date(this.startDate).getTime() > (new Date(this.endDate).getTime()))) {
      console.log('Startdate je posle enddate');
      // @TODO: dodati toastr
      return;
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
    this.startDate = null;
    this.endDate = null;
  }
}
