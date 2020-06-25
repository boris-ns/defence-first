export interface Log {
    id: number;
    date: Date;
    logType: LogType;
    severity: number;
    message: string;
    source: string;
    agent: string;
}

export enum LogType {
    INFO, WARN, ERROR, SUCCESS, DEBUG, OTHER, SUCCESSFUL_LOGIN, UNSUCCESSFUL_LOGIN
}
