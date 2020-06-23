import { RulesService } from './../../../services/siem-centar/rules.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-rules',
  templateUrl: './add-rules.component.html',
  styleUrls: ['./add-rules.component.css']
})
export class AddRulesComponent implements OnInit {

  file: File;

  constructor(private ruleService: RulesService) { 
  }

  ngOnInit() {
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  onUpload() {
    this.ruleService.addRule(this.file).subscribe(data => {
        console.log("added rule");
        // TODO: dodati toastr
      }, error => {
        // TODO: dodati toastr
        console.log(error.message);
      }
    );
  }
}
