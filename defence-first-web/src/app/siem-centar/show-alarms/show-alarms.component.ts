import { Component, OnInit } from '@angular/core';
import { AlarmService } from 'src/app/services/siem-centar/alarm.service';
import { Alarm } from 'src/app/models/alarm.modle';

@Component({
  selector: 'app-show-alarms',
  templateUrl: './show-alarms.component.html',
  styleUrls: ['./show-alarms.component.css']
})
export class ShowAlarmsComponent implements OnInit {

  data: Alarm[];
  displayedColumns: string[] = ['id', 'date', 'reason'];

  constructor(private alarmService: AlarmService) {
  }

  ngOnInit() {
    this.getLogs();
  }

  private getLogs() {
    this.alarmService.getAllAlarms().subscribe(
      (data: Alarm[]) => {
        data.sort((a: Alarm, b: Alarm) => b.id - a.id);
        this.data = data;
      }
    );
  }

  checkClass(row: any) {
    console.log(row);
    return '';
  }

}
