-- Vô hiệu hóa kiểm tra khóa ngoại để TRUNCATE bảng
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ từ các bảng theo thứ tự để tránh lỗi khóa ngoại
TRUNCATE TABLE `appointments`;
TRUNCATE TABLE `provider_schedules`;
TRUNCATE TABLE `services`;
TRUNCATE TABLE `provider_applications`;
TRUNCATE TABLE `providers`;
TRUNCATE TABLE `categories`;
TRUNCATE TABLE `users`;

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- Thêm dữ liệu vào bảng 'users'
INSERT INTO `users` (`id`, `display_name`, `email`, `password`, `role`, `phone_number`, `avatar_url`, `date_of_birth`, `gender`, `is_email_verified`, `address`, `created_at`, `updated_at`) VALUES
('0a9a8c1f-9a7d-4b8f-8c6e-1b3a1a3a1a3a', 'Admin Ryu', 'admin@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'ADMIN', '0987654321', '/uploads/blank.jpg', '1990-01-01', 'MALE', 1, '123 Admin St, Ba Dinh District, Hanoi', NOW(), NOW()),
('1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'John Customer', 'user1@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0123456789', '/uploads/blank.jpg', '1995-05-15', 'MALE', 1, '456 User Ave, Hoan Kiem District, Hanoi', NOW(), NOW()),
('2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'Alice User', 'user2@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0988888888', '/uploads/blank.jpg', '1998-11-20', 'FEMALE', 1, '789 Customer Blvd, Hai Ba Trung District, Hanoi', NOW(), NOW()),
('3d6c9d4c-6d4a-4e5c-7f3d-4e6d4d6d4d6d', 'Michael Provider', 'provider1@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0911111111', '/uploads/blank.jpg', '1988-08-08', 'MALE', 1, '101 Provider Rd, Dong Da District, Hanoi', NOW(), NOW()),
('4e7d0e5b-5e39-4f4b-6a2c-5f7e5e7e5e7e', 'Sarah Provider', 'provider2@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0922222222', '/uploads/blank.jpg', '1992-04-25', 'FEMALE', 1, '202 Service Ln, Cau Giay District, Hanoi', NOW(), NOW()),
('prov-user-003', 'Dr. Emily Carter', 'provider3@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0933333333', '/uploads/blank.jpg', '1985-01-10', 'FEMALE', 1, '303 Expert St, Hanoi', NOW(), NOW()),
('prov-user-004', 'David Chen', 'provider4@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0944444444', '/uploads/blank.jpg', '1991-07-19', 'MALE', 1, '404 Technology Rd, Hanoi', NOW(), NOW()),
('prov-user-005', 'Jessica Nguyen', 'provider5@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0955555555', '/uploads/blank.jpg', '1993-02-14', 'FEMALE', 1, '505 Art St, Hanoi', NOW(), NOW()),
('prov-user-006', 'Olivia Lee', 'provider6@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0966666666', '/uploads/blank.jpg', '1994-09-30', 'FEMALE', 1, '606 Beauty Ave, Hanoi', NOW(), NOW()),
('prov-user-007', 'Professor Ben', 'provider7@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0977777777', '/uploads/blank.jpg', '1980-12-12', 'MALE', 1, '707 Knowledge St, Hanoi', NOW(), NOW()),
('prov-user-008', 'Alex Green', 'provider8@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0988776655', '/uploads/blank.jpg', '2000-01-01', 'OTHER', 1, '808 Clean St, Hanoi', NOW(), NOW()),
('prov-user-009', 'Sophia Tran', 'provider9@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0912349876', '/uploads/blank.jpg', '1989-03-03', 'FEMALE', 1, '909 Relax Rd, Hanoi', NOW(), NOW()),
('prov-user-010', 'Daniel King', 'provider10@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0909090909', '/uploads/blank.jpg', '1990-10-10', 'MALE', 1, '1010 Fashion St, Hanoi', NOW(), NOW()),
('app-user-001', 'Chris Applicant', 'applicant1@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112221', NULL, '1996-01-01', 'MALE', 0, '1 A Street, Hanoi', NOW(), NOW()),
('app-user-002', 'Betty Applicant', 'applicant2@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112222', NULL, '1997-02-02', 'FEMALE', 1, '2 B Street, Hanoi', NOW(), NOW()),
('app-user-003', 'Charles Applicant', 'applicant3@example.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112223', NULL, '1998-03-03', 'MALE', 1, '3 C Street, Hanoi', NOW(), NOW());

-- Thêm dữ liệu vào bảng 'categories'
INSERT INTO `categories` (`id`, `name`, `description`, `is_active`, `created_at`, `updated_at`) VALUES
('cat-001', 'Men''s Haircut', 'Dịch vụ cắt, gội, sấy và tạo kiểu tóc cho nam giới.', 1, NOW(), NOW()),
('cat-002', 'Women''s Haircut', 'Dịch vụ cắt, gội, sấy và tạo kiểu tóc chuyên nghiệp cho nữ.', 1, NOW(), NOW()),
('cat-003', 'Facial Skincare', 'Các liệu trình chăm sóc da mặt từ cơ bản đến chuyên sâu.', 1, NOW(), NOW()),
('cat-004', 'Nail Services', 'Dịch vụ sơn, sửa, vẽ và đắp móng nghệ thuật.', 1, NOW(), NOW()),
('cat-005', 'Massage & Spa', 'Các liệu pháp massage thư giãn, trị liệu toàn thân.', 1, NOW(), NOW()),
('cat-006', 'Makeup', 'Trang điểm cá nhân, dự tiệc, cô dâu.', 1, NOW(), NOW()),
('cat-007', 'Mobile Repair', 'Dịch vụ sửa chữa, thay thế linh kiện điện thoại di động.', 1, NOW(), NOW()),
('cat-008', 'Psychological Counseling', 'Dịch vụ tư vấn, trò chuyện cùng chuyên gia tâm lý.', 1, NOW(), NOW()),
('cat-009', 'Home Tutoring', 'Cung cấp gia sư các môn học tại nhà cho học sinh.', 1, NOW(), NOW()),
('cat-010', 'Home Cleaning', 'Dịch vụ dọn dẹp vệ sinh nhà cửa theo giờ hoặc trọn gói.', 1, NOW(), NOW());

-- Thêm dữ liệu vào bảng 'providers'
INSERT INTO `providers` (`id`, `user_id`, `business_name`, `bio`, `address`, `phone_number`, `website`, `logo_url`, `banner_url`, `status`, `is_verified`, `created_at`, `updated_at`) VALUES
('prov-001', '3d6c9d4c-6d4a-4e5c-7f3d-4e6d4d6d4d6d', 'Gentlemen''s Cut', 'Leading men''s hairstylist with over 10 years of experience.', '101 Provider Rd, Dong Da District, Hanoi', '0911111111', 'https://gentlemenscut.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-002', '4e7d0e5b-5e39-4f4b-6a2c-5f7e5e7e5e7e', 'Serenity Spa', 'A place to find relaxation and natural beauty for women.', '202 Service Ln, Cau Giay District, Hanoi', '0922222222', 'https://serenityspa.vn', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-003', 'prov-user-003', 'Mindful Wellness Center', 'We listen and accompany you through life''s challenges.', '303 Expert St, Hanoi', '0933333333', 'https://mindfulwellness.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-004', 'prov-user-004', 'QuickFix Mobile Repair', 'Fast, reliable repairs with genuine parts.', '404 Technology Rd, Hanoi', '0944444444', 'https://quickfix.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-005', 'prov-user-005', 'The Nail Corner', 'Featuring the trendiest and safest nail designs.', '505 Art St, Hanoi', '0955555555', 'https://nailcorner.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-006', 'prov-user-006', 'Glamour Makeup Studio', 'Transforming you into the most beautiful and confident version of yourself.', '606 Beauty Ave, Hanoi', '0966666666', 'https://glamourstudio.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-007', 'prov-user-007', 'Elite Tutors Academy', 'A team of high-quality tutors from top universities.', '707 Knowledge St, Hanoi', '0977777777', 'https://elitetutors.edu.vn', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
('prov-008', 'prov-user-008', 'Green Clean Services', 'Bringing a fresh living space to your family.', '808 Clean St, Hanoi', '0988776655', 'https://greenclean.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 0, NOW(), NOW()),
('prov-009', 'prov-user-009', 'Peaceful Yoga Studio', 'Yoga classes to balance body, mind, and spirit.', '909 Relax Rd, Hanoi', '0912349876', 'https://peacefulyoga.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'SUSPENDED', 1, NOW(), NOW()),
('prov-010', 'prov-user-010', 'Prestige Barber House', 'A modern and classy chain of men''s hair salons.', '1010 Fashion St, Hanoi', '0909090909', 'https://prestigebarber.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW());

-- Thêm dữ liệu vào bảng 'services'
INSERT INTO `services` (`id`, `provider_id`, `category_id`, `service_name`, `description`, `image_url`, `duration_minutes`, `price`, `buffer_time_after`, `capacity`, `is_active`, `created_at`, `updated_at`) VALUES
('ser-001', 'prov-001', 'cat-001', 'Cắt tóc tạo kiểu nam', 'Cắt, gội, sấy và vuốt sáp tạo kiểu theo yêu cầu.', '/uploads/blank.jpg', 60, 100000.00, 10, 1, 1, NOW(), NOW()),
('ser-002', 'prov-001', 'cat-001', 'Uốn tóc nam', 'Tạo kiểu tóc xoăn bồng bềnh, lãng tử.', '/uploads/blank.jpg', 120, 400000.00, 15, 1, 1, NOW(), NOW()),
('ser-003', 'prov-002', 'cat-003', 'Chăm sóc da cơ bản', 'Làm sạch sâu, tẩy da chết, đắp mặt nạ dưỡng ẩm.', '/uploads/blank.jpg', 60, 250000.00, 10, 2, 1, NOW(), NOW()),
('ser-004', 'prov-002', 'cat-005', 'Massage body tinh dầu', 'Massage toàn thân với tinh dầu thiên nhiên giúp thư giãn.', '/uploads/blank.jpg', 90, 450000.00, 15, 1, 1, NOW(), NOW()),
('ser-005', 'prov-005', 'cat-004', 'Sơn gel Hàn Quốc', 'Sơn móng tay/chân bằng gel cao cấp, bền màu.', '/uploads/blank.jpg', 45, 150000.00, 5, 3, 1, NOW(), NOW()),
('ser-006', 'prov-004', 'cat-007', 'Thay màn hình iPhone', 'Thay thế màn hình iPhone các dòng, bảo hành 6 tháng.', '/uploads/blank.jpg', 30, 1200000.00, 0, 1, 1, NOW(), NOW()),
('ser-007', 'prov-003', 'cat-008', 'Tư vấn tâm lý 1:1', 'Phiên tư vấn riêng tư cùng chuyên gia tâm lý (60 phút).', '/uploads/blank.jpg', 60, 500000.00, 15, 1, 1, NOW(), NOW()),
('ser-008', 'prov-006', 'cat-006', 'Trang điểm dự tiệc', 'Gói trang điểm cá nhân đi tiệc, sự kiện.', '/uploads/blank.jpg', 75, 500000.00, 10, 1, 1, NOW(), NOW()),
('ser-009', 'prov-007', 'cat-009', 'Gia sư môn Toán lớp 9', 'Dạy kèm môn Toán cho học sinh lớp 9 (2 tiếng/buổi).', '/uploads/blank.jpg', 120, 300000.00, 0, 1, 1, NOW(), NOW()),
('ser-010', 'prov-008', 'cat-010', 'Dọn nhà theo giờ', 'Gói dọn dẹp cơ bản 3 tiếng cho căn hộ dưới 80m2.', '/uploads/blank.jpg', 180, 400000.00, 0, 5, 1, NOW(), NOW()),
('ser-011', 'prov-010', 'cat-001', 'Nhuộm màu thời trang', 'Nhuộm tóc nam với các màu hot trend nhất.', '/uploads/blank.jpg', 90, 600000.00, 15, 2, 1, NOW(), NOW()),
('ser-012', 'prov-002', 'cat-003', 'Liệu trình trị mụn chuyên sâu', 'Lấy nhân mụn, điện di tinh chất, chiếu ánh sáng sinh học.', '/uploads/blank.jpg', 90, 700000.00, 15, 1, 1, NOW(), NOW()),
('ser-013', 'prov-005', 'cat-004', 'Đắp móng bột', 'Tạo form móng và kéo dài móng bằng bột.', '/uploads/blank.jpg', 90, 350000.00, 10, 2, 1, NOW(), NOW()),
('ser-014', 'prov-004', 'cat-007', 'Thay pin điện thoại Samsung', 'Thay pin chính hãng cho các dòng máy Samsung Galaxy.', '/uploads/blank.jpg', 20, 450000.00, 5, 1, 1, NOW(), NOW()),
('ser-015', 'prov-010', 'cat-001', 'Combo Cắt + Uốn', 'Gói dịch vụ tiết kiệm bao gồm cắt và uốn tóc nam.', '/uploads/blank.jpg', 150, 850000.00, 15, 1, 0, NOW(), NOW());

-- Thêm dữ liệu vào bảng 'provider_schedules'
INSERT INTO `provider_schedules` (`id`, `provider_id`, `service_id`, `start_time`, `end_time`, `max_capacity`, `remaining_slots`, `status`, `notes`, `created_at`, `updated_at`) VALUES
('sch-001', 'prov-001', 'ser-001', DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 1 HOUR), 1, 1, 'AVAILABLE', 'Lịch trống buổi sáng', NOW(), NOW()),
('sch-002', 'prov-001', 'ser-001', DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 3 HOUR), 1, 0, 'BOOKED', NULL, NOW(), NOW()),
('sch-003', 'prov-002', 'ser-003', DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 1 HOUR), 2, 2, 'AVAILABLE', 'Còn 2 suất', NOW(), NOW()),
('sch-004', 'prov-002', 'ser-004', DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 3 HOUR), INTERVAL 30 MINUTE), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-005', 'prov-005', 'ser-005', DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 45 MINUTE), 3, 2, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-006', 'prov-004', 'ser-006', DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 1 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 1 HOUR), INTERVAL 30 MINUTE), 1, 1, 'AVAILABLE', 'Slot vàng', NOW(), NOW()),
('sch-007', 'prov-003', 'ser-007', DATE_ADD(NOW(), INTERVAL 4 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 1 HOUR), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-008', 'prov-008', 'ser-010', DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 5 DAY), INTERVAL 3 HOUR), 5, 4, 'AVAILABLE', 'Nhận dọn khu vực Cầu Giấy', NOW(), NOW()),
('sch-009', 'prov-010', 'ser-011', DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 5 HOUR), INTERVAL 30 MINUTE), 2, 2, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-010', 'prov-002', 'ser-012', DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 5 HOUR), INTERVAL 30 MINUTE), 1, 0, 'BOOKED', 'Đã có khách đặt', NOW(), NOW()),
('sch-011', 'prov-001', 'ser-002', DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 10 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 12 HOUR), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-012', 'prov-001', 'ser-001', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 23 HOUR), 1, 0, 'COMPLETED', 'Lịch đã hoàn thành', NOW(), NOW()),
('sch-013', 'prov-005', 'ser-005', DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 2 HOUR), INTERVAL 45 MINUTE), 3, 3, 'AVAILABLE', NULL, NOW(), NOW()),
('sch-014', 'prov-003', 'ser-007', DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 3 HOUR), 1, 0, 'BOOKED', NULL, NOW(), NOW()),
('sch-015', 'prov-004', 'ser-014', DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), INTERVAL 20 MINUTE), 1, 1, 'AVAILABLE', NULL, NOW(), NOW());

