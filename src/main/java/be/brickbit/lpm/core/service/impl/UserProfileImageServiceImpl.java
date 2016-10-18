package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.service.api.file.UserProfileImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserProfileImageServiceImpl implements UserProfileImageService {

    @Value("${lpm.storage.images.user}")
    private String userProfileImageLocation;

    @Override
    public Optional<File> getImage(String userId) {
        File image = new File(Paths.get(userProfileImageLocation, userId + ".png").toString());

        if (image.exists()) {
            return Optional.of(image);
        }

        return Optional.empty();
    }

    @Override
    public Boolean deleteImage(String userId) {
        File fileToDelete = new File(Paths.get(userProfileImageLocation, userId + ".png").toString());
        return fileToDelete.exists() && fileToDelete.delete();
    }

    @Override
    public void saveImage(String userId, byte[] imageFile) throws IOException {
        File newImage = new File(Paths.get(userProfileImageLocation, userId + ".png").toString());
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(newImage));
        stream.write(imageFile);
        stream.close();
    }
}
