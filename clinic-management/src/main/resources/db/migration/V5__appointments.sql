create table if not exists appointments (
                                            id bigserial primary key,
                                            patient_id bigint not null,
                                            doctor_id bigint not null,
                                            start_time timestamptz not null,
                                            end_time timestamptz not null,
                                            status varchar(20) not null,
    reason varchar(255),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_appointments_patient foreign key (patient_id) references patients(id),
    constraint fk_appointments_doctor foreign key (doctor_id) references doctors(id),
    constraint ck_appointments_time check (start_time < end_time)
    );

create index if not exists ix_appointments_doctor_time on appointments (doctor_id, start_time, end_time);
create index if not exists ix_appointments_patient_time on appointments (patient_id, start_time);
create index if not exists ix_appointments_status on appointments (status);
