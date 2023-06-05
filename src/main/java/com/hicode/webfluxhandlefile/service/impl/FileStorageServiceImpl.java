package com.hicode.webfluxhandlefile.service.impl;

import com.hicode.webfluxhandlefile.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    /**
     * The code Paths.get("uploads") creates
     * a Path object representing a file or directory path named "uploads".
     * */
    private final Path root = Paths.get("uploads");
    @Override
    public void init() {
        try{
            // create directories in the root source
            Files.createDirectories(root);
        }
        catch (IOException e){
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Mono<String> save(Mono<FilePart> filePartMono) {
        return filePartMono.doOnNext(fp -> System.out.println("Receiving File : "+ fp.filename())).flatMap(filePart -> {
            String fileName = filePart.filename();
            return filePart.transferTo(root.resolve(fileName)).then(Mono.just(fileName));
        });

    }

    @Override
    public Flux<DataBuffer> load(String fileName) {
        try{
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            System.out.println(resource);
            if(resource.exists() || resource.isReadable()){
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4086);
            }else{
                throw new RuntimeException("could not read the file!");
            }
        }
         catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root,1)
                     .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
