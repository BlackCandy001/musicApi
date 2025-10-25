package com.minh.musicApi;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

    // Chuyển File thành MultipartFile
    public static MultipartFile convertToMultipartFile(File file) throws IOException {
        // Đọc dữ liệu từ file và gán vào một byte array
        byte[] content = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(content);
        }

        // Tạo ByteArrayMultipartFile từ byte array và thông tin file
        return new ByteArrayMultipartFile(content, file.getName(), "audio/mp3");
    }
}
