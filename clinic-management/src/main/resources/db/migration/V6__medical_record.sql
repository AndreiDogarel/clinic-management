create table if not exists medical_records (
                                               id bigserial primary key,
                                               patient_id bigint not null,
                                               doctor_id bigint not null,
                                               appointment_id bigint unique,
                                               diagnosis varchar(255) not null,
    notes text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_medical_records_patient foreign key (patient_id) references patients(id),
    constraint fk_medical_records_doctor foreign key (doctor_id) references doctors(id),
    constraint fk_medical_records_appointment foreign key (appointment_id) references appointments(id)
    );

create index if not exists ix_medical_records_patient on medical_records (patient_id);
create index if not exists ix_medical_records_doctor on medical_records (doctor_id);
