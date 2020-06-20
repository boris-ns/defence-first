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

  constructor(private reportsService: ReportsService) { 
  }

  ngOnInit() {
    this.getReports();    
  }

  private getReports() {
    const request: ReportRequestDTO = {
      startDateTime: 1592593077715,
      endDateTime: 1592593077715
    };
    
    this.reportsService.getReports(request).subscribe((data: ReportsDTO) => {
      this.reports = data;
    }, error => {
      // TODO: dodati toastr
      console.log(error);
    });
  }
}
