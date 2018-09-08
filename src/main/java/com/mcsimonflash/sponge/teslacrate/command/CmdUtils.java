package com.mcsimonflash.sponge.teslacrate.command;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.mcsimonflash.sponge.teslacore.tesla.Tesla;
import com.mcsimonflash.sponge.teslacore.tesla.TeslaUtils;
import com.mcsimonflash.sponge.teslacrate.TeslaCrate;
import com.mcsimonflash.sponge.teslacrate.internal.Registry;
import com.mcsimonflash.sponge.teslacrate.internal.Utils;
import com.mcsimonflash.sponge.teslalibs.argument.Arguments;
import com.mcsimonflash.sponge.teslalibs.argument.CommandElement;
import com.mcsimonflash.sponge.teslalibs.argument.FlagsElement;
import com.mcsimonflash.sponge.teslalibs.command.Command;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tuple;

import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CmdUtils {

    public static final CommandElement
            CRATE_ELEM = Arguments.choices(Registry.CRATES.getComponents().getAll(), ImmutableMap.of("no-choice", "Input <arg> is not the id of a crate.")).map(Tuple::getFirst).toElement("crate"),
            EFFECT_ELEM = Arguments.choices(Registry.EFFECTS.getComponents().getAll(), ImmutableMap.of("no-choice", "Input <arg> is not the id of a effect.")).map(Tuple::getFirst).toElement("effect"),
            KEY_ELEM = Arguments.choices(Registry.KEYS.getComponents().getAll(), ImmutableMap.of("no-choice", "Input <arg> is not the id of a key.")).map(Tuple::getFirst).toElement("key"),
            PRIZE_ELEM = Arguments.choices(Registry.PRIZES.getComponents().getAll(), ImmutableMap.of("no-choice", "Input <arg> is not the id of a prize.")).map(Tuple::getFirst).toElement("prize"),
            REWARD_ELEM = Arguments.choices(Registry.REWARDS.getComponents().getAll(), ImmutableMap.of("no-choice", "Input <arg> is not the id of a reward.")).map(Tuple::getFirst).toElement("reward"),
            PLAYER_ELEM = Arguments.player().toElement("player"),
            USER_ELEM = Arguments.user().toElement("user"),
            USER_SELECTOR_ELEM = Arguments.userSelector().toElement("users"),
            USER_OR_SOURCE_ELEM = Arguments.user().orSource().toElement("user"),
            LOCATION_ELEM = Arguments.location().toElement("location"),
            OPT_LOCATION_ELEM = Arguments.location().optional().toElement("location"),
            QUANTITY_ELEM = Arguments.intObj().inRange(Range.atLeast(1)).toElement("quantity");
    public static final FlagsElement
            NO_GUI_FLAG = Arguments.flags().flag("nogui").build();
    public static final Text
            SUBCOMMAND_ARG = arg(true, "...", "A subcommand."),
            CRATE_ARG = arg(true, "crate", "Id of a crate component."),
            EFFECT_ARG = arg(true, "effect", "Id of an item component."),
            KEY_ARG = arg(true, "key", "Id of a key component."),
            PRIZE_ARG = arg(true, "prize", "Id of a prize component."),
            REWARD_ARG = arg(true, "reward", "Id of a reward component."),
            PLAYER_ARG = arg(true, "player", "Name of an online player."),
            USER_ARG = arg(true, "user", "Name of a user."),
            USER_SELECTOR_ARG = arg(true, "users", "User selector."),
            USER_OR_SOURCE_ARG = arg(false, "user", "Name of a user. Defaults to the source if the source is a user."),
            LOCATION_ARG = arg(true, "location", "A location (world and position). The world is taken from the source if possible, the position must be provided."),
            OPT_LOCATION_ARG = arg(false, "position", "A location (world and position). If undefined, the location is taken from the source or given player where applicable."),
            QUANTITY_ARG = arg(true, "quantity", "An integer between 1 and 64 inclusive."),
            NO_GUI_ARG = arg(false, "-nogui", "If defined, a text display will be shown instead of the gui.");

    private static final Text LINKS = Text.of("                      ", link("Ore Project", TeslaCrate.get().getContainer().getUrl().flatMap(TeslaUtils::parseURL)), TextColors.GRAY, " | ", link("Support Discord", Tesla.DISCORD));

    public static Player requirePlayer(CommandSource src) throws CommandException {
        if (src instanceof Player) {
            return (Player) src;
        }
        throw new CommandException(TeslaCrate.getMessage(src, "teslacrate.command.player-only"));
    }

    public static Text usage(String base, String description, Text... args) {
        return Text.builder(base)
                .color(TextColors.GOLD)
                .onClick(args.length == 0 ? TextActions.runCommand(base) : TextActions.suggestCommand(base))
                .onHover(TextActions.showText(Text.of(TextColors.GRAY, description)))
                .append(args.length == 0 && base.endsWith(" ") ? SUBCOMMAND_ARG : Text.joinWith(Text.of(" "), args))
                .build();
    }

    public static Text arg(boolean req, String name, String desc) {
        return Text.builder((req ? "<" : "[") + name + (req ? ">" : "]"))
                .color(TextColors.YELLOW)
                .onHover(TextActions.showText(Text.of(TextColors.WHITE, name, ": ", TextColors.GRAY, desc)))
                .build();
    }

    public static Text link(String name, Optional<URL> optUrl) {
        return optUrl.map(u -> Text.builder(name)
                .color(TextColors.WHITE)
                .onClick(TextActions.openUrl(u))
                .onHover(TextActions.showText(Text.of(u)))
                .build()).orElseGet(() -> Text.builder(name)
                .color(TextColors.RED)
                .onHover(TextActions.showText(Text.of("Sorry! This URL is unavailable.")))
                .build());
    }

    public static PaginationList getUsages(Command command) {
        return PaginationList.builder()
                .title(TeslaCrate.get().getPrefix())
                .padding(Utils.toText("&7="))
                .contents(Stream.concat(Stream.of(command.getUsage()), command.getChildren().stream().map(Command::getUsage)).collect(Collectors.toList()))
                .footer(LINKS)
                .build();
    }

}
