package com.hicode.webfluxhandlefile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileInfo {
    private String filename;
    private String url;
}
