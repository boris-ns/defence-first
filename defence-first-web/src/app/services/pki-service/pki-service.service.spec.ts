import { TestBed } from '@angular/core/testing';

import { PkiServiceService } from './pki-service.service';

describe('PkiServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PkiServiceService = TestBed.get(PkiServiceService);
    expect(service).toBeTruthy();
  });
});
