alter table patients
    add column if not exists cnp varchar(32);

create unique index if not exists ux_patients_cnp on patients (cnp) where cnp is not null;
