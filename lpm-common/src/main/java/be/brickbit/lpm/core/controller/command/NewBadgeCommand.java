package be.brickbit.lpm.core.controller.command;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class NewBadgeCommand {
    @NotNull
    private Long userId;
    private String token;
}
