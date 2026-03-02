package com.creativeknight.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // 骑士生成概率配置
    public static final ForgeConfigSpec.DoubleValue zombieKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue zombieVillagerKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue zombifiedPiglinKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue skeletonKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue witherSkeletonKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue vindicatorKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue evokerKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue witchKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue huskKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue pillagerKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue piglinKnightSpawnChance;
    public static final ForgeConfigSpec.DoubleValue piglinBruteKnightSpawnChance;

    // 武器概率配置
    public static final ForgeConfigSpec.DoubleValue weaponSpawnChance;
    public static final ForgeConfigSpec.DoubleValue helmetSpawnChance;
    

    
    // 自定义生物配置
    public static final ForgeConfigSpec.ConfigValue<String> customCreatures;
    public static final ForgeConfigSpec.ConfigValue<String> customCreaturesConfig;

    // 坐骑系统配置
    public static final ForgeConfigSpec.BooleanValue enableMountSystem;
    public static final ForgeConfigSpec.DoubleValue zombieMountChance;
    public static final ForgeConfigSpec.DoubleValue zombieVillagerMountChance;
    public static final ForgeConfigSpec.DoubleValue zombifiedPiglinMountChance;
    public static final ForgeConfigSpec.DoubleValue skeletonMountChance;
    public static final ForgeConfigSpec.DoubleValue witherSkeletonMountChance;
    public static final ForgeConfigSpec.DoubleValue vindicatorMountChance;
    public static final ForgeConfigSpec.DoubleValue evokerMountChance;
    public static final ForgeConfigSpec.DoubleValue witchMountChance;
    public static final ForgeConfigSpec.DoubleValue huskMountChance;
    public static final ForgeConfigSpec.DoubleValue pillagerMountChance;
    public static final ForgeConfigSpec.DoubleValue piglinMountChance;
    public static final ForgeConfigSpec.DoubleValue piglinBruteMountChance;

    // 武器选择配置
    public static final ForgeConfigSpec.ConfigValue<String> zombieWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> zombieVillagerWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> zombifiedPiglinWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> skeletonWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> witherSkeletonWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> vindicatorWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> evokerWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> witchWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> huskWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> pillagerWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> piglinWeapon;
    public static final ForgeConfigSpec.ConfigValue<String> piglinBruteWeapon;

    // 坐骑选择配置
    public static final ForgeConfigSpec.ConfigValue<String> zombieMount;
    public static final ForgeConfigSpec.ConfigValue<String> zombieVillagerMount;
    public static final ForgeConfigSpec.ConfigValue<String> zombifiedPiglinMount;
    public static final ForgeConfigSpec.ConfigValue<String> skeletonMount;
    public static final ForgeConfigSpec.ConfigValue<String> witherSkeletonMount;
    public static final ForgeConfigSpec.ConfigValue<String> vindicatorMount;
    public static final ForgeConfigSpec.ConfigValue<String> evokerMount;
    public static final ForgeConfigSpec.ConfigValue<String> witchMount;
    public static final ForgeConfigSpec.ConfigValue<String> huskMount;
    public static final ForgeConfigSpec.ConfigValue<String> pillagerMount;
    public static final ForgeConfigSpec.ConfigValue<String> piglinMount;
    public static final ForgeConfigSpec.ConfigValue<String> piglinBruteMount;

    static {
        BUILDER.push("Creative Knight Config Settings");

        // 骑士生成概率配置
        BUILDER.push("Spawn Chances");
        zombieKnightSpawnChance = BUILDER
                .comment("僵尸骑士生成概率")
                .defineInRange("zombieKnightSpawnChance", 0.3, 0.0, 1.0);
        zombieVillagerKnightSpawnChance = BUILDER
                .comment("僵尸村民骑士生成概率")
                .defineInRange("zombieVillagerKnightSpawnChance", 0.2, 0.0, 1.0);
        zombifiedPiglinKnightSpawnChance = BUILDER
                .comment("僵尸猪灵骑士生成概率")
                .defineInRange("zombifiedPiglinKnightSpawnChance", 0.2, 0.0, 1.0);
        skeletonKnightSpawnChance = BUILDER
                .comment("骷髅骑士生成概率")
                .defineInRange("skeletonKnightSpawnChance", 0.25, 0.0, 1.0);
        witherSkeletonKnightSpawnChance = BUILDER
                .comment("凋灵骷髅骑士生成概率")
                .defineInRange("witherSkeletonKnightSpawnChance", 0.15, 0.0, 1.0);
        vindicatorKnightSpawnChance = BUILDER
                .comment("卫道士骑士生成概率")
                .defineInRange("vindicatorKnightSpawnChance", 0.15, 0.0, 1.0);
        evokerKnightSpawnChance = BUILDER
                .comment("唤魔者骑士生成概率")
                .defineInRange("evokerKnightSpawnChance", 0.1, 0.0, 1.0);
        witchKnightSpawnChance = BUILDER
                .comment("女巫骑士生成概率")
                .defineInRange("witchKnightSpawnChance", 0.1, 0.0, 1.0);
        huskKnightSpawnChance = BUILDER
                .comment("尸壳骑士生成概率")
                .defineInRange("huskKnightSpawnChance", 0.2, 0.0, 1.0);
        pillagerKnightSpawnChance = BUILDER
                .comment("掠夺者骑士生成概率")
                .defineInRange("pillagerKnightSpawnChance", 0.2, 0.0, 1.0);
        piglinKnightSpawnChance = BUILDER
                .comment("猪灵骑士生成概率")
                .defineInRange("piglinKnightSpawnChance", 0.15, 0.0, 1.0);
        piglinBruteKnightSpawnChance = BUILDER
                .comment("猪灵蛮兵骑士生成概率")
                .defineInRange("piglinBruteKnightSpawnChance", 0.1, 0.0, 1.0);
        weaponSpawnChance = BUILDER
                .comment("武器生成概率")
                .defineInRange("weaponSpawnChance", 0.8, 0.0, 1.0);
        helmetSpawnChance = BUILDER
                .comment("头盔生成概率")
                .defineInRange("helmetSpawnChance", 0.5, 0.0, 1.0);
        customCreatures = BUILDER
                .comment("自定义生物列表，格式: modid:entity1,modid:entity2,...")
                .define("customCreatures", "");
        customCreaturesConfig = BUILDER
                .comment("自定义生物配置，格式: modid:entity1:spawnChance:weapon:mountFaction,modid:entity2:spawnChance:weapon:mountFaction,...")
                .define("customCreaturesConfig", "");
        BUILDER.pop();

        // 坐骑系统配置
        BUILDER.push("Mount System");
        enableMountSystem = BUILDER
                .comment("是否启用坐骑系统")
                .define("enableMountSystem", true);
        zombieMountChance = BUILDER
                .comment("僵尸坐骑生成概率")
                .defineInRange("zombieMountChance", 0.3, 0.0, 1.0);
        zombieVillagerMountChance = BUILDER
                .comment("僵尸村民坐骑生成概率")
                .defineInRange("zombieVillagerMountChance", 0.25, 0.0, 1.0);
        zombifiedPiglinMountChance = BUILDER
                .comment("僵尸猪灵坐骑生成概率")
                .defineInRange("zombifiedPiglinMountChance", 0.25, 0.0, 1.0);
        skeletonMountChance = BUILDER
                .comment("骷髅坐骑生成概率")
                .defineInRange("skeletonMountChance", 0.3, 0.0, 1.0);
        witherSkeletonMountChance = BUILDER
                .comment("凋灵骷髅坐骑生成概率")
                .defineInRange("witherSkeletonMountChance", 0.25, 0.0, 1.0);
        vindicatorMountChance = BUILDER
                .comment("卫道士坐骑生成概率")
                .defineInRange("vindicatorMountChance", 0.35, 0.0, 1.0);
        evokerMountChance = BUILDER
                .comment("唤魔者坐骑生成概率")
                .defineInRange("evokerMountChance", 0.3, 0.0, 1.0);
        witchMountChance = BUILDER
                .comment("女巫坐骑生成概率")
                .defineInRange("witchMountChance", 0.25, 0.0, 1.0);
        huskMountChance = BUILDER
                .comment("尸壳坐骑生成概率")
                .defineInRange("huskMountChance", 0.25, 0.0, 1.0);
        pillagerMountChance = BUILDER
                .comment("掠夺者坐骑生成概率")
                .defineInRange("pillagerMountChance", 0.35, 0.0, 1.0);
        piglinMountChance = BUILDER
                .comment("猪灵坐骑生成概率")
                .defineInRange("piglinMountChance", 0.3, 0.0, 1.0);
        piglinBruteMountChance = BUILDER
                .comment("猪灵蛮兵坐骑生成概率")
                .defineInRange("piglinBruteMountChance", 0.25, 0.0, 1.0);
        BUILDER.pop();

        // 武器选择配置
        BUILDER.push("Weapon Selection");
        zombieWeapon = BUILDER
                .comment("僵尸骑士武器 (格式: modid:item)")
                .define("zombieWeapon", "basicweapons:iron_spear");
        zombieVillagerWeapon = BUILDER
                .comment("僵尸村民骑士武器 (格式: modid:item)")
                .define("zombieVillagerWeapon", "minecraft:iron_axe");
        zombifiedPiglinWeapon = BUILDER
                .comment("僵尸猪灵骑士武器 (格式: modid:item)")
                .define("zombifiedPiglinWeapon", "minecraft:golden_sword");
        skeletonWeapon = BUILDER
                .comment("骷髅骑士武器 (格式: modid:item)")
                .define("skeletonWeapon", "minecraft:bow");
        witherSkeletonWeapon = BUILDER
                .comment("凋灵骷髅骑士武器 (格式: modid:item)")
                .define("witherSkeletonWeapon", "minecraft:stone_sword");
        vindicatorWeapon = BUILDER
                .comment("卫道士骑士武器 (格式: modid:item)")
                .define("vindicatorWeapon", "minecraft:iron_axe");
        evokerWeapon = BUILDER
                .comment("唤魔者骑士武器 (格式: modid:item)")
                .define("evokerWeapon", "minecraft:stick");
        witchWeapon = BUILDER
                .comment("女巫骑士武器 (格式: modid:item)")
                .define("witchWeapon", "minecraft:stick");
        huskWeapon = BUILDER
                .comment("尸壳骑士武器 (格式: modid:item)")
                .define("huskWeapon", "minecraft:iron_sword");
        pillagerWeapon = BUILDER
                .comment("掠夺者骑士武器 (格式: modid:item)")
                .define("pillagerWeapon", "minecraft:crossbow");
        piglinWeapon = BUILDER
                .comment("猪灵骑士武器 (格式: modid:item)")
                .define("piglinWeapon", "minecraft:golden_sword");
        piglinBruteWeapon = BUILDER
                .comment("猪灵蛮兵骑士武器 (格式: modid:item)")
                .define("piglinBruteWeapon", "minecraft:golden_axe");
        BUILDER.pop();

        // 坐骑选择配置
        BUILDER.push("Mount Selection");
        zombieMount = BUILDER
                .comment("僵尸坐骑 (格式: modid:entity)")
                .define("zombieMount", "minecraft:zombie_horse");
        zombieVillagerMount = BUILDER
                .comment("僵尸村民坐骑 (格式: modid:entity)")
                .define("zombieVillagerMount", "minecraft:zombie_horse");
        zombifiedPiglinMount = BUILDER
                .comment("僵尸猪灵坐骑 (格式: modid:entity)")
                .define("zombifiedPiglinMount", "minecraft:hoglin");
        skeletonMount = BUILDER
                .comment("骷髅坐骑 (格式: modid:entity)")
                .define("skeletonMount", "minecraft:skeleton_horse");
        witherSkeletonMount = BUILDER
                .comment("凋灵骷髅坐骑 (格式: modid:entity)")
                .define("witherSkeletonMount", "minecraft:skeleton_horse");
        vindicatorMount = BUILDER
                .comment("卫道士坐骑 (格式: modid:entity)")
                .define("vindicatorMount", "minecraft:ravager");
        evokerMount = BUILDER
                .comment("唤魔者坐骑 (格式: modid:entity)")
                .define("evokerMount", "minecraft:ravager");
        witchMount = BUILDER
                .comment("女巫坐骑 (格式: modid:entity)")
                .define("witchMount", "minecraft:cat");
        huskMount = BUILDER
                .comment("尸壳坐骑 (格式: modid:entity)")
                .define("huskMount", "minecraft:zombie_horse");
        pillagerMount = BUILDER
                .comment("掠夺者坐骑 (格式: modid:entity)")
                .define("pillagerMount", "minecraft:ravager");
        piglinMount = BUILDER
                .comment("猪灵坐骑 (格式: modid:entity)")
                .define("piglinMount", "minecraft:hoglin");
        piglinBruteMount = BUILDER
                .comment("猪灵蛮兵坐骑 (格式: modid:entity)")
                .define("piglinBruteMount", "minecraft:hoglin");
        BUILDER.pop();

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static final ForgeConfigSpec COMMON_CONFIG = SPEC;
}
