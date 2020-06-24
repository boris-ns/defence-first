import { Injectable} from '@angular/core';
import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpErrorResponse
} from '@angular/common/http';

import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorDialogData } from '../models/error-dialog-data';
import { ErrorDialogService } from '../services/error-dialog-service/error-dialog.service';

@Injectable()
export class ErrorHandlingInterceptor implements HttpInterceptor {

    constructor() {}

    handleError(error: HttpErrorResponse) {
        let errorData: ErrorDialogData = null;
        if (error.error instanceof ErrorEvent) {
            errorData = new ErrorDialogData('0', error.error.message);
        } else {
            ErrorDialogService.get().emit(error.message);
            errorData = new ErrorDialogData(error.status.toString(), error.message);
        }
        return throwError(errorData);
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
        .pipe(
            catchError(this.handleError)
        );
    }
}
