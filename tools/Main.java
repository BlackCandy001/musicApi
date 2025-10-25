package com.minh.musicApi;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Đường dẫn tới thư mục chứa các file MP3
        Path folderPath = Paths.get("C:/Users//84338/Music");
        File folder = folderPath.toFile();

        // Lấy tất cả các file MP3 trong thư mục
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));
        if (files == null || files.length == 0) {
            System.out.println("Không có file MP3 nào trong thư mục.");
            return;
        }

        // Tạo đối tượng RestTemplate để gọi API
        RestTemplate restTemplate = new RestTemplate();

        // Tạo List để lưu các file MP3
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            fileList.add(file);
        }

        // In ra số lượng file và bắt đầu quá trình tải lên
        System.out.println("Uploading " + fileList.size() + " file...");

        // Lặp qua từng file để gọi API
        for (int i = 0; i < fileList.size(); i++) {
            File mp3File = fileList.get(i);
            String title = mp3File.getName(); // Lấy tên file làm title
            String artist = "Unknown Artist"; // Có thể thay đổi nếu cần

            // Kiểm tra kích thước file trước khi gửi (ví dụ: giới hạn 10MB)
            if (mp3File.length() > 10 * 1024 * 1024) {  // Kiểm tra nếu file lớn hơn 10MB
                System.out.println("File " + title + " quá lớn, bỏ qua.");
                continue;  // Bỏ qua file này và tiếp tục với file tiếp theo
            }

            // Hiển thị tiến trình
            System.out.println("Uploading file " + (i + 1) + " / " + fileList.size() + ": " + title);

            // Gọi API để tải lên file
            boolean success = false;
            try {
                // Đọc file MP3 và gửi lên API
                MultipartFile multipartFile = FileUtils.convertToMultipartFile(mp3File);
                success = uploadSong(restTemplate, multipartFile, title, artist);
            } catch (Exception e) {
                System.err.println("Error sending file " + title + ": " + e.getMessage());
            }

            if (!success) {
                System.out.println("File " + title + " không thể tải lên, bỏ qua.");
            }
        }

        System.out.println("Upload complete");
    }

    // Phương thức gửi file lên API
    public static boolean uploadSong(RestTemplate restTemplate, MultipartFile file, String title, String artist) {
        try {
            // Xây dựng request body
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Tạo body cho request (Multipart)
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());
            body.add("title", title);
            body.add("artist", artist);

            // Tạo request entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Gửi POST request tới API
            String url = "http://103.166.183.52:8080/api/songs"; // Địa chỉ API của bạn
            //String url = "http://localhost:8080/api/songs";

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // Kiểm tra phản hồi
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Uploading " + title + " thành công.");
                return true;
            } else {
                System.out.println("File sending failed  " + title + ": " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi API: " + e.getMessage());
            return false;
        }
    }
}
