import { TypeOccursMessageTemplate } from './../../../models/type-occurs-message-template.model';
import { TypeOccursTemplate } from './../../../models/type-occurs.model';
import { TypeMessageTemplate } from './../../../models/type-message-template.model';
import { RulesService } from './../../../services/siem-centar/rules.service';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-rules',
  templateUrl: './add-rules.component.html',
  styleUrls: ['./add-rules.component.css']
})
export class AddRulesComponent implements OnInit {

  file: File;

  typeMessageTemplate: TypeMessageTemplate = {type: 'INFO', messageRegex: '', alarmMessage: ''};
  typeOccursTemplate: TypeOccursTemplate = {type: 'INFO', occurs: 1, alarmMessage: '', time: 1};
  typeOccursMessageTemplate: TypeOccursMessageTemplate = {type: 'INFO', occurs: 1, alarmMessage: '', time: 1, messageRegex: ''};

  constructor(
    private ruleService: RulesService,
    private toastr: ToastrService) {
  }

  ngOnInit() {
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  onUpload() {
    this.ruleService.addRule(this.file).subscribe(data => {
        this.toastr.success('Added rule');
      }
    );
  }

  onClickCreateTypeMessageRule() {
    if (this.typeMessageTemplate.messageRegex === '' || this.typeMessageTemplate.alarmMessage === '') {
      this.toastr.warning('All fields must be filled');
      return;
    }

    this.ruleService.addTypeMessageRule(this.typeMessageTemplate).subscribe(data => {
      this.toastr.success('Rule successfully created!');
    }, error => {
      this.toastr.error(error.error.message);
    });
  }

  onClickCreateTypeOccursRule() {
    if (this.typeOccursTemplate.alarmMessage === '') {
      this.toastr.warning('All fields must be filled');
      return;
    }

    this.ruleService.addTypeOccursRule(this.typeOccursTemplate).subscribe(data => {
      this.toastr.success('Rule successfully created!');
    }, error => {
      this.toastr.error(error.error.message);
    });
  }

  onClickCreateTypeOccursMessageRule() {
    if (this.typeOccursMessageTemplate.messageRegex === '' || this.typeOccursMessageTemplate.alarmMessage === '') {
      this.toastr.warning('All fields must be filled');
      return;
    }

    this.ruleService.addTypeOccursMessageRule(this.typeOccursMessageTemplate).subscribe(data => {
      this.toastr.success('Rule successfully created!');
    }, error => {
      this.toastr.error(error.error.message);
    });
  }
}
