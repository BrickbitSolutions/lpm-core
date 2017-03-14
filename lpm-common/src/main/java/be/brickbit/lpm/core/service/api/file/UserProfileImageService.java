package be.brickbit.lpm.core.service.api.file;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface UserProfileImageService {
    Optional<File> getImage(String userId);

    Boolean deleteImage(String userId);

    void saveImage(String userId, byte[] imageFile) throws IOException;
}
