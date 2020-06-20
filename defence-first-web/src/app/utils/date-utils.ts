export function createArrayFromDate(date: string): number[] {
    const dateObj = new Date(date);
    return [
        dateObj.getUTCDate() + 1,
        dateObj.getUTCMonth() + 1,
        dateObj.getFullYear()
    ];
}
