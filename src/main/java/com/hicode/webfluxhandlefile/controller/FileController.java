package com.hicode.webfluxhandlefile.controller;

import com.hicode.webfluxhandlefile.model.FileInfo;
import com.hicode.webfluxhandlefile.model.ResponseMessage;
import com.hicode.webfluxhandlefile.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class FileController {

    private FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public Mono<ResponseEntity<ResponseMessage>> uploadFile(@RequestPart("file")Mono<FilePart> filePartMono){
        return fileStorageService.save(filePartMono).map(filename -> ResponseEntity.ok(new ResponseMessage("Upload the file successfully"+filename)));
    }

    @GetMapping("/files")
    public ResponseEntity<Flux<FileInfo>> getListFiles(){
        Stream<FileInfo> fileInfoStream = fileStorageService.loadAll()
                .map(path -> {
                    String filename = path.getFileName().toString();
                    String url = UriComponentsBuilder.newInstance().path("/files/{filename}").buildAndExpand(filename).toString();
                    return new FileInfo(filename, url);
                });
        Flux<FileInfo> fileInfosFlux =Flux.fromStream(fileInfoStream);
        return ResponseEntity.ok(fileInfosFlux);

    }
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Flux<DataBuffer>> getFile(@PathVariable("filename")String filename){
        Flux<DataBuffer> file = fileStorageService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ filename+ "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);

    }
}
