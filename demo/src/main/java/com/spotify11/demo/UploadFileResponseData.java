package com.spotify11.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileResponseData {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
