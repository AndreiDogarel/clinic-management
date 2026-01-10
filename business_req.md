# Business Requirements

BR1. Sistemul trebuie să permită înregistrarea pacienților cu date de identificare și contact și să prevină dublurile pe baza unei chei unice (ex: email sau CNP).

BR2. Sistemul trebuie să permită administrarea medicilor cu specializare și program de lucru, iar fiecare medic aparține unei clinici.

BR3. Sistemul trebuie să permită crearea unei programări pentru un pacient la un medic la un interval orar definit.

BR4. Sistemul trebuie să valideze că o programare nu se poate crea dacă medicul are deja o programare în același interval sau dacă intervalul este în afara programului medicului.

BR5. Sistemul trebuie să permită anularea unei programări și să păstreze istoricul (status: SCHEDULED/CANCELLED/COMPLETED).

BR6. Sistemul trebuie să permită listarea programărilor filtrate după pacient, medic și interval de timp (de la/până la).

BR7. Sistemul trebuie să permită crearea și actualizarea fișelor medicale pentru un pacient, asociate unei consultații/programări.

BR8. Sistemul trebuie să permită atașarea de prescripții la o fișă medicală, incluzând medicament, dozaj și durată.

BR9. Sistemul trebuie să asigure integritatea datelor: nu se pot șterge entități dacă există legături critice (ex: nu ștergi un pacient cu programări/fișe existente; se folosește “dezactivare” sau validare).

BR10. Sistemul trebuie să expună documentație completă a API-urilor prin Swagger/OpenAPI și să fie demonstrabil prin colecție Postman sau UI.
