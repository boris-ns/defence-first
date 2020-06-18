export class Alarm {

    id: number;
    date: Date;
    reason: string;

    constructor(id: number, date: Date, reason: string) {
        this.id = id;
        this.date = date;
        this.reason = reason;
    }
}
