INSERT INTO users (id, display_name, email, password, role, phone_number, avatar_url, date_of_birth, gender, is_email_verified, address, created_at, updated_at) VALUES
('admin-001', 'Alice Johnson', 'alice.johnson@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'ADMIN', '0901234589', NULL, '1992-04-15', 'FEMALE', true, 'Hanoi, Vietnam', NOW(), NOW()),
('admin-002', 'Michael Smith', 'michael.smith@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'ADMIN', '0901234590', NULL, '1988-09-21', 'MALE', true, 'Ho Chi Minh City, Vietnam', NOW(), NOW()),
('admin-003', 'Sophia Brown', 'sophia.brown@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'ADMIN', '0901234591', NULL, '1995-11-02', 'FEMALE', false, 'Hanoi, Vietnam', NOW(), NOW()),
('admin-004', 'Daniel Miller', 'daniel.miller@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'ADMIN', '0901234592', NULL, '1990-01-28', 'MALE', true, 'Ho Chi Minh City, Vietnam', NOW(), NOW()),
('admin-005', 'Emily Davis', 'emily.davis@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'ADMIN', '0901234593', NULL, '1993-06-12', 'FEMALE', true, 'Hanoi, Vietnam', NOW(), NOW()),

('provider-001', 'William Wilson', 'william.wilson@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'PROVIDER', '0901234594', NULL, '1987-08-19', 'MALE', false, 'District 1, HCMC', NOW(), NOW()),
('provider-002', 'Olivia Moore', 'olivia.moore@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'PROVIDER', '0901234595', NULL, '1994-02-07', 'FEMALE', true, 'District 2, HCMC', NOW(), NOW()),
('provider-003', 'James Taylor', 'james.taylor@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'PROVIDER', '0901234596', NULL, '1989-12-03', 'MALE', true, 'District 3, HCMC', NOW(), NOW()),
('provider-004', 'Isabella Anderson', 'isabella.anderson@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'PROVIDER', '0901234597', NULL, '1996-07-16', 'FEMALE', false, 'District 4, HCMC', NOW(), NOW()),
('provider-005', 'Benjamin Thomas', 'benjamin.thomas@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'PROVIDER', '0901234598', NULL, '1986-05-11', 'MALE', true, 'District 5, HCMC', NOW(), NOW()),

('user-001', 'Grace Lee', 'grace.lee@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234599', NULL, '1992-10-20', 'FEMALE', true, 'District 6, HCMC', NOW(), NOW()),
('user-002', 'Henry Martin', 'henry.martin@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234600', NULL, '1989-03-14', 'MALE', false, 'District 7, HCMC', NOW(), NOW()),
('user-003', 'Chloe White', 'chloe.white@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234601', NULL, '1994-07-05', 'FEMALE', true, 'District 8, HCMC', NOW(), NOW()),
('user-004', 'Matthew Harris', 'matthew.harris@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234602', NULL, '1991-12-09', 'MALE', true, 'District 9, HCMC', NOW(), NOW()),
('user-005', 'Lily Clark', 'lily.clark@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234603', NULL, '1995-08-17', 'FEMALE', false, 'District 10, HCMC', NOW(), NOW()),
('user-006', 'Christopher Hall', 'christopher.hall@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234604', NULL, '1987-11-22', 'MALE', true, 'District 11, HCMC', NOW(), NOW()),
('user-007', 'Zoe King', 'zoe.king@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234605', NULL, '1993-01-30', 'FEMALE', true, 'District 12, HCMC', NOW(), NOW()),
('user-008', 'Ethan Scott', 'ethan.scott@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234606', NULL, '1988-06-27', 'MALE', false, 'Binh Thanh District, HCMC', NOW(), NOW()),
('user-009', 'Amelia Young', 'amelia.young@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234607', NULL, '1996-09-13', 'FEMALE', true, 'Go Vap District, HCMC', NOW(), NOW()),
('user-010', 'Jack Turner', 'jack.turner@vietlong.com', '$2a$10$DjINL/b/898DpXclQzbQ4O9zCM.kfCGmWGmlyQaOmTQJnC1eioFHO', 'USER', '0901234608', NULL, '1990-05-06', 'MALE', true, 'Tan Binh District, HCMC', NOW(), NOW());
