import { createArrayFromDate } from 'src/app/utils/date-utils';
import { ReportsDTO, SourceReport } from './../../../models/reports.model';
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
  labels = [];
  datasets = [];

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
      startDate: (this.startDate != null) ? createArrayFromDate(this.startDate) : null,
      endDate: (this.endDate != null) ? createArrayFromDate(this.endDate) : null,
      showAll: false
    };

    this.getReports(request);
  }

  private getReports(request: ReportRequestDTO) {
    this.reportsService.getReports(request).subscribe((data: ReportsDTO) => {
      this.reports = data;
      this.initChart();
    }, error => {
      // TODO: dodati toastr
      console.log(error.error.message);
    });
  }

  onClickClear() {
    this.getAllReports();
    this.startDate = null;
    this.endDate = null;
  }

  initChart() {
    for (const agent of this.reports.reports) {
      const label = [];
      const logs = [];
      const alarms = [];
      for (const source of agent.sources) {
        label.push(source.source);
        logs.push(source.numOfLogs);
        alarms.push(source.numOfAlarms);
      }
      this.labels.push(label);
      this.datasets.push([
        { data: logs, label: 'Logs'},
        { data: alarms, label: 'Alarms'}
      ]);
    }

  }
}
