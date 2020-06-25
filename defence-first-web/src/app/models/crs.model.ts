export interface CSR {
    status: CSRStatus;
    serialNumber: string;
}

export enum CSRStatus {
    APPROVED = 'APPROVED', DECLINED = 'DECLINED', WAITING = 'WAITING', WAITING_RENEWAL = 'WAITING_RENEWAL'
}
