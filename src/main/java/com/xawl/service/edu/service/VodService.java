package com.xawl.service.edu.service;



import org.springframework.web.multipart.MultipartFile;


public interface VodService {
    String uploadVideo(MultipartFile file);
}
