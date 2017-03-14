package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.service.api.file.UserProfileImageService;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserImageController {

    @Autowired
    private UserProfileImageService userProfileImageService;

    @RequestMapping(value = "{id}/image", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize(value = "hasAnyRole('USER')")
    public ResponseEntity<InputStreamResource> getProductImage(@PathVariable("id") String id) throws IOException {
        Optional<File> image = userProfileImageService.getImage(id);

        if (image.isPresent()) {
            return ResponseEntity.ok(new InputStreamResource(new FileInputStream(image.get())));
        } else {
            return ResponseEntity.ok((new InputStreamResource(this.getClass().getClassLoader().getResourceAsStream("nophoto.png"))));
        }
    }

    @RequestMapping(value = "{id}/image", method = RequestMethod.POST)
    @PreAuthorize(value = "hasAnyRole('CATERING_ADMIN', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductImage(MultipartHttpServletRequest request, @PathVariable("id") String id) throws IOException {
        Iterator<String> itr = request.getFileNames();
        MultipartFile file = request.getFile(itr.next());
        if (file.getContentType().equals("image/png")) {
            userProfileImageService.saveImage(id, file.getBytes());
        } else {
            throw new ServiceException("File format not supported.");
        }
    }
}
