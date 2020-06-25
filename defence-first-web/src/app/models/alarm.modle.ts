export interface Alarm {

    id: number;
    date: Date;
    alarmType: AlarmType;
    reason: string;

}

export enum AlarmType {
    BRUTE_FORCE, DOS, OTHER
}
