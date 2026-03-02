package com.creativeknight.event;

import com.creativeknight.CreativeKnightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = "creative_knight_config")
public class ItemIdCopyEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // 检查是否按下了复制物品ID按键
        if (event.getAction() == 1) { // 1表示按键按下
            if (CreativeKnightConfig.COPY_ITEM_ID_KEY.matches(event.getKey(), event.getScanCode())) {
                Minecraft minecraft = Minecraft.getInstance();
                
                // 检查是否在游戏中
                if (minecraft.player != null) {
                    // 获取鼠标悬停的物品
                    ItemStack hoveredItem = getHoveredItem();
                    
                    if (!hoveredItem.isEmpty()) {
                        // 获取物品的英文id
                        String itemId = hoveredItem.getItem().builtInRegistryHolder().key().location().toString();
                        
                        // 复制到剪贴板
                        minecraft.keyboardHandler.setClipboard(itemId);
                        
                        // 显示消息
                        minecraft.player.displayClientMessage(Component.literal("物品ID已复制到剪贴板: " + itemId), true);
                    } else {
                        // 显示没有物品悬停的消息
                        minecraft.player.displayClientMessage(Component.literal("请将鼠标悬停在物品上再按复制键"), true);
                    }
                }
            }
        }
    }
    
    private static ItemStack getHoveredItem() {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        
        // 检查是否在游戏中且有屏幕
        if (screen == null) {
            return ItemStack.EMPTY;
        }
        
        // 对于物品栏和背包界面
        if (screen instanceof net.minecraft.client.gui.screens.inventory.AbstractContainerScreen) {
            net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<?> containerScreen = (net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<?>) screen;
            // 在Minecraft 1.20.1中，使用getSlotUnderMouse方法来获取鼠标悬停的槽位
            net.minecraft.world.inventory.Slot hoveredSlot = containerScreen.getSlotUnderMouse();
            return hoveredSlot != null ? hoveredSlot.getItem() : ItemStack.EMPTY;
        }
        
        // 对于创造模式物品栏
        if (screen instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen) {
            net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen creativeScreen = (net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen) screen;
            // 获取创造模式物品栏中悬停的物品
            net.minecraft.world.inventory.Slot hoveredSlot = creativeScreen.getSlotUnderMouse();
            return hoveredSlot != null ? hoveredSlot.getItem() : ItemStack.EMPTY;
        }
        
        // 对于其他界面，可能需要其他方式获取悬停物品
        return ItemStack.EMPTY;
    }
}
