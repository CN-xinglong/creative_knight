package com.creativeknight.event;

import com.creativeknight.config.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = "creative_knight_config")
public class KnightEventHandler {
    private static final Random RANDOM = new Random();
    private static Set<ResourceLocation> customCreatures = new HashSet<>();
    private static Map<ResourceLocation, CustomCreatureConfig> customCreatureConfigs = new HashMap<>();
    
    static {
        // 解析自定义生物列表和配置
        parseCustomCreatures();
        parseCustomCreaturesConfig();
    }
    
    private static void parseCustomCreatures() {
        String customCreaturesStr = ModConfig.customCreatures.get();
        if (!customCreaturesStr.isEmpty()) {
            String[] creatureIds = customCreaturesStr.split(",");
            for (String id : creatureIds) {
                id = id.trim();
                ResourceLocation rl = ResourceLocation.tryParse(id);
                if (rl != null) {
                    customCreatures.add(rl);
                    // 如果没有配置，添加默认配置
                    if (!customCreatureConfigs.containsKey(rl)) {
                        customCreatureConfigs.put(rl, new CustomCreatureConfig(0.2, "minecraft:iron_sword", "undead"));
                    }
                }
            }
        }
    }
    
    private static void parseCustomCreaturesConfig() {
        String customCreaturesConfigStr = ModConfig.customCreaturesConfig.get();
        if (!customCreaturesConfigStr.isEmpty()) {
            String[] creatureConfigs = customCreaturesConfigStr.split(",");
            for (String configStr : creatureConfigs) {
                configStr = configStr.trim();
                String[] parts = configStr.split(":");
                if (parts.length >= 5) {
                    String modid = parts[0];
                    String entityName = parts[1];
                    double spawnChance = Double.parseDouble(parts[2]);
                    String weapon = parts[3];
                    String mountFaction = parts[4];
                    
                    ResourceLocation rl = new ResourceLocation(modid, entityName);
                    customCreatureConfigs.put(rl, new CustomCreatureConfig(spawnChance, weapon, mountFaction));
                    customCreatures.add(rl);
                }
            }
        }
    }
    
    private static class CustomCreatureConfig {
        public final double spawnChance;
        public final String weapon;
        public final String mountFaction;
        
        public CustomCreatureConfig(double spawnChance, String weapon, String mountFaction) {
            this.spawnChance = spawnChance;
            this.weapon = weapon;
            this.mountFaction = mountFaction;
        }
    }
    
    private static boolean isCustomCreature(LivingEntity entity) {
        ResourceLocation entityTypeRL = entity.getType().builtInRegistryHolder().key().location();
        return entityTypeRL != null && customCreatures.contains(entityTypeRL);
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        Entity entity = event.getEntity();

        // 处理骑士生成和武器装备
        if (entity instanceof LivingEntity livingEntity) {
            handleKnightSystem(livingEntity);
        }
        
        // 当坐骑生成时，检查周围是否有无坐骑的骑士
        checkForRidersForMount(entity);
    }
    
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        
        LivingEntity entity = event.getEntity();
        
