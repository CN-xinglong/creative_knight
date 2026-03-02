package com.creativeknight.gui;

import com.creativeknight.config.ModConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private int currentTab = 0;
    private static final String[] TABS = {"spawn_chances", "mount_system", "weapons", "mounts"};
    private int currentPage = 0;
    private int maxPages = 1;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("creative_knight_config.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        // 清除所有现有组件
        clearWidgets();
        
        // 创建标签按钮
        int tabCount = TABS.length;
        int totalTabWidth = tabCount * 100; // 每个标签宽度90px，间距10px
        int startX = width / 2 - totalTabWidth / 2;
        
        for (int i = 0; i < TABS.length; i++) {
            final int tabIndex = i;
            addRenderableWidget(Button.builder(
                    Component.translatable("creative_knight_config.config.tab." + TABS[i]), 
                    button -> {
                        currentTab = tabIndex;
                        currentPage = 0;
                        init(); // 重新初始化界面
                    }
            ).bounds(startX + i * 100, 20, 90, 20).build());
        }
        
        // 根据当前标签显示不同的配置选项
        maxPages = 1;
        
        switch (currentTab) {
            case 0:
                showSpawnChancesTab();
                break;
            case 1:
                showMountSystemTab();
                break;
            case 2:
                showWeaponsTab();
                break;
            case 3:
                showMountsTab();
                break;
        }
    }
    

    
    // 通用方法：添加分页和返回按钮
    private void addNavigationButtons(int contentHeight) {
        // 计算按钮位置
        int paginationY = 100 + contentHeight + 20;
        
        // 确保按钮在屏幕内
        if (paginationY > height - 60) {
            paginationY = height - 60;
        }
        
        // 添加分页按钮
        if (maxPages > 1) {
            addRenderableWidget(Button.builder(
                    Component.literal("上一页"), 
                    button -> {
                        if (currentPage > 0) {
                            currentPage--;
                            init();
                        }
                    }
            ).bounds(width / 2 - 100, paginationY, 80, 20).build());
            
            addRenderableWidget(Button.builder(
                    Component.literal("下一页"), 
                    button -> {
                        if (currentPage < maxPages - 1) {
                            currentPage++;
                            init();
                        }
                    }
            ).bounds(width / 2 + 20, paginationY, 80, 20).build());
            
            addRenderableWidget(new StringWidget(width / 2 - 15, paginationY, 30, 20, Component.literal((currentPage + 1) + "/" + maxPages), font));
            paginationY += 30;
        }
        
        // 添加返回按钮
        addRenderableWidget(Button.builder(
                Component.translatable("creative_knight_config.config.back"), 
                button -> minecraft.setScreen(parent)
        ).bounds(width / 2 - 100, paginationY, 200, 20).build());
    }

    private void showSpawnChancesTab() {
        int centerX = width / 2;
        int y = 60; // 固定起始位置
        
        // 标题居中
        Component titleComponent = Component.translatable("creative_knight_config.config.tab.spawn_chances");
        int titleWidth = font.width(titleComponent);
        addRenderableWidget(new StringWidget(centerX - titleWidth / 2, y, titleWidth, 20, titleComponent, font));
        y += 40; // 增加标题与内容之间的间距
        
        // 计算显示的选项
        int startIndex = currentPage * 2;
        int endIndex = Math.min(startIndex + 2, 14); // 总共有14个选项
        maxPages = (14 + 1) / 2; // 每页2个选项
        
        // 僵尸骑士生成概率
        if (startIndex <= 0 && endIndex > 0) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombieKnightSpawnChance.get())), 
                    button -> {
                        // 循环增加概率
                        double current = ModConfig.zombieKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombieKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 骷髅骑士生成概率
        if (startIndex <= 1 && endIndex > 1) {
            Component labelComponent = Component.translatable("creative_knight_config.config.skeleton_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.skeletonKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.skeletonKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.skeletonKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 掠夺者骑士生成概率
        if (startIndex <= 2 && endIndex > 2) {
            Component labelComponent = Component.translatable("creative_knight_config.config.pillager_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.pillagerKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.pillagerKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.pillagerKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 猪灵骑士生成概率
        if (startIndex <= 3 && endIndex > 3) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.piglinKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.piglinKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.piglinKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 女巫骑士生成概率
        if (startIndex <= 4 && endIndex > 4) {
            Component labelComponent = Component.translatable("creative_knight_config.config.witch_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.witchKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.witchKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.witchKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 僵尸村民骑士生成概率
        if (startIndex <= 5 && endIndex > 5) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_villager_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombieVillagerKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.zombieVillagerKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombieVillagerKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 僵尸猪灵骑士生成概率
        if (startIndex <= 6 && endIndex > 6) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombified_piglin_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombifiedPiglinKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.zombifiedPiglinKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombifiedPiglinKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 凋灵骷髅骑士生成概率
        if (startIndex <= 7 && endIndex > 7) {
            Component labelComponent = Component.translatable("creative_knight_config.config.wither_skeleton_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.witherSkeletonKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.witherSkeletonKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.witherSkeletonKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 卫道士骑士生成概率
        if (startIndex <= 8 && endIndex > 8) {
            Component labelComponent = Component.translatable("creative_knight_config.config.vindicator_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.vindicatorKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.vindicatorKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.vindicatorKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 唤魔者骑士生成概率
        if (startIndex <= 9 && endIndex > 9) {
            Component labelComponent = Component.translatable("creative_knight_config.config.evoker_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.evokerKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.evokerKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.evokerKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 尸壳骑士生成概率
        if (startIndex <= 10 && endIndex > 10) {
            Component labelComponent = Component.translatable("creative_knight_config.config.husk_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.huskKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.huskKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.huskKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 猪灵蛮兵骑士生成概率
        if (startIndex <= 11 && endIndex > 11) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_brute_knight_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.piglinBruteKnightSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.piglinBruteKnightSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.piglinBruteKnightSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 武器生成概率
        if (startIndex <= 12 && endIndex > 12) {
            Component labelComponent = Component.translatable("creative_knight_config.config.weapon_spawn_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.weaponSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.weaponSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.weaponSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 头盔生成概率
        if (startIndex <= 13 && endIndex > 13) {
            Component labelComponent = Component.translatable("creative_knight_config.config.helmet_spawn_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.helmetSpawnChance.get())), 
                    button -> {
                        double current = ModConfig.helmetSpawnChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.helmetSpawnChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        

        

        
        // 计算内容高度
        int contentHeight = y - 60;
        
        // 添加分页和返回按钮
        addNavigationButtons(contentHeight);
    }

    private void showMountSystemTab() {
        int centerX = width / 2;
        int y = 60; // 固定起始位置
        
        // 标题居中
        Component titleComponent = Component.translatable("creative_knight_config.config.tab.mount_system");
        int titleWidth = font.width(titleComponent);
        addRenderableWidget(new StringWidget(centerX - titleWidth / 2, y, titleWidth, 20, titleComponent, font));
        y += 40; // 增加标题与内容之间的间距
        
        // 计算显示的选项
        int startIndex = currentPage * 2;
        int endIndex = Math.min(startIndex + 2, 13); // 总共有13个选项
        maxPages = (13 + 1) / 2; // 每页2个选项
        
        // 启用坐骑系统
        if (startIndex <= 0 && endIndex > 0) {
            Component labelComponent = Component.translatable("creative_knight_config.config.enable_mount_system");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            String status = ModConfig.enableMountSystem.get() ? "开启" : "关闭";
            addRenderableWidget(Button.builder(
                    Component.literal(status), 
                    button -> {
                        boolean newValue = !ModConfig.enableMountSystem.get();
                        ModConfig.enableMountSystem.set(newValue);
                        ModConfig.SPEC.save();
                        // 重新初始化界面以反映变化
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 僵尸坐骑生成概率
        if (startIndex <= 1 && endIndex > 1) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombieMountChance.get())), 
                    button -> {
                        double current = ModConfig.zombieMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombieMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 僵尸村民坐骑生成概率
        if (startIndex <= 2 && endIndex > 2) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_villager_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombieVillagerMountChance.get())), 
                    button -> {
                        double current = ModConfig.zombieVillagerMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombieVillagerMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 僵尸猪灵坐骑生成概率
        if (startIndex <= 3 && endIndex > 3) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombified_piglin_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.zombifiedPiglinMountChance.get())), 
                    button -> {
                        double current = ModConfig.zombifiedPiglinMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.zombifiedPiglinMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 骷髅坐骑生成概率
        if (startIndex <= 4 && endIndex > 4) {
            Component labelComponent = Component.translatable("creative_knight_config.config.skeleton_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.skeletonMountChance.get())), 
                    button -> {
                        double current = ModConfig.skeletonMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.skeletonMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 凋灵骷髅坐骑生成概率
        if (startIndex <= 5 && endIndex > 5) {
            Component labelComponent = Component.translatable("creative_knight_config.config.wither_skeleton_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.witherSkeletonMountChance.get())), 
                    button -> {
                        double current = ModConfig.witherSkeletonMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.witherSkeletonMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 卫道士坐骑生成概率
        if (startIndex <= 6 && endIndex > 6) {
            Component labelComponent = Component.translatable("creative_knight_config.config.vindicator_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.vindicatorMountChance.get())), 
                    button -> {
                        double current = ModConfig.vindicatorMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.vindicatorMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 唤魔者坐骑生成概率
        if (startIndex <= 7 && endIndex > 7) {
            Component labelComponent = Component.translatable("creative_knight_config.config.evoker_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.evokerMountChance.get())), 
                    button -> {
                        double current = ModConfig.evokerMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.evokerMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 女巫坐骑生成概率
        if (startIndex <= 8 && endIndex > 8) {
            Component labelComponent = Component.translatable("creative_knight_config.config.witch_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.witchMountChance.get())), 
                    button -> {
                        double current = ModConfig.witchMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.witchMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 尸壳坐骑生成概率
        if (startIndex <= 9 && endIndex > 9) {
            Component labelComponent = Component.translatable("creative_knight_config.config.husk_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.huskMountChance.get())), 
                    button -> {
                        double current = ModConfig.huskMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.huskMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 掠夺者坐骑生成概率
        if (startIndex <= 10 && endIndex > 10) {
            Component labelComponent = Component.translatable("creative_knight_config.config.pillager_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.pillagerMountChance.get())), 
                    button -> {
                        double current = ModConfig.pillagerMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.pillagerMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 猪灵坐骑生成概率
        if (startIndex <= 11 && endIndex > 11) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.piglinMountChance.get())), 
                    button -> {
                        double current = ModConfig.piglinMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.piglinMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        
        // 猪灵蛮兵坐骑生成概率
        if (startIndex <= 12 && endIndex > 12) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_brute_mount_chance");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            addRenderableWidget(Button.builder(
                    Component.literal(String.format("%.2f", ModConfig.piglinBruteMountChance.get())), 
                    button -> {
                        double current = ModConfig.piglinBruteMountChance.get();
                        double newValue = (current + 0.05) % 1.05;
                        if (newValue < 0.01) newValue = 0.0;
                        ModConfig.piglinBruteMountChance.set(newValue);
                        ModConfig.SPEC.save();
                        init();
                    }
            ).bounds(centerX + 10, y, 80, 20).build());
            y += 40;
        }
        

        
        // 添加分页和返回按钮
        int contentHeight = y - 60;
        addNavigationButtons(contentHeight);
    }

    private void showWeaponsTab() {
        int centerX = width / 2;
        int y = 60; // 固定起始位置
        
        // 标题居中
        Component titleComponent = Component.translatable("creative_knight_config.config.tab.weapons");
        int titleWidth = font.width(titleComponent);
        addRenderableWidget(new StringWidget(centerX - titleWidth / 2, y, titleWidth, 20, titleComponent, font));
        y += 40; // 增加标题与内容之间的间距
        
        // 计算显示的选项
        int startIndex = currentPage * 2;
        int endIndex = Math.min(startIndex + 2, 12); // 总共有12个选项
        maxPages = (12 + 1) / 2; // 每页2个选项
        
        // 僵尸骑士武器
        if (startIndex <= 0 && endIndex > 0) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombieWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombieWeaponBox.setValue(ModConfig.zombieWeapon.get());
            zombieWeaponBox.setResponder(value -> {
                ModConfig.zombieWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombieWeaponBox);
            y += 40;
        }
        
        // 骷髅骑士武器
        if (startIndex <= 1 && endIndex > 1) {
            Component labelComponent = Component.translatable("creative_knight_config.config.skeleton_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox skeletonWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            skeletonWeaponBox.setValue(ModConfig.skeletonWeapon.get());
            skeletonWeaponBox.setResponder(value -> {
                ModConfig.skeletonWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(skeletonWeaponBox);
            y += 40;
        }
        
        // 掠夺者骑士武器
        if (startIndex <= 2 && endIndex > 2) {
            Component labelComponent = Component.translatable("creative_knight_config.config.pillager_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox pillagerWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            pillagerWeaponBox.setValue(ModConfig.pillagerWeapon.get());
            pillagerWeaponBox.setResponder(value -> {
                ModConfig.pillagerWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(pillagerWeaponBox);
            y += 40;
        }
        
        // 猪灵骑士武器
        if (startIndex <= 3 && endIndex > 3) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox piglinWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            piglinWeaponBox.setValue(ModConfig.piglinWeapon.get());
            piglinWeaponBox.setResponder(value -> {
                ModConfig.piglinWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(piglinWeaponBox);
            y += 40;
        }
        
        // 女巫骑士武器
        if (startIndex <= 4 && endIndex > 4) {
            Component labelComponent = Component.translatable("creative_knight_config.config.witch_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox witchWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            witchWeaponBox.setValue(ModConfig.witchWeapon.get());
            witchWeaponBox.setResponder(value -> {
                ModConfig.witchWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(witchWeaponBox);
            y += 40;
        }
        
        // 僵尸村民骑士武器
        if (startIndex <= 5 && endIndex > 5) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_villager_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombieVillagerWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombieVillagerWeaponBox.setValue(ModConfig.zombieVillagerWeapon.get());
            zombieVillagerWeaponBox.setResponder(value -> {
                ModConfig.zombieVillagerWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombieVillagerWeaponBox);
            y += 40;
        }
        
        // 僵尸猪灵骑士武器
        if (startIndex <= 6 && endIndex > 6) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombified_piglin_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombifiedPiglinWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombifiedPiglinWeaponBox.setValue(ModConfig.zombifiedPiglinWeapon.get());
            zombifiedPiglinWeaponBox.setResponder(value -> {
                ModConfig.zombifiedPiglinWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombifiedPiglinWeaponBox);
            y += 40;
        }
        
        // 凋灵骷髅骑士武器
        if (startIndex <= 7 && endIndex > 7) {
            Component labelComponent = Component.translatable("creative_knight_config.config.wither_skeleton_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox witherSkeletonWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            witherSkeletonWeaponBox.setValue(ModConfig.witherSkeletonWeapon.get());
            witherSkeletonWeaponBox.setResponder(value -> {
                ModConfig.witherSkeletonWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(witherSkeletonWeaponBox);
            y += 40;
        }
        
        // 卫道士骑士武器
        if (startIndex <= 8 && endIndex > 8) {
            Component labelComponent = Component.translatable("creative_knight_config.config.vindicator_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox vindicatorWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            vindicatorWeaponBox.setValue(ModConfig.vindicatorWeapon.get());
            vindicatorWeaponBox.setResponder(value -> {
                ModConfig.vindicatorWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(vindicatorWeaponBox);
            y += 40;
        }
        
        // 唤魔者骑士武器
        if (startIndex <= 9 && endIndex > 9) {
            Component labelComponent = Component.translatable("creative_knight_config.config.evoker_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox evokerWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            evokerWeaponBox.setValue(ModConfig.evokerWeapon.get());
            evokerWeaponBox.setResponder(value -> {
                ModConfig.evokerWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(evokerWeaponBox);
            y += 40;
        }
        
        // 尸壳骑士武器
        if (startIndex <= 10 && endIndex > 10) {
            Component labelComponent = Component.translatable("creative_knight_config.config.husk_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox huskWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            huskWeaponBox.setValue(ModConfig.huskWeapon.get());
            huskWeaponBox.setResponder(value -> {
                ModConfig.huskWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(huskWeaponBox);
            y += 40;
        }
        
        // 猪灵蛮兵骑士武器
        if (startIndex <= 11 && endIndex > 11) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_brute_weapon");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox piglinBruteWeaponBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            piglinBruteWeaponBox.setValue(ModConfig.piglinBruteWeapon.get());
            piglinBruteWeaponBox.setResponder(value -> {
                ModConfig.piglinBruteWeapon.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(piglinBruteWeaponBox);
            y += 40;
        }
        
        // 添加分页和返回按钮
        int contentHeight = y - 60;
        addNavigationButtons(contentHeight);
    }

    private void showMountsTab() {
        int centerX = width / 2;
        int y = 60; // 固定起始位置
        
        // 标题居中
        Component titleComponent = Component.translatable("creative_knight_config.config.tab.mounts");
        int titleWidth = font.width(titleComponent);
        addRenderableWidget(new StringWidget(centerX - titleWidth / 2, y, titleWidth, 20, titleComponent, font));
        y += 40; // 增加标题与内容之间的间距
        
        // 计算显示的选项
        int startIndex = currentPage * 2;
        int endIndex = Math.min(startIndex + 2, 12); // 总共有12个选项
        maxPages = (12 + 1) / 2; // 每页2个选项
        
        // 僵尸坐骑
        if (startIndex <= 0 && endIndex > 0) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombieMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombieMountBox.setValue(ModConfig.zombieMount.get());
            zombieMountBox.setResponder(value -> {
                ModConfig.zombieMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombieMountBox);
            y += 40;
        }
        
        // 僵尸村民坐骑
        if (startIndex <= 1 && endIndex > 1) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombie_villager_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombieVillagerMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombieVillagerMountBox.setValue(ModConfig.zombieVillagerMount.get());
            zombieVillagerMountBox.setResponder(value -> {
                ModConfig.zombieVillagerMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombieVillagerMountBox);
            y += 40;
        }
        
        // 僵尸猪灵坐骑
        if (startIndex <= 2 && endIndex > 2) {
            Component labelComponent = Component.translatable("creative_knight_config.config.zombified_piglin_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox zombifiedPiglinMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            zombifiedPiglinMountBox.setValue(ModConfig.zombifiedPiglinMount.get());
            zombifiedPiglinMountBox.setResponder(value -> {
                ModConfig.zombifiedPiglinMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(zombifiedPiglinMountBox);
            y += 40;
        }
        
        // 骷髅坐骑
        if (startIndex <= 3 && endIndex > 3) {
            Component labelComponent = Component.translatable("creative_knight_config.config.skeleton_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox skeletonMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            skeletonMountBox.setValue(ModConfig.skeletonMount.get());
            skeletonMountBox.setResponder(value -> {
                ModConfig.skeletonMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(skeletonMountBox);
            y += 40;
        }
        
        // 凋灵骷髅坐骑
        if (startIndex <= 4 && endIndex > 4) {
            Component labelComponent = Component.translatable("creative_knight_config.config.wither_skeleton_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox witherSkeletonMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            witherSkeletonMountBox.setValue(ModConfig.witherSkeletonMount.get());
            witherSkeletonMountBox.setResponder(value -> {
                ModConfig.witherSkeletonMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(witherSkeletonMountBox);
            y += 40;
        }
        
        // 卫道士坐骑
        if (startIndex <= 5 && endIndex > 5) {
            Component labelComponent = Component.translatable("creative_knight_config.config.vindicator_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox vindicatorMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            vindicatorMountBox.setValue(ModConfig.vindicatorMount.get());
            vindicatorMountBox.setResponder(value -> {
                ModConfig.vindicatorMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(vindicatorMountBox);
            y += 40;
        }
        
        // 唤魔者坐骑
        if (startIndex <= 6 && endIndex > 6) {
            Component labelComponent = Component.translatable("creative_knight_config.config.evoker_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox evokerMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            evokerMountBox.setValue(ModConfig.evokerMount.get());
            evokerMountBox.setResponder(value -> {
                ModConfig.evokerMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(evokerMountBox);
            y += 40;
        }
        
        // 女巫坐骑
        if (startIndex <= 7 && endIndex > 7) {
            Component labelComponent = Component.translatable("creative_knight_config.config.witch_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox witchMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            witchMountBox.setValue(ModConfig.witchMount.get());
            witchMountBox.setResponder(value -> {
                ModConfig.witchMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(witchMountBox);
            y += 40;
        }
        
        // 尸壳坐骑
        if (startIndex <= 8 && endIndex > 8) {
            Component labelComponent = Component.translatable("creative_knight_config.config.husk_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox huskMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            huskMountBox.setValue(ModConfig.huskMount.get());
            huskMountBox.setResponder(value -> {
                ModConfig.huskMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(huskMountBox);
            y += 40;
        }
        
        // 掠夺者坐骑
        if (startIndex <= 9 && endIndex > 9) {
            Component labelComponent = Component.translatable("creative_knight_config.config.pillager_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox pillagerMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            pillagerMountBox.setValue(ModConfig.pillagerMount.get());
            pillagerMountBox.setResponder(value -> {
                ModConfig.pillagerMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(pillagerMountBox);
            y += 40;
        }
        
        // 猪灵坐骑
        if (startIndex <= 10 && endIndex > 10) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox piglinMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            piglinMountBox.setValue(ModConfig.piglinMount.get());
            piglinMountBox.setResponder(value -> {
                ModConfig.piglinMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(piglinMountBox);
            y += 40;
        }
        
        // 猪灵蛮兵坐骑
        if (startIndex <= 11 && endIndex > 11) {
            Component labelComponent = Component.translatable("creative_knight_config.config.piglin_brute_mount");
            int labelWidth = font.width(labelComponent);
            addRenderableWidget(new StringWidget(centerX - labelWidth - 10, y, labelWidth, 20, labelComponent, font));
            EditBox piglinBruteMountBox = new EditBox(font, centerX + 10, y, 130, 20, Component.literal(""));
            piglinBruteMountBox.setValue(ModConfig.piglinBruteMount.get());
            piglinBruteMountBox.setResponder(value -> {
                ModConfig.piglinBruteMount.set(value);
                ModConfig.SPEC.save();
            });
            addRenderableWidget(piglinBruteMountBox);
            y += 40;
        }
        
        // 添加分页和返回按钮
        int contentHeight = y - 60;
        addNavigationButtons(contentHeight);
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
