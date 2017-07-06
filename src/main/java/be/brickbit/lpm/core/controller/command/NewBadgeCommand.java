package be.brickbit.lpm.core.controller.command;

import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
public class NewBadgeCommand {
    @NotNull
    private Long userId;
    private String token;
}
