# Top 10 Web Application Security Risks

https://owasp.org/www-project-top-ten/

1. Injection - SQL Injection je automatski rešen jer Hibernate interno koristi prepared statements. Isto važi i za NoSQL injection jer biblioteka za rad sa MongoDB-om to rešava. 

2. Broken Authentication - Za rukovanje sesijama i JWT tokenima je zadužen Keycloak. Sve što se tiče rada sa korisnicima i sesijama je konfigurisano u njemu.

3. Sensitive Data Exposure - Preko mreže se prenose samo neophodni podaci za rad aplikacije koja ih je zatražila. Prenos podataka je zaštićen HTTPS-om. Podaci koji se uvek nalaze u stanju mirovanja su zaštićeni adekvatnim podešavanjem prava pristupa različitih korisnika.

4. XML External Entities (XXE) - Projekat ne radi sa XML podacima.

5. Broken Access Control - Keycloak u sebi sadrži mogućnost dodavanja uloga korisnicima. U PKI servisu i SIEM centru nad svakim endpointom je podešeno koji tip korisnika može da im pristupi.

6. Security Misconfiguration - Sve konfiguracije se nalaze u application.properties fajlovima i konfiguracionim klasama.

7. Cross-Site Scripting XSS - Za razvoj klijentske aplikacije smo koristili Angular koji interno ima ugrađenu zaštitu od XSS napada. Spring Boot aplikacije takođe sadrže zaštitu od XSS napada u vidu slanja ```X-XSS-Protection``` headera browseru.

8. Insecure Deserialization - Uradjena je validacija DTO-ova na nivou backend aplikacija i u okviru formi na frontendu.

9. Using Components with Known Vulnerabilities - Koristili smo najnovije i stabilne verzije komponenti.

10. Insufficient Logging & Monitoring - Zadatak ovog projekta je da se bavi informisanjem administratora i operatora o logovima i dešavanjima u sistemu.
