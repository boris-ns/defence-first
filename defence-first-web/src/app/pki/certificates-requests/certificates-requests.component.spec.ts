import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificatesRequestsComponent } from './certificates-requests.component';

describe('CertificatesRequestsComponent', () => {
  let component: CertificatesRequestsComponent;
  let fixture: ComponentFixture<CertificatesRequestsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CertificatesRequestsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CertificatesRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
