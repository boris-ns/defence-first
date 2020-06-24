import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class ErrorDialogService {

  private static emitter: EventEmitter<any>;
  static get(): EventEmitter<any> {
    if (!this.emitter) {
      this.emitter = new EventEmitter();
    }
    return this.emitter;
  }
}
