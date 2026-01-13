create table if not exists audit_logs (
                                          id bigserial primary key,
                                          actor_email varchar(255),
    actor_roles varchar(255),
    action varchar(60) not null,
    entity_type varchar(60) not null,
    entity_id bigint,
    details text,
    created_at timestamptz not null default now()
    );

create index if not exists ix_audit_logs_entity on audit_logs (entity_type, entity_id);
create index if not exists ix_audit_logs_created_at on audit_logs (created_at);
create index if not exists ix_audit_logs_action on audit_logs (action);
