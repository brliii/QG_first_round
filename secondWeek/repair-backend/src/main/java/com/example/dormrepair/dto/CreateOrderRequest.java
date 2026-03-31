package com.example.dormrepair.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateOrderRequest {
    private String deviceType;
    private String description;
    private Integer priority;
    private MultipartFile image; //图片文件，非必传
}