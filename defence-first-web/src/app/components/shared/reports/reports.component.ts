import { createArrayFromDate } from 'src/app/utils/date-utils';
import { ReportsDTO } from './../../../models/reports.model';
import { ReportRequestDTO } from './../../../models/report-request.model';
import { ReportsService } from './../../../services/siem-centar/reports.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  reports: ReportsDTO = {reports: []};
  // displayedColumns: string[] = ['source', 'numOfLogs', 'numOfAlarms'];

  startDate: string;
  endDate: string;

  constructor(private reportsService: ReportsService) { 
  }

  ngOnInit() {
    this.getAllReports();    
  }

  private getAllReports() {
    const request: ReportRequestDTO = {
      startDate: [],
      endDate: [],
      showAll: true
    };
    
    this.getReports(request);
  }

  onClickSearch() {
    const request: ReportRequestDTO = {
      startDate: createArrayFromDate(this.startDate),
      endDate: createArrayFromDate(this.endDate),
      showAll: false
    };

    this.getReports(request);
  }

  private getReports(request: ReportRequestDTO) {
    this.reportsService.getReports(request).subscribe((data: ReportsDTO) => {
      this.reports = data;
    }, error => {
      // TODO: dodati toastr
      console.log(error);
    });
  }

  onClickClear() {
    this.getAllReports();
    this.startDate = null;
    this.endDate = null;
  }
}
