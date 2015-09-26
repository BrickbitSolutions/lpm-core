package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.home.NewUserCommand;
import be.brickbit.lpm.core.service.IUserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HomeControllerTest extends AbstractControllerTest {
    @Mock
    private IUserService userService;

    @InjectMocks
    private HomeController homeController;

    @Test
    public void getHello() throws Exception {
        mvc().perform(MockMvcRequestBuilders.get("/"))
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

        mvc().perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(convertToJson(newUserCommand)))
                .andExpect(status().isCreated());
    }

    @Override
    protected Object getControllerInstance() {
        return homeController;
    }
}