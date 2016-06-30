package be.brickbit.lpm.core;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.brickbit.lpm.Application;

import javax.servlet.Filter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles("test")
public abstract class AbstractControllerIT extends AbstractIT{
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private Filter springSecurityFilterChain;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(springSecurityFilterChain)
				.build();
	}

	protected String convertToJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	protected ResultActions performGet(String url) throws Exception {
		return mvc().perform(
				MockMvcRequestBuilders.get(url)
						.accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YWRtaW46YWRtaW4="));
	}

	protected ResultActions performPost(String url, Object command) throws Exception {
		return mvc().perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
				.accept(MediaType.APPLICATION_JSON)
				.content(convertToJson(command)));
	}

    protected ResultActions performPut(String url, Object command) throws Exception {
        return mvc().perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .accept(MediaType.APPLICATION_JSON)
                .content(convertToJson(command)));
    }

	protected MockMvc mvc() {
		return mvc;
	}
}
