package com.example.fbrckmacro;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class FbrckMacroClient implements ClientModInitializer {
    private static final String FEEDBACK_PREFIX = "§7[§aMacro§7] §r";

    private KeyBinding toggleMacroKey;
    private MacroSettings settings;
    private boolean macroEnabled = false;
    private long lastClickTime = 0L;

    @Override
    public void onInitializeClient() {
        this.settings = MacroSettings.load();

        this.toggleMacroKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fbrckmacro.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.fbrckmacro.general"
        ));

        registerCommands();
        registerTickLoop();
    }

    private void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("macro")
                        .executes(ctx -> {
                            sendFeedback(
                                    "Enabled: " + macroEnabled
                                            + ", delay: " + settings.getClickDelayMs() + "ms"
                                            + ", weaponOnly: " + settings.isOnlyWhenHoldingWeapon()
                                            + ", holdToClick: " + settings.isHoldToClick()
                            );
                            return 1;
                        })
                        .then(literal("toggle").executes(ctx -> {
                            macroEnabled = !macroEnabled;
                            sendFeedback("Auto-clicker " + (macroEnabled ? "enabled" : "disabled"));
                            return 1;
                        }))
                        .then(literal("set")
                                .then(argument("delayMs", IntegerArgumentType.integer(1, 2000)).executes(ctx -> {
                                    int delay = IntegerArgumentType.getInteger(ctx, "delayMs");
                                    settings.setClickDelayMs(delay);
                                    settings.save();
                                    sendFeedback("Delay set to " + delay + "ms");
                                    return 1;
                                })))
                        .then(literal("weaponOnly")
                                .then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                                    settings.setOnlyWhenHoldingWeapon(enabled);
                                    settings.save();
                                    sendFeedback("Weapon-only mode " + (enabled ? "enabled" : "disabled"));
                                    return 1;
                                })))
                        .then(literal("holdToClick")
                                .then(argument("enabled", BoolArgumentType.bool()).executes(ctx -> {
                                    boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                                    settings.setHoldToClick(enabled);
                                    settings.save();
                                    sendFeedback("Hold-to-click mode " + (enabled ? "enabled" : "disabled"));
                                    return 1;
                                })))
        ));
    }

    private void registerTickLoop() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleMacroKey.wasPressed()) {
                macroEnabled = !macroEnabled;
                sendFeedback("Auto-clicker " + (macroEnabled ? "enabled" : "disabled"));
            }

            if (!macroEnabled || client.player == null || client.world == null || client.currentScreen != null) {
                return;
            }

            if (settings.isHoldToClick() && !client.options.attackKey.isPressed()) {
                return;
            }

            if (settings.isOnlyWhenHoldingWeapon() && !isHoldingWeapon(client)) {
                return;
            }

            long now = System.currentTimeMillis();
            if (now - lastClickTime < settings.getClickDelayMs()) {
                return;
            }

            lastClickTime = now;
            clickAttack(client);
        });
    }

    private void clickAttack(MinecraftClient client) {
        client.execute(() -> {
            KeyBinding attack = client.options.attackKey;
            InputUtil.Key boundKey = attack.getBoundKey();
            KeyBinding.onKeyPressed(boundKey);
        });
    }

    private boolean isHoldingWeapon(MinecraftClient client) {
        ItemStack mainHand = client.player.getMainHandStack();
        return mainHand.getItem() instanceof SwordItem || mainHand.getItem() instanceof TridentItem;
    }

    private void sendFeedback(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(FEEDBACK_PREFIX + message), false);
        }
    }
}
