package com.example.ir_semestralka.service;

import com.example.ir_semestralka.model.Article;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Article readArticle(int articleId);
}
