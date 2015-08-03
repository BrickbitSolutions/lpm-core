package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.model.User;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class HomeControllerTest {
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HomeController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRegister() throws Exception {

        NewUserCommand newUserCommand = new NewUserCommand();
        newUserCommand.setUsername("jay");
        newUserCommand.setPassword("pwd");
        newUserCommand.setFirstName("Jonas");
        newUserCommand.setLastName("Liekens");
        newUserCommand.setEmail("soulscammer@gmail.com");

        mvc.perform(MockMvcRequestBuilders.post("/register")
                .sessionAttr("newUserCommand", newUserCommand))
                .andExpect(status().isCreated());
    }
}