        // 让无坐骑的骑士主动寻找附近的坐骑
        if (isKnightType(entity) && !entity.isPassenger() && RANDOM.nextFloat() < 0.05) { // 5%的概率检查，避免性能问题
            checkMountCombination(entity);
        }
    }
    

    
    // 当坐骑生成时，检查周围是否有无坐骑的骑士
    private static void checkForRidersForMount(Entity mount) {
        // 只处理可能成为坐骑的实体
        if (!isPotentialMount(mount)) return;
        
        // 检查周围是否有无坐骑的骑士
        net.minecraft.world.level.Level level = mount.level();
        net.minecraft.core.BlockPos pos = mount.blockPosition();
        
        // 搜索周围2格内的实体（距离足够近才会组合）
        for (LivingEntity potentialRider : level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(
            pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
            pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2
        ))) {
            // 检查是否是合适的骑士且无坐骑，且坐骑未被骑乘
            if (!potentialRider.isPassenger() && !mount.isVehicle() && isSuitableRiderForMount(potentialRider, mount)) {
                // 组合成有坐骑的骑士
                potentialRider.startRiding(mount);

                // 有坐骑的骑士必定持有武器
                if (potentialRider instanceof Zombie) {
                    equipWeapon(potentialRider, ModConfig.zombieWeapon.get());
                } else if (potentialRider instanceof Skeleton) {
                    equipWeapon(potentialRider, ModConfig.skeletonWeapon.get());
                } else if (potentialRider instanceof Pillager || potentialRider instanceof Vindicator || potentialRider instanceof Evoker || potentialRider instanceof Illusioner) {
                    equipWeapon(potentialRider, ModConfig.pillagerWeapon.get());
                } else if (potentialRider instanceof Piglin || potentialRider instanceof PiglinBrute) {
                    equipWeapon(potentialRider, ModConfig.piglinWeapon.get());
                } else if (potentialRider instanceof Witch) {
                    equipWeapon(potentialRider, ModConfig.witchWeapon.get());
                }
                
                // 找到一个骑士后就停止搜索，避免多个骑士骑同一个坐骑
                break;
            }
        }
    }
    
    // 检查实体是否可能成为坐骑
    private static boolean isPotentialMount(Entity entity) {
        return entity instanceof ZombieHorse || entity instanceof SkeletonHorse || entity instanceof Spider ||
               entity instanceof Ravager || entity instanceof Wolf || entity instanceof Hoglin ||
               entity instanceof Strider || entity instanceof MagmaCube || entity instanceof Cat ||
               entity instanceof Panda;
    }
    
    // 检查实体是否是适合特定坐骑的骑士
    private static boolean isSuitableRiderForMount(LivingEntity rider, Entity mount) {
        // 移除阵营限制，任何骑士都可以骑乘任何坐骑
        return isKnightType(rider) && isPotentialMount(mount);
    }

    private static void handleKnightSystem(LivingEntity entity) {
        // 检查是否是可生成骑士的生物
        if (entity instanceof Husk) {
            handleHuskKnight((Husk) entity);
        } else if (entity instanceof ZombieVillager) {
            handleZombieVillagerKnight((ZombieVillager) entity);
        } else if (entity instanceof ZombifiedPiglin) {
            handleZombifiedPiglinKnight((ZombifiedPiglin) entity);
        } else if (entity instanceof WitherSkeleton) {
            handleWitherSkeletonKnight((WitherSkeleton) entity);
        } else if (entity instanceof Vindicator) {
            handleVindicatorKnight((Vindicator) entity);
        } else if (entity instanceof Evoker) {
            handleEvokerKnight((Evoker) entity);
        } else if (entity instanceof Witch) {
            handleWitchKnight((Witch) entity);
        } else if (entity instanceof Pillager || entity instanceof Illusioner) {
            handleIllagerKnight(entity);
        } else if (entity instanceof Piglin) {
            handlePiglinKnight((Piglin) entity);
        } else if (entity instanceof PiglinBrute) {
            handlePiglinBruteKnight((PiglinBrute) entity);
        } else if (entity instanceof Zombie) {
            handleZombieKnight((Zombie) entity);
        } else if (entity instanceof Skeleton) {
            handleSkeletonKnight((Skeleton) entity);
        } else if (isCustomCreature(entity)) {
            handleCustomCreatureKnight(entity);
        }
        
        // 检查无坐骑骑士与对应坐骑的组合
        checkMountCombination(entity);
    }
    
    // 检查实体是否是骑士类型
    private static boolean isKnightType(LivingEntity entity) {
        return entity instanceof Zombie || entity instanceof ZombieVillager || 
               entity instanceof ZombifiedPiglin || entity instanceof Skeleton || 
               entity instanceof WitherSkeleton || entity instanceof Vindicator || 
               entity instanceof Evoker || entity instanceof Illusioner || 
               entity instanceof Piglin || entity instanceof PiglinBrute || 
               entity instanceof Witch || entity instanceof Husk || 
               entity instanceof Pillager || isCustomCreature(entity);
    }
    


    private static void handleZombieKnight(Zombie zombie) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.zombieKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(zombie, "undead");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(zombie, ModConfig.zombieWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(zombie, ModConfig.zombieWeapon.get());
            }
            
            // 装备头盔
            if (RANDOM.nextFloat() < ModConfig.helmetSpawnChance.get()) {
                zombie.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
        }
    }

    private static void handleSkeletonKnight(Skeleton skeleton) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.skeletonKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(skeleton, "undead");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(skeleton, ModConfig.skeletonWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(skeleton, ModConfig.skeletonWeapon.get());
            }
            
            // 装备头盔
            if (RANDOM.nextFloat() < ModConfig.helmetSpawnChance.get()) {
                skeleton.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            }
        }
    }

    private static void handleIllagerKnight(LivingEntity illager) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.pillagerKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(illager, "illager");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(illager, ModConfig.pillagerWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(illager, ModConfig.pillagerWeapon.get());
            }
        }
    }

    private static void handlePiglinKnight(LivingEntity piglin) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.piglinKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(piglin, "piglin");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(piglin, ModConfig.piglinWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(piglin, ModConfig.piglinWeapon.get());
            }
        }
    }

    private static void handleWitchKnight(Witch witch) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.witchKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(witch, "witch");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(witch, ModConfig.witchWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(witch, ModConfig.witchWeapon.get());
            }
        }
    }
    
    private static void handleZombieVillagerKnight(ZombieVillager zombieVillager) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.zombieVillagerKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(zombieVillager, "undead");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(zombieVillager, ModConfig.zombieVillagerWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(zombieVillager, ModConfig.zombieVillagerWeapon.get());
            }
        }
    }
    
    private static void handleZombifiedPiglinKnight(ZombifiedPiglin zombifiedPiglin) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.zombifiedPiglinKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(zombifiedPiglin, "piglin");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(zombifiedPiglin, ModConfig.zombifiedPiglinWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(zombifiedPiglin, ModConfig.zombifiedPiglinWeapon.get());
            }
        }
    }
    
    private static void handleWitherSkeletonKnight(WitherSkeleton witherSkeleton) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.witherSkeletonKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(witherSkeleton, "undead");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(witherSkeleton, ModConfig.witherSkeletonWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(witherSkeleton, ModConfig.witherSkeletonWeapon.get());
            }
        }
    }
    
    private static void handleVindicatorKnight(Vindicator vindicator) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.vindicatorKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(vindicator, "illager");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(vindicator, ModConfig.vindicatorWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(vindicator, ModConfig.vindicatorWeapon.get());
            }
        }
    }
    
    private static void handleEvokerKnight(Evoker evoker) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.evokerKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(evoker, "illager");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(evoker, ModConfig.evokerWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(evoker, ModConfig.evokerWeapon.get());
            }
        }
    }
    
    private static void handleHuskKnight(Husk husk) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.huskKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(husk, "undead");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(husk, ModConfig.huskWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(husk, ModConfig.huskWeapon.get());
            }
        }
    }
    
    private static void handlePiglinBruteKnight(PiglinBrute piglinBrute) {
        // 检查是否生成骑士
        if (RANDOM.nextFloat() < ModConfig.piglinBruteKnightSpawnChance.get()) {
            boolean hasMount = false;
            // 生成坐骑
            if (ModConfig.enableMountSystem.get()) {
                hasMount = spawnMount(piglinBrute, "piglin");
            }
            
            // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
            if (hasMount) {
                equipWeapon(piglinBrute, ModConfig.piglinBruteWeapon.get());
            } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                equipWeapon(piglinBrute, ModConfig.piglinBruteWeapon.get());
            }
        }
    }
    
    private static void handleCustomCreatureKnight(LivingEntity entity) {
        ResourceLocation entityTypeRL = entity.getType().builtInRegistryHolder().key().location();
        CustomCreatureConfig config = customCreatureConfigs.get(entityTypeRL);
        
        if (config != null) {
            // 检查是否生成骑士
            if (RANDOM.nextFloat() < config.spawnChance) {
                boolean hasMount = false;
                // 生成坐骑
                if (ModConfig.enableMountSystem.get()) {
                    // 使用配置的坐骑阵营
                    hasMount = spawnMount(entity, config.mountFaction);
                }
                
                // 装备武器：有坐骑的骑士必定持有武器，无坐骑的骑士根据概率
                if (hasMount) {
                    equipWeapon(entity, config.weapon);
                } else if (RANDOM.nextFloat() < ModConfig.weaponSpawnChance.get()) {
                    equipWeapon(entity, config.weapon);
                }
            }
        }
    }
    


    private static void equipWeapon(LivingEntity entity, String weaponId) {
        Item weapon = ForgeRegistries.ITEMS.getValue(
                net.minecraft.resources.ResourceLocation.tryParse(weaponId));
        
        if (weapon != null) {
            ItemStack weaponStack = new ItemStack(weapon);
            entity.setItemSlot(EquipmentSlot.MAINHAND, weaponStack);
        }
    }

    private static boolean spawnMount(LivingEntity rider, String faction) {
        net.minecraft.world.level.Level level = rider.level();
        net.minecraft.core.BlockPos pos = rider.blockPosition();
        
        Entity mount = null;
        double mountChance = 0;
        String mountId = "";
        
        // 根据具体生物类型获取坐骑生成概率和坐骑ID
        if (rider instanceof Zombie) {
            mountChance = ModConfig.zombieMountChance.get();
            mountId = ModConfig.zombieMount.get();
        } else if (rider instanceof ZombieVillager) {
            mountChance = ModConfig.zombieVillagerMountChance.get();
            mountId = ModConfig.zombieVillagerMount.get();
        } else if (rider instanceof ZombifiedPiglin) {
            mountChance = ModConfig.zombifiedPiglinMountChance.get();
            mountId = ModConfig.zombifiedPiglinMount.get();
        } else if (rider instanceof Skeleton) {
            mountChance = ModConfig.skeletonMountChance.get();
            mountId = ModConfig.skeletonMount.get();
        } else if (rider instanceof WitherSkeleton) {
            mountChance = ModConfig.witherSkeletonMountChance.get();
            mountId = ModConfig.witherSkeletonMount.get();
        } else if (rider instanceof Vindicator) {
            mountChance = ModConfig.vindicatorMountChance.get();
            mountId = ModConfig.vindicatorMount.get();
        } else if (rider instanceof Evoker) {
            mountChance = ModConfig.evokerMountChance.get();
            mountId = ModConfig.evokerMount.get();
        } else if (rider instanceof Witch) {
            mountChance = ModConfig.witchMountChance.get();
            mountId = ModConfig.witchMount.get();
        } else if (rider instanceof Husk) {
            mountChance = ModConfig.huskMountChance.get();
            mountId = ModConfig.huskMount.get();
        } else if (rider instanceof Pillager || rider instanceof Illusioner) {
            mountChance = ModConfig.pillagerMountChance.get();
            mountId = ModConfig.pillagerMount.get();
        } else if (rider instanceof Piglin) {
            mountChance = ModConfig.piglinMountChance.get();
            mountId = ModConfig.piglinMount.get();
        } else if (rider instanceof PiglinBrute) {
            mountChance = ModConfig.piglinBruteMountChance.get();
            mountId = ModConfig.piglinBruteMount.get();
        }
        
        // 根据配置的坐骑ID生成坐骑
        if (RANDOM.nextFloat() < mountChance && !mountId.isEmpty()) {
            EntityType<?> mountType = getEntityType(mountId);
            if (mountType != null) {
                mount = mountType.create(level);
            }
        }
        
        if (mount != null) {
            mount.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(mount);
            rider.startRiding(mount);
            return true;
        }
        return false;
    }
    
    // 检查并处理无坐骑骑士与对应坐骑的组合
    private static void checkMountCombination(LivingEntity entity) {
        // 只处理无坐骑的骑士
        if (entity.isPassenger()) return;
        
        // 检查周围是否有合适的坐骑
        net.minecraft.world.level.Level level = entity.level();
        net.minecraft.core.BlockPos pos = entity.blockPosition();
        
        // 搜索周围2格内的实体（距离足够近才会组合）
        for (Entity potentialMount : level.getEntitiesOfClass(Entity.class, new net.minecraft.world.phys.AABB(
            pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2,
            pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2
        ))) {
            // 检查是否是合适的坐骑且未被骑乘
            if (!potentialMount.isVehicle() && isSuitableMount(entity, potentialMount)) {
                // 组合成有坐骑的骑士
                entity.startRiding(potentialMount);
                // 有坐骑的骑士必定持有武器
                if (entity instanceof Zombie) {
                    equipWeapon(entity, ModConfig.zombieWeapon.get());
                } else if (entity instanceof Skeleton) {
                    equipWeapon(entity, ModConfig.skeletonWeapon.get());
                } else if (entity instanceof Pillager || entity instanceof Vindicator || entity instanceof Evoker || entity instanceof Illusioner) {
                    equipWeapon(entity, ModConfig.pillagerWeapon.get());
                } else if (entity instanceof Piglin || entity instanceof PiglinBrute) {
                    equipWeapon(entity, ModConfig.piglinWeapon.get());
                } else if (entity instanceof Witch) {
                    equipWeapon(entity, ModConfig.witchWeapon.get());
                }
                
                // 找到一个坐骑后就停止搜索，避免一个骑士骑多个坐骑
                break;
            }
        }
    }
    
    // 检查实体是否是合适的坐骑
    private static boolean isSuitableMount(LivingEntity rider, Entity potentialMount) {
        // 移除阵营限制，任何骑士都可以骑乘任何坐骑
        return isKnightType(rider) && isPotentialMount(potentialMount);
    }


    


    private static EntityType<?> getEntityType(String entityId) {
        try {
            net.minecraft.resources.ResourceLocation resourceLocation = net.minecraft.resources.ResourceLocation.tryParse(entityId);
            if (resourceLocation != null) {
                return ForgeRegistries.ENTITY_TYPES.getValue(resourceLocation);
            }
        } catch (Exception e) {
            // 忽略解析错误
        }
        return null;
    }
}
