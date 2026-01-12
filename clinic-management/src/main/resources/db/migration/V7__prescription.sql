create table if not exists prescriptions (
                                             id bigserial primary key,
                                             medical_record_id bigint not null,
                                             medication_name varchar(150) not null,
    dosage varchar(120) not null,
    duration_days int not null,
    instructions varchar(500),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_prescriptions_record foreign key (medical_record_id) references medical_records(id),
    constraint ck_prescriptions_duration check (duration_days > 0)
    );

create index if not exists ix_prescriptions_record on prescriptions (medical_record_id);
