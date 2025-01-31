import { LogService } from '../../services/siem-centar/log.service';
import { LogFilterDTO } from '../../models/log-filter.model';
import { Component, OnInit } from '@angular/core';
import { Log } from 'src/app/models/log.model';
import { createArrayFromDate } from 'src/app/utils/date-utils';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-logs-search',
  templateUrl: './logs-search.component.html',
  styleUrls: ['./logs-search.component.css']
})
export class LogsSearchComponent implements OnInit {

  searchedLogs: Log[] = [];
  displayedColumns: string[] = ['id', 'type', 'severity', 'date', 'source', 'agent', 'message'];
  filter: LogFilterDTO = {};
  startDate: string;
  endDate: string;

  constructor(
    private logService: LogService,
    private toastr: ToastrService) {
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
      this.toastr.error('Startdate is after enddate');
      return;
    }

    this.logService.filterLogs(this.filter).subscribe((data: Log[]) => {
      this.searchedLogs = data;
    }
    );
  }

  onClickClear() {
    this.searchedLogs = [];
    this.filter = {};
    this.startDate = null;
    this.endDate = null;
  }
}
