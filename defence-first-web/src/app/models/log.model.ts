export interface Log {
    id: number;
    date: string;
    logType: LogType;
    message: string;
    source: string;
}

export enum LogType {
    INFO, WARN, ERROR, SUCCESS
}