-- Thêm dữ liệu vào bảng 'appointments'
INSERT INTO `appointments` (`id`, `user_id`, `provider_schedule_id`, `status`, `notes_from_user`, `cancellation_reason`, `created_at`, `updated_at`) VALUES
('app-001', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'sch-002', 'CONFIRMED', 'Vui lòng gọi trước khi tôi đến 15 phút.', NULL, NOW(), NOW()),
('app-002', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'sch-010', 'CONFIRMED', 'Da mình hơi nhạy cảm, mong được tư vấn kỹ.', NULL, NOW(), NOW()),
('app-003', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'sch-005', 'SCHEDULED', 'Mình sẽ đi cùng 1 người bạn.', NULL, NOW(), NOW()),
('app-004', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'sch-014', 'SCHEDULED', NULL, NULL, NOW(), NOW()),
('app-005', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'sch-012', 'COMPLETED', 'Rất hài lòng với dịch vụ.', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('app-006', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'sch-001', 'SCHEDULED', 'Mình muốn cắt kiểu layer.', NULL, NOW(), NOW()),
('app-007', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'sch-003', 'SCHEDULED', NULL, NULL, NOW(), NOW()),
('app-008', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'sch-004', 'SCHEDULED', NULL, NULL, NOW(), NOW()),
('app-009', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'sch-006', 'CANCELLED', NULL, 'Có việc đột xuất.', NOW(), NOW()),
('app-010', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'sch-007', 'CONFIRMED', 'Đây là lần đầu mình sử dụng dịch vụ.', NULL, NOW(), NOW());

-- Thêm dữ liệu vào bảng 'provider_applications'
INSERT INTO `provider_applications` (`id`, `user_id`, `business_name`, `bio`, `address`, `phone_number`, `website`, `business_license_file_url`, `status`, `rejection_reason`, `submitted_at`, `reviewed_at`, `reviewed_by`, `created_at`, `updated_at`) VALUES
('appreg-001', 'app-user-001', 'A Photography', 'Professional photography services.', '1 A Street, Hanoi', '0901112221', 'https://aphotography.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
('appreg-002', 'app-user-002', 'B Home Bakery', 'Homemade sweets, delivered to your door.', '2 B Street, Hanoi', '0901112222', 'https://bhomebakery.com', '/uploads/blank.jpg', 'APPROVED', NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), '0a9a8c1f-9a7d-4b8f-8c6e-1b3a1a3a1a3a', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('appreg-003', 'app-user-003', 'C Fitness Coach', 'Personal trainer at the gym or at home.', '3 C Street, Hanoi', '0901112223', 'https://cfitness.com', '/uploads/blank.jpg', 'REJECTED', 'Profile lacks sufficient information about professional qualifications.', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), '0a9a8c1f-9a7d-4b8f-8c6e-1b3a1a3a1a3a', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
('appreg-004', '1b4a7b2e-8b6c-4c7e-9d5f-2c4b2b4b2b4b', 'John''s Translation', 'English-Vietnamese document translation.', '456 User Ave, Hoan Kiem District, Hanoi', '0123456789', NULL, NULL, 'CANCELLED', NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
('appreg-005', 'app-user-001', 'A Photography 2nd App', 'Re-registering photography service.', '1 A Street, Hanoi', '0901112221', 'https://aphotography.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
('appreg-006', 'app-user-002', 'B Home Bakery Update', 'Updating business information.', '2B B Street, Hanoi', '0901112222', 'https://bhomebakery.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
('appreg-007', 'app-user-003', 'C Fitness Re-apply', 'Re-submitting fitness coach application.', '3 C Street, Hanoi', '0901112223', 'https://cfitness.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
('appreg-008', '2c5b8c3d-7c5b-4d6d-8e4e-3d5c3c5c3c5c', 'Alice''s Graphic Design', 'Logo and banner design services.', '789 Customer Blvd, Hai Ba Trung District, Hanoi', '0988888888', 'https://alicedesign.com', '/uploads/blank.jpg', 'APPROVED', NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), '0a9a8c1f-9a7d-4b8f-8c6e-1b3a1a3a1a3a', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
('appreg-009', 'app-user-001', 'A Car Wash', 'At-home car wash service.', '1 A Street, Hanoi', '0901112221', NULL, NULL, 'REJECTED', 'Business model not suitable.', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), '0a9a8c1f-9a7d-4b8f-8c6e-1b3a1a3a1a3a', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('appreg-010', 'app-user-002', 'Pet Sitting Service', 'Pet sitting service.', '2 B Street, Hanoi', '0901112222', 'https://bpet.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW());