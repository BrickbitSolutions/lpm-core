package be.brickbit.lpm.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.subethamail.wiser.Wiser;

import be.brickbit.lpm.Application;
import be.brickbit.lpm.core.util.OAuthHelper;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractControllerIT extends AbstractIT {
    private MockMvc mvc;
    private Wiser wiser;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private OAuthHelper oAuthHelper;

    private RequestPostProcessor defaultUser;

    @Before
    public void setUp() throws Exception {
        defaultUser = token("admin", "ROLE_ADMIN", "ROLE_USER");
        wiser = new Wiser();
        wiser.setPort(8070);
        wiser.setHostname("localhost");
        wiser.start();

        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    protected MockMvc mvc() {
        return mvc;
    }

    protected RequestPostProcessor defaultuser() {
        return defaultUser;
    }

    protected Wiser wiser() {
        return wiser;
    }

    protected String convertToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected ResultActions performGet(String url) throws Exception {
        return mvc.perform(
                get(url)
                        .with(defaultUser)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions performPost(String url, Object command) throws Exception {
        return mvc.perform(
                post(url)
                        .with(defaultUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(convertToJson(command)))
                .andDo(print());
    }

    protected ResultActions performPut(String url, Object command) throws Exception {
        return mvc.perform(
                put(url)
                        .with(defaultUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(convertToJson(command)))
                .andDo(print());
    }

    protected ResultActions performDelete(String url) throws Exception {
        return mvc.perform(
                delete(url)
                        .with(defaultUser)
                        .accept(APPLICATION_JSON))
                .andDo(print());
    }

    private RequestPostProcessor token(String username, String... authorities) {
        return oAuthHelper.addBearerToken(username, authorities);
    }
}
