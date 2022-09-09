package com.dreamtea.commands;

import com.dreamtea.imixin.IManageDeadSpectators;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class  OpSpectate  implements CommandRegistrationCallback{
  private static final SimpleCommandExceptionType ALREADY_OPPED_EXCEPTION = new SimpleCommandExceptionType(Text.of("Already In Op spectate mode"));
  private static final SimpleCommandExceptionType ALREADY_DEOPPED_EXCEPTION = new SimpleCommandExceptionType(Text.of("Already In Dead spectate mode"));

  public static void init(){
    CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> new OpSpectate().register(dispatcher, registryAccess, environment)));
  }

  public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(
      CommandManager.literal("deadspectate")
                      .requires((source) -> source.hasPermissionLevel(3))
      .then((CommandManager.argument("targets", GameProfileArgumentType.gameProfile())
                            .suggests((context, builder) -> {
                                  PlayerManager playerManager = context.getSource().getServer().getPlayerManager();
                                  return CommandSource.suggestMatching(playerManager.getPlayerList().stream().map((player) -> {
                                    return player.getGameProfile().getName();
                                  }), builder);
                            })
            ).then(CommandManager.literal("op").executes((context) -> op(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets"), false)))
             .then(CommandManager.literal("dead").executes((context) -> op(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets"), true)))
      )
    );
  }

  private static int op(ServerCommandSource source, Collection<GameProfile> targets, boolean deadSpectate) throws CommandSyntaxException {
    IManageDeadSpectators playerManager = (IManageDeadSpectators) source.getServer().getPlayerManager();
    int i = 0;

    for (GameProfile gameProfile : targets) {
      if (deadSpectate != playerManager.willDeadSpectate(gameProfile)) {
          playerManager.setDeadSpectate(gameProfile, deadSpectate);
          ++i;
          source.sendFeedback(Text.of(gameProfile.getName() + " made " + (deadSpectate? "dead": "op")), true);
      }
    }
    if (i == 0) {
      throw deadSpectate? ALREADY_DEOPPED_EXCEPTION.create(): ALREADY_OPPED_EXCEPTION.create();
    } else {
      return i;
    }
  }

  @Override
  public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
    register(dispatcher);
  }
}
