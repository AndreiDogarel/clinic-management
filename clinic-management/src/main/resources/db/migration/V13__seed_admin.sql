insert into users (email, password_hash, active)
values ('admin@clinic.com', '$2a$12$8fLkB/2.SzyMKxkYkds/duAYUCMI8QVJ2ObQTHoiEgqSAnvZP5CTe', true)
    on conflict (lower(email)) do nothing;

insert into user_roles (user_id, role)
select u.id, 'ADMIN'
from users u
where lower(u.email) = lower('admin@clinic.com')
    on conflict do nothing;
