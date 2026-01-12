create table if not exists doctor_schedules (
                                                id bigserial primary key,
                                                doctor_id bigint not null,
                                                day_of_week smallint not null,
                                                start_time time not null,
                                                end_time time not null,
                                                active boolean not null default true,
                                                created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_doctor_schedules_doctor foreign key (doctor_id) references doctors(id),
    constraint ck_doctor_schedules_day check (day_of_week between 1 and 7),
    constraint ck_doctor_schedules_time check (start_time < end_time),
    constraint ux_doctor_schedules_doctor_day unique (doctor_id, day_of_week)
    );

create index if not exists ix_doctor_schedules_doctor on doctor_schedules (doctor_id);
