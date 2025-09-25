SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `appointments`;
TRUNCATE TABLE `provider_schedules`;
TRUNCATE TABLE `services`;
TRUNCATE TABLE `provider_applications`;
TRUNCATE TABLE `providers`;
TRUNCATE TABLE `categories`;
TRUNCATE TABLE `users`;
SELECT @admin_id := UUID(), @user1_id := UUID(), @user2_id := UUID();
SELECT @prov_user1_id := UUID(), @prov_user2_id := UUID(), @prov_user3_id := UUID();
SELECT @prov_user4_id := UUID(), @prov_user5_id := UUID(), @prov_user6_id := UUID();
SELECT @prov_user7_id := UUID(), @prov_user8_id := UUID(), @prov_user9_id := UUID();
SELECT @prov_user10_id := UUID();
SELECT @app_user1_id := UUID(), @app_user2_id := UUID(), @app_user3_id := UUID();
SELECT @cat1_id := UUID(), @cat2_id := UUID(), @cat3_id := UUID(), @cat4_id := UUID(), @cat5_id := UUID();
SELECT @cat6_id := UUID(), @cat7_id := UUID(), @cat8_id := UUID(), @cat9_id := UUID(), @cat10_id := UUID();
INSERT INTO `users` (`id`, `display_name`, `email`, `password`, `role`, `phone_number`, `avatar_url`, `date_of_birth`, `gender`, `is_email_verified`, `address`, `created_at`, `updated_at`) VALUES
(@admin_id, 'Trần Minh Trí', 'tranminhtri@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'ADMIN', '0987654321', '/uploads/blank.jpg', '1990-01-01', 'MALE', 1, '123 Admin St, Ba Dinh District, Hanoi', NOW(), NOW()),
(@user1_id, 'Lê Văn Hùng', 'levanhung@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0123456788', '/uploads/blank.jpg', '1995-05-15', 'MALE', 1, '456 User Ave, Hoan Kiem District, Hanoi', NOW(), NOW()),
(@user2_id, 'Nguyễn Thị Lan', 'nguyenthilan@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0988888888', '/uploads/blank.jpg', '1998-11-20', 'FEMALE', 1, '789 Customer Blvd, Hai Ba Trung District, Hanoi', NOW(), NOW()),
(@prov_user1_id, 'Phạm Minh Tuấn', 'phamminhtuan@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0911111111', '/uploads/blank.jpg', '1988-08-08', 'MALE', 1, '101 Provider Rd, Dong Da District, Hanoi', NOW(), NOW()),
(@prov_user2_id, 'Lê Thị Hương', 'lethihuong@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0922222222', '/uploads/blank.jpg', '1992-04-25', 'FEMALE', 1, '202 Service Ln, Cau Giay District, Hanoi', NOW(), NOW()),
(@prov_user3_id, 'Đỗ Thị Mai', 'dothimai@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0933333333', '/uploads/blank.jpg', '1985-01-10', 'FEMALE', 1, '303 Expert St, Hanoi', NOW(), NOW()),
(@prov_user4_id, 'Nguyễn Văn Hoàng', 'nguyenvanhoang@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0944444444', '/uploads/blank.jpg', '1991-07-19', 'MALE', 1, '404 Technology Rd, Hanoi', NOW(), NOW()),
(@prov_user5_id, 'Trần Thị Phương', 'tranthiphuong@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0955555555', '/uploads/blank.jpg', '1993-02-14', 'FEMALE', 1, '505 Art St, Hanoi', NOW(), NOW()),
(@prov_user6_id, 'Lê Thị Ngọc', 'lethingoc@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0966666666', '/uploads/blank.jpg', '1994-09-30', 'FEMALE', 1, '606 Beauty Ave, Hanoi', NOW(), NOW()),
(@prov_user7_id, 'Phạm Văn Dũng', 'phamvandung@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0977777777', '/uploads/blank.jpg', '1980-12-12', 'MALE', 1, '707 Knowledge St, Hanoi', NOW(), NOW()),
(@prov_user8_id, 'Công Ty Vệ Sinh', 'congtyvesinh@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0988776655', '/uploads/blank.jpg', '2000-01-01', 'OTHER', 1, '808 Clean St, Hanoi', NOW(), NOW()),
(@prov_user9_id, 'Nguyễn Thị Hạnh', 'nguyenthihanh@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0912349876', '/uploads/blank.jpg', '1989-03-03', 'FEMALE', 1, '909 Relax Rd, Hanoi', NOW(), NOW()),
(@prov_user10_id, 'Phan Văn Nam', 'phanvannam@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'PROVIDER', '0909090909', '/uploads/blank.jpg', '1990-10-10', 'MALE', 1, '1010 Fashion St, Hanoi', NOW(), NOW()),
(@app_user1_id, 'Vũ Quang Huy', 'vuquanghuy@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112221', NULL, '1996-01-01', 'MALE', 0, '1 A Street, Hanoi', NOW(), NOW()),
(@app_user2_id, 'Trần Thị Thanh', 'tranthithanh@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112222', NULL, '1997-02-02', 'FEMALE', 1, '2 B Street, Hanoi', NOW(), NOW()),
(@app_user3_id, 'Phạm Văn Cường', 'phamvancuong@bookme.com', '$2a$10$mH1VHlavk1U9WLf5c74dLuWZRjwVUilfeMu.WGzjMnYkg7uxgr//2', 'USER', '0901112223', NULL, '1998-03-03', 'MALE', 1, '3 C Street, Hanoi', NOW(), NOW());
INSERT INTO `categories` (`id`, `name`, `description`, `is_active`, `created_at`, `updated_at`) VALUES
(@cat1_id, 'Cắt Tóc Nam', 'Dịch vụ cắt, gội, sấy và tạo kiểu tóc cho nam giới.', 1, NOW(), NOW()),
(@cat2_id, 'Cắt Tóc Nữ', 'Dịch vụ cắt, gội, sấy và tạo kiểu tóc chuyên nghiệp cho nữ.', 1, NOW(), NOW()),
(@cat3_id, 'Chăm Sóc Da Mặt', 'Các liệu trình chăm sóc da mặt từ cơ bản đến chuyên sâu.', 1, NOW(), NOW()),
(@cat4_id, 'Dịch Vụ Móng', 'Dịch vụ sơn, sửa, vẽ và đắp móng nghệ thuật.', 1, NOW(), NOW()),
(@cat5_id, 'Massage & Spa', 'Các liệu pháp massage thư giãn, trị liệu toàn thân.', 1, NOW(), NOW()),
(@cat6_id, 'Trang Điểm', 'Trang điểm cá nhân, dự tiệc, cô dâu.', 1, NOW(), NOW()),
(@cat7_id, 'Sửa Chữa Điện Thoại', 'Dịch vụ sửa chữa, thay thế linh kiện điện thoại di động.', 1, NOW(), NOW()),
(@cat8_id, 'Tư Vấn Tâm Lý', 'Dịch vụ tư vấn, trò chuyện cùng chuyên gia tâm lý.', 1, NOW(), NOW()),
(@cat9_id, 'Gia Sư Tại Nhà', 'Cung cấp gia sư các môn học tại nhà cho học sinh.', 1, NOW(), NOW()),
(@cat10_id, 'Dọn Dẹp Nhà Cửa', 'Dịch vụ dọn dẹp vệ sinh nhà cửa theo giờ hoặc trọn gói.', 1, NOW(), NOW());
SELECT @prov1_id := UUID(), @prov2_id := UUID(), @prov3_id := UUID(), @prov4_id := UUID(), @prov5_id := UUID();
SELECT @prov6_id := UUID(), @prov7_id := UUID(), @prov8_id := UUID(), @prov9_id := UUID(), @prov10_id := UUID();
SELECT @appreg1_id := UUID(), @appreg2_id := UUID(), @appreg3_id := UUID(), @appreg4_id := UUID();
SELECT @appreg5_id := UUID(), @appreg6_id := UUID(), @appreg7_id := UUID(), @appreg8_id := UUID();
SELECT @appreg9_id := UUID(), @appreg10_id := UUID();
INSERT INTO `providers` (`id`, `user_id`, `business_name`, `bio`, `address`, `phone_number`, `website`, `logo_url`, `banner_url`, `status`, `is_verified`, `created_at`, `updated_at`) VALUES
(@prov1_id, @prov_user1_id, 'Salon Quý Ông', 'Nhà tạo mẫu tóc nam hàng đầu với hơn 10 năm kinh nghiệm.', '101 Provider Rd, Dong Da District, Hanoi', '0911111111', 'https://gentlemenscut.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov2_id, @prov_user2_id, 'Spa Tinh Tế', 'Nơi tìm thấy sự thư giãn và vẻ đẹp tự nhiên cho phụ nữ.', '202 Service Ln, Cau Giay District, Hanoi', '0922222222', 'https://serenityspa.vn', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov3_id, @prov_user3_id, 'Trung Tâm Sống Khỏe', 'Chúng tôi lắng nghe và đồng hành cùng bạn vượt qua thử thách.', '303 Expert St, Hanoi', '0933333333', 'https://mindfulwellness.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov4_id, @prov_user4_id, 'Cửa Hàng Sửa Điện Thoại Nhanh', 'Sửa chữa nhanh chóng, uy tín với linh kiện chính hãng.', '404 Technology Rd, Hanoi', '0944444444', 'https://quickfix.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov5_id, @prov_user5_id, 'Góc Làm Móng', 'Nổi bật với các thiết kế móng thời thượng và an toàn nhất.', '505 Art St, Hanoi', '0955555555', 'https://nailcorner.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov6_id, @prov_user6_id, 'Studio Trang Điểm Lộng Lẫy', 'Biến bạn thành phiên bản xinh đẹp và tự tin nhất.', '606 Beauty Ave, Hanoi', '0966666666', 'https://glamourstudio.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov7_id, @prov_user7_id, 'Học Viện Gia Sư Ưu Tú', 'Đội ngũ gia sư chất lượng cao từ các trường đại học top.', '707 Knowledge St, Hanoi', '0977777777', 'https://elitetutors.edu.vn', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW()),
(@prov8_id, @prov_user8_id, 'Dịch Vụ Vệ Sinh Xanh', 'Mang lại không gian sống trong lành cho gia đình bạn.', '808 Clean St, Hanoi', '0988776655', 'https://greenclean.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 0, NOW(), NOW()),
(@prov9_id, @prov_user9_id, 'Yoga Bình Yên', 'Các lớp yoga cân bằng thân, tâm, trí.', '909 Relax Rd, Hanoi', '0912349876', 'https://peacefulyoga.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'SUSPENDED', 1, NOW(), NOW()),
(@prov10_id, @prov_user10_id, 'Salon Tóc Nam Vua', 'Chuỗi salon tóc nam hiện đại và đẳng cấp.', '1010 Fashion St, Hanoi', '0909090909', 'https://prestigebarber.com', '/uploads/blank.jpg', '/uploads/blank.jpg', 'ACTIVE', 1, NOW(), NOW());
INSERT INTO `provider_applications` (`id`, `user_id`, `business_name`, `bio`, `address`, `phone_number`, `website`, `business_license_file_url`, `status`, `rejection_reason`, `submitted_at`, `reviewed_at`, `reviewed_by`, `created_at`, `updated_at`) VALUES
(@appreg1_id, @app_user1_id, 'Nhiếp Ảnh A', 'Professional photography services.', '1 A Street, Hanoi', '0901112221', 'https://aphotography.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
(@appreg2_id, @app_user2_id, 'Tiệm Bánh B', 'Homemade sweets, delivered to your door.', '2 B Street, Hanoi', '0901112222', 'https://bhomebakery.com', '/uploads/blank.jpg', 'APPROVED', NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), @admin_id, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(@appreg3_id, @app_user3_id, 'Huấn Luyện C', 'Personal trainer at the gym or at home.', '3 C Street, Hanoi', '0901112223', 'https://cfitness.com', '/uploads/blank.jpg', 'REJECTED', 'Hồ sơ thiếu thông tin về bằng cấp chuyên môn.', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), @admin_id, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(@appreg4_id, @user1_id, 'Dịch Thuật Hùng', 'English-Vietnamese document translation.', '456 User Ave, Hoan Kiem District, Hanoi', '0123456789', NULL, NULL, 'CANCELLED', NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
(@appreg5_id, @app_user1_id, 'Nhiếp Ảnh A - Lần 2', 'Re-registering photography service.', '1 A Street, Hanoi', '0901112221', 'https://aphotography.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
(@appreg6_id, @app_user2_id, 'Tiệm Bánh B - Cập Nhật', 'Updating business information.', '2B B Street, Hanoi', '0901112222', 'https://bhomebakery.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
(@appreg7_id, @app_user3_id, 'Huấn Luyện C - Nộp Lại', 'Re-submitting fitness coach application.', '3 C Street, Hanoi', '0901112223', 'https://cfitness.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW()),
(@appreg8_id, @user2_id, 'Thiết Kế Alice', 'Logo and banner design services.', '789 Customer Blvd, Hai Ba Trung District, Hanoi', '0988888888', 'https://alicedesign.com', '/uploads/blank.jpg', 'APPROVED', NULL, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), @admin_id, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
(@appreg9_id, @app_user1_id, 'Rửa Xe Tại Nhà A', 'At-home car wash service.', '1 A Street, Hanoi', '0901112221', NULL, NULL, 'REJECTED', 'Mô hình kinh doanh không phù hợp.', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), @admin_id, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(@appreg10_id, @app_user2_id, 'Chăm Sóc Thú Cưng B', 'Pet sitting service.', '2 B Street, Hanoi', '0901112222', 'https://bpet.com', '/uploads/blank.jpg', 'PENDING', NULL, NOW(), NULL, NULL, NOW(), NOW());
SELECT @ser1_id := UUID(), @ser2_id := UUID(), @ser3_id := UUID(), @ser4_id := UUID(), @ser5_id := UUID();
SELECT @ser6_id := UUID(), @ser7_id := UUID(), @ser8_id := UUID(), @ser9_id := UUID(), @ser10_id := UUID();
SELECT @ser11_id := UUID(), @ser12_id := UUID(), @ser13_id := UUID(), @ser14_id := UUID(), @ser15_id := UUID();
INSERT INTO `services` (`id`, `provider_id`, `category_id`, `service_name`, `description`, `image_url`, `duration_minutes`, `price`, `buffer_time_after`, `capacity`, `is_active`, `created_at`, `updated_at`) VALUES
(@ser1_id, @prov1_id, @cat1_id, 'Cắt tóc tạo kiểu nam', 'Cắt, gội, sấy và vuốt sáp tạo kiểu theo yêu cầu của quý khách.', '/uploads/blank.jpg', 60, 100000.00, 10, 1, 1, NOW(), NOW()),
(@ser2_id, @prov1_id, @cat1_id, 'Uốn tóc nam', 'Tạo kiểu tóc xoăn bồng bềnh, lãng tử theo phong cách Hàn Quốc.', '/uploads/blank.jpg', 120, 400000.00, 15, 1, 1, NOW(), NOW()),
(@ser3_id, @prov2_id, @cat3_id, 'Chăm sóc da cơ bản', 'Làm sạch sâu, tẩy da chết, đắp mặt nạ dưỡng ẩm chuyên sâu.', '/uploads/blank.jpg', 60, 250000.00, 10, 2, 1, NOW(), NOW()),
(@ser4_id, @prov2_id, @cat5_id, 'Massage body tinh dầu', 'Massage toàn thân với tinh dầu thiên nhiên giúp thư giãn và giảm căng thẳng.', '/uploads/blank.jpg', 90, 450000.00, 15, 1, 1, NOW(), NOW()),
(@ser5_id, @prov5_id, @cat4_id, 'Sơn gel Hàn Quốc', 'Sơn móng tay/chân bằng gel cao cấp, bền màu, không gây hại móng.', '/uploads/blank.jpg', 45, 150000.00, 5, 3, 1, NOW(), NOW()),
(@ser6_id, @prov4_id, @cat7_id, 'Thay màn hình iPhone', 'Thay thế màn hình iPhone các dòng, bảo hành 6 tháng chính hãng.', '/uploads/blank.jpg', 30, 1200000.00, 0, 1, 1, NOW(), NOW()),
(@ser7_id, @prov3_id, @cat8_id, 'Tư vấn tâm lý 1:1', 'Phiên tư vấn riêng tư cùng chuyên gia tâm lý (60 phút) bảo mật tuyệt đối.', '/uploads/blank.jpg', 60, 500000.00, 15, 1, 1, NOW(), NOW()),
(@ser8_id, @prov6_id, @cat6_id, 'Trang điểm dự tiệc', 'Gói trang điểm cá nhân đi tiệc, sự kiện, làm nổi bật vẻ đẹp tự nhiên.', '/uploads/blank.jpg', 75, 500000.00, 10, 1, 1, NOW(), NOW()),
(@ser9_id, @prov7_id, @cat9_id, 'Gia sư môn Toán lớp 9', 'Dạy kèm môn Toán cho học sinh lớp 9 (2 tiếng/buổi), cam kết tiến bộ.', '/uploads/blank.jpg', 120, 300000.00, 0, 1, 1, NOW(), NOW()),
(@ser10_id, @prov8_id, @cat10_id, 'Dọn nhà theo giờ', 'Gói dọn dẹp cơ bản 3 tiếng cho căn hộ dưới 80m2, bao gồm lau dọn, hút bụi.', '/uploads/blank.jpg', 180, 400000.00, 0, 5, 1, NOW(), NOW()),
(@ser11_id, @prov10_id, @cat1_id, 'Nhuộm màu thời trang', 'Nhuộm tóc nam với các màu hot trend nhất, bảo hành màu.', '/uploads/blank.jpg', 90, 600000.00, 15, 2, 1, NOW(), NOW()),
(@ser12_id, @prov2_id, @cat3_id, 'Liệu trình trị mụn chuyên sâu', 'Lấy nhân mụn, điện di tinh chất, chiếu ánh sáng sinh học, giảm sưng tấy.', '/uploads/blank.jpg', 90, 700000.00, 15, 1, 1, NOW(), NOW()),
(@ser13_id, @prov5_id, @cat4_id, 'Đắp móng bột', 'Tạo form móng và kéo dài móng bằng bột, cứng cáp và bền đẹp.', '/uploads/blank.jpg', 90, 350000.00, 10, 2, 1, NOW(), NOW()),
(@ser14_id, @prov4_id, @cat7_id, 'Thay pin điện thoại Samsung', 'Thay pin chính hãng cho các dòng máy Samsung Galaxy, bảo hành 3 tháng.', '/uploads/blank.jpg', 20, 450000.00, 5, 1, 1, NOW(), NOW()),
(@ser15_id, @prov10_id, @cat1_id, 'Combo Cắt + Uốn', 'Gói dịch vụ tiết kiệm bao gồm cắt và uốn tóc nam, tạo kiểu chuyên nghiệp.', '/uploads/blank.jpg', 150, 850000.00, 15, 1, 0, NOW(), NOW());
SELECT @sch1_id := UUID(), @sch2_id := UUID(), @sch3_id := UUID(), @sch4_id := UUID(), @sch5_id := UUID();
SELECT @sch6_id := UUID(), @sch7_id := UUID(), @sch8_id := UUID(), @sch9_id := UUID(), @sch10_id := UUID();
SELECT @sch11_id := UUID(), @sch12_id := UUID(), @sch13_id := UUID(), @sch14_id := UUID(), @sch15_id := UUID();
INSERT INTO `provider_schedules` (`id`, `provider_id`, `service_id`, `start_time`, `end_time`, `max_capacity`, `remaining_slots`, `status`, `notes`, `created_at`, `updated_at`) VALUES
(@sch1_id, @prov1_id, @ser1_id, DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 1 HOUR), 1, 1, 'AVAILABLE', 'Lịch trống buổi sáng', NOW(), NOW()),
(@sch2_id, @prov1_id, @ser1_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 3 HOUR), 1, 0, 'BOOKED', NULL, NOW(), NOW()),
(@sch3_id, @prov2_id, @ser3_id, DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 1 HOUR), 2, 2, 'AVAILABLE', 'Còn 2 suất', NOW(), NOW()),
(@sch4_id, @prov2_id, @ser4_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 3 HOUR), INTERVAL 30 MINUTE), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch5_id, @prov5_id, @ser5_id, DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 45 MINUTE), 3, 2, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch6_id, @prov4_id, @ser6_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 1 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 1 HOUR), INTERVAL 30 MINUTE), 1, 1, 'AVAILABLE', 'Slot vàng', NOW(), NOW()),
(@sch7_id, @prov3_id, @ser7_id, DATE_ADD(NOW(), INTERVAL 4 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 1 HOUR), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch8_id, @prov8_id, @ser10_id, DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 5 DAY), INTERVAL 3 HOUR), 5, 4, 'AVAILABLE', 'Nhận dọn khu vực Cầu Giấy', NOW(), NOW()),
(@sch9_id, @prov10_id, @ser11_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 5 HOUR), INTERVAL 30 MINUTE), 2, 2, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch10_id, @prov2_id, @ser12_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 2 DAY), INTERVAL 5 HOUR), INTERVAL 30 MINUTE), 1, 0, 'BOOKED', 'Đã có khách đặt', NOW(), NOW()),
(@sch11_id, @prov1_id, @ser2_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 10 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 12 HOUR), 1, 1, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch12_id, @prov1_id, @ser1_id, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 23 HOUR), 1, 0, 'COMPLETED', 'Lịch đã hoàn thành', NOW(), NOW()),
(@sch13_id, @prov5_id, @ser5_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 2 HOUR), INTERVAL 45 MINUTE), 3, 3, 'AVAILABLE', NULL, NOW(), NOW()),
(@sch14_id, @prov3_id, @ser7_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 2 HOUR), DATE_ADD(DATE_ADD(NOW(), INTERVAL 4 DAY), INTERVAL 3 HOUR), 1, 0, 'BOOKED', NULL, NOW(), NOW()),
(@sch15_id, @prov4_id, @ser14_id, DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 4 HOUR), INTERVAL 20 MINUTE), 1, 1, 'AVAILABLE', NULL, NOW(), NOW());
SELECT @app1_id := UUID(), @app2_id := UUID(), @app3_id := UUID(), @app4_id := UUID(), @app5_id := UUID();
SELECT @app6_id := UUID(), @app7_id := UUID(), @app8_id := UUID(), @app9_id := UUID(), @app10_id := UUID();
INSERT INTO `appointments` (`id`, `user_id`, `provider_schedule_id`, `status`, `notes_from_user`, `cancellation_reason`, `created_at`, `updated_at`) VALUES
(@app1_id, @user1_id, @sch2_id, 'CONFIRMED', 'Vui lòng gọi trước khi tôi đến 15 phút.', NULL, NOW(), NOW()),
(@app2_id, @user2_id, @sch10_id, 'CONFIRMED', 'Da mình hơi nhạy cảm, mong được tư vấn kỹ.', NULL, NOW(), NOW()),
(@app3_id, @user1_id, @sch5_id, 'SCHEDULED', 'Mình sẽ đi cùng 1 người bạn.', NULL, NOW(), NOW()),
(@app4_id, @user2_id, @sch14_id, 'SCHEDULED', NULL, NULL, NOW(), NOW()),
(@app5_id, @user1_id, @sch12_id, 'COMPLETED', 'Rất hài lòng với dịch vụ.', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(@app6_id, @user2_id, @sch1_id, 'SCHEDULED', 'Mình muốn cắt kiểu layer.', NULL, NOW(), NOW()),
(@app7_id, @user1_id, @sch3_id, 'SCHEDULED', NULL, NULL, NOW(), NOW()),
(@app8_id, @user2_id, @sch4_id, 'SCHEDULED', NULL, NULL, NOW(), NOW()),
(@app9_id, @user1_id, @sch6_id, 'CANCELLED', NULL, 'Có việc đột xuất.', NOW(), NOW()),
(@app10_id, @user2_id, @sch7_id, 'CONFIRMED', 'Đây là lần đầu mình sử dụng dịch vụ.', NULL, NOW(), NOW());
SET FOREIGN_KEY_CHECKS = 1;