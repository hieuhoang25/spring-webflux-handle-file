package com.hicode.webfluxhandlefile.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    void init();

    Mono<String> save(Mono<FilePart> filePartMono);

    Flux<DataBuffer> load(String fileName);

    public Stream<Path> loadAll();
}
