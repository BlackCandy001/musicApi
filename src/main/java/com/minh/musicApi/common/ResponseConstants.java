package com.minh.musicApi.common;

public class ResponseConstants{

    public static class Common {
        public static final String STATUS = "status";
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String MESSAGE = "message";
        public static final String FIELD = "field";
    }

    public static class Login {
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
        public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
        public static final String INVALID_CREDENTIALS = "Thông tin không hợp lệ";
    }

    // Có thể thêm các nhóm khác nếu cần, ví dụ:
    public static class Register {
        public static final String REGISTER_SUCCESS = "Đăng ký thành công";
        public static final String USERNAME_TAKEN = "Tên đăng nhập đã tồn tại";
    }

    private ResponseConstants() {
        // Ngăn tạo instance
    }
}