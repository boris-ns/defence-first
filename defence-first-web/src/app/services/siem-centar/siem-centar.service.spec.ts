import { TestBed } from '@angular/core/testing';

import { SiemCentarService } from './siem-centar.service';

describe('SiemCentarService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SiemCentarService = TestBed.get(SiemCentarService);
    expect(service).toBeTruthy();
  });
});
