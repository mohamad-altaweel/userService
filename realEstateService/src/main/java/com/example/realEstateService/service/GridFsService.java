package com.example.realEstateService.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import com.mongodb.client.model.Filters;

import java.io.IOException;

@Service
public class GridFsService {

    private final MongoDatabaseFactory  mongoDbFactory;

    @Autowired
    public GridFsService(MongoDatabaseFactory  mongoDbFactory) {
        this.mongoDbFactory = mongoDbFactory;
    }

    public String storeFile(MultipartFile file) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDbFactory.getMongoDatabase());
        ObjectId fileId = gridFSBucket.uploadFromStream(file.getOriginalFilename(), file.getInputStream(), new GridFSUploadOptions());
        return fileId.toHexString();
    }

    public Resource getFile(String id) throws IOException {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDbFactory.getMongoDatabase());

        // Create a filter to find the file by its ObjectId
        Bson filter = Filters.eq("_id", new ObjectId(id));
        GridFSFile gridFSFile = gridFSBucket.find(filter).first();

        if (gridFSFile == null) {
            throw new IllegalStateException("File not found with id: " + id);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId())) {
            int data = downloadStream.read();
            while (data != -1) {
                outputStream.write(data);
                data = downloadStream.read();
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }
}

