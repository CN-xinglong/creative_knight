package com.creativeknight;

import com.creativeknight.config.ModConfig;
import com.creativeknight.event.KnightEventHandler;
import com.creativeknight.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.client.ConfigScreenHandler;

@Mod(CreativeKnightConfig.MODID)
public class CreativeKnightConfig {
    public static final String MODID = "creative_knight_config";
    
    // 按键绑定
    public static final KeyMapping COPY_ITEM_ID_KEY = new KeyMapping(
        "key.creative_knight_config.copy_item_id",
        InputConstants.Type.KEYSYM,
        InputConstants.KEY_I,
        "key.categories.creative_knight_config"
    );

    public CreativeKnightConfig() {
        // 注册事件监听器
        MinecraftForge.EVENT_BUS.register(KnightEventHandler.class);
        
        // 注册配置
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG);
        
        // 注册客户端配置屏幕
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        // 注册命令
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        // 注册配置屏幕
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, 
            () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
        
        // 注册按键绑定
        event.enqueueWork(() -> {
            // 启用高级工具提示功能
            net.minecraft.client.Options options = Minecraft.getInstance().options;
            options.advancedItemTooltips = true;
        });
    }
    
    // 注册键位绑定
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 在Forge 1.20.1中，键位绑定会自动注册
            // 这里不需要额外的注册代码
        });
    }
    
    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        LiteralArgumentBuilder<CommandSourceStack> command = LiteralArgumentBuilder.<CommandSourceStack>
            literal("creativeknight")
            .then(LiteralArgumentBuilder.<CommandSourceStack>
                literal("config")
                .executes(context -> {
                    // 在客户端打开配置界面
                    Minecraft.getInstance().execute(() -> {
                        Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
                    });
                    context.getSource().sendSystemMessage(Component.literal("打开骑士配置界面"));
                    return 1;
                })
            );
        
        dispatcher.register(command);
    }
}
