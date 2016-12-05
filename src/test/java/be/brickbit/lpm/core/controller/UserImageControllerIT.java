package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.AbstractControllerIT;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.FileCopyUtils;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserImageControllerIT extends AbstractControllerIT {

    @Test
    public void savesNewImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image.png", "image.png", "image/png", "image".getBytes());

        mvc().perform(
                MockMvcRequestBuilders.fileUpload("/user/1/image")
                        .file(image)
                        .with(defaultuser()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void throwsExceptionOnWrongImageFormat() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", "image".getBytes());

        mvc().perform(
                MockMvcRequestBuilders.fileUpload("/user/1/image")
                        .file(image)
                        .with(defaultuser()))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.cause", is("File format not supported.")));
    }

    @Test
    public void readsProfileImage() throws Exception {
        File savedImage = new File("/tmp/1.png");
        FileCopyUtils.copy(
                FileCopyUtils.copyToByteArray(
                        this.getClass().getClassLoader().getResourceAsStream("png_test_by_destron23.png")
                ),
                savedImage
        );

        mvc().perform(
                MockMvcRequestBuilders
                        .get("/user/1/image")
                        .with(defaultuser()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void readsDefaultProfileImage() throws Exception {
        mvc().perform(
                MockMvcRequestBuilders
                        .get("/user/100/image")
                        .with(defaultuser()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}