# Business Requirements

BR1. Sistemul trebuie să permită înregistrarea și administrarea pacienților cu date de identificare și contact și să prevină dublurile pe baza unei chei unice (email și/sau CNP).

BR2. Sistemul trebuie să permită administrarea clinicilor și a medicilor, unde fiecare medic aparține unei clinici și are asociată o specializare.

BR3. Sistemul trebuie să permită definirea programului de lucru al medicilor (intervale orare zilnice) și să marcheze programul ca activ/inactiv.

BR4. Sistemul trebuie să permită calcularea și listarea sloturilor disponibile pentru un medic, pentru o dată și o durată specificată.

BR5. Sistemul trebuie să permită crearea unei programări pentru un pacient la un medic, într-un slot disponibil, la un interval orar valid.

BR6. Sistemul trebuie să valideze că o programare nu se poate crea dacă medicul are deja o programare suprapusă sau dacă intervalul este în afara programului de lucru.

BR7. Sistemul trebuie să permită anularea și finalizarea programărilor și să păstreze istoricul acestora prin statusuri (SCHEDULED / CANCELLED / COMPLETED).

BR8. Sistemul trebuie să permită crearea și administrarea fișelor medicale pentru pacienți, asociate unei programări și unui medic.

BR9. Sistemul trebuie să permită atașarea de prescripții la fișele medicale, incluzând medicament, dozaj și durată.

BR10. Sistemul trebuie să asigure securitatea și trasabilitatea: acces pe bază de roluri (ADMIN / DOCTOR / RECEPTIONIST), integritatea datelor prin dezactivare logică și auditarea tuturor acțiunilor relevante.

---

## MVP Features

### F1. Management pacienți
Acoperă BR1, BR10 (parțial).  
CRUD pacienți, validare unicitate (email/CNP), dezactivare logică în loc de ștergere.

### F2. Management clinici și medici
Acoperă BR2, BR10 (parțial).  
CRUD clinici și medici, asociere medic–clinică, specializare, activ/inactiv.

### F3. Program de lucru și sloturi disponibile
Acoperă BR3, BR4, BR6.  
Definire program de lucru pentru medici, calcul sloturi disponibile pe bază de dată și durată, excluderea intervalelor ocupate.

### F4. Management programări
Acoperă BR5, BR6, BR7.  
Creare programări, validare suprapuneri, anulare/finalizare, listare cu filtre (pacient, medic, interval).

### F5. Dosar medical, prescripții, securitate și audit
Acoperă BR8, BR9, BR10.  
Fișe medicale și prescripții, reguli de ownership pentru DOCTOR, securitate JWT cu roluri, audit log pentru acțiuni critice și interogare audit de către ADMIN.
