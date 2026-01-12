create table if not exists doctor_schedule_breaks (
                                                      id bigserial primary key,
                                                      schedule_id bigint not null,
                                                      start_time time not null,
                                                      end_time time not null,
                                                      note varchar(120),
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint fk_doctor_breaks_schedule foreign key (schedule_id) references doctor_schedules(id) on delete cascade,
    constraint ck_doctor_breaks_time check (start_time < end_time)
    );

create index if not exists ix_doctor_breaks_schedule on doctor_schedule_breaks (schedule_id);
