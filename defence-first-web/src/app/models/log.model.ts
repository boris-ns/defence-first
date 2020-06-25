export interface Log {
    id: number;
    date: Date;
    logType: LogType;
    sevetity: number;
    message: string;
    source: string;
    agent: string;
}

export enum LogType {
    INFO, WARN, ERROR, SUCCESS
}
