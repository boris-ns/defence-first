export class CertificateCreate {

    subjectName: Name;
    issuerAlias: string;

    constructor(subjectName: Name, issuerAlias: string) {
        this.subjectName = subjectName;
        this.issuerAlias = issuerAlias;
    }
}

export class Name {
    c: string;
    st: string;
    l: string;
    o: string;
    ou: string;
    cn: string;

    constructor(c: string, st: string, l: string, o: string, ou: string, cn: string) {
        this.c = c;
        this.st = st;
        this.l = l;
        this.o = o;
        this.ou = ou;
        this.cn = cn;
    }
}
