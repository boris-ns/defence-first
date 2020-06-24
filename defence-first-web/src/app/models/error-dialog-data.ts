export class ErrorDialogData {
    public status: string;
    public message: string;

    constructor(status?: string, message?: string) {
        this.message = message;
        this.status = status;
    }

}
