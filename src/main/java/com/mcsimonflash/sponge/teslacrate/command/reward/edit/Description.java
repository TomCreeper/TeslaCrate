package com.mcsimonflash.sponge.teslacrate.command.reward.edit;

import com.mcsimonflash.sponge.teslacrate.TeslaCrate;
import com.mcsimonflash.sponge.teslacrate.component.Reward;
import com.mcsimonflash.sponge.teslacrate.internal.Storage;
import com.mcsimonflash.sponge.teslacore.command.Arguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class Description implements CommandExecutor {

    public static final CommandSpec COMMAND = CommandSpec.builder()
            .executor(new Description())
            .arguments(Arguments.map("reward", Storage.rewards, Arguments.string("string")),
                    Arguments.remainingStrings("new-description"))
            .permission("teslacrate.command.reward.edit.description.base")
            .build();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Reward reward = args.<Reward>getOne("reward").get();
        String newDescription = args.<String>getOne("new-description").get();
        String oldDescription = reward.getDescription();
        reward.setDescription(newDescription);
        TeslaCrate.sendMessage(src, "teslacrate.command.reward.edit.description.success", "reward", reward.getName(), "new-description", newDescription, "old-description", oldDescription);
        return CommandResult.success();
    }

}