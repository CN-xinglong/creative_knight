# 自定义骑士 (Creative Knight Config)

## 模组简介

自定义骑士是一个Minecraft 1.20.1 Forge模组，允许玩家在游戏内配置各种生物的骑士行为、坐骑和武器。

## 功能特性

### 1. 游戏内配置
- 提供直观的配置界面，可通过游戏内的模组配置按钮访问
- 支持中文界面，操作简单易懂

### 2. 生物配置
- **生成概率**：可调整各种生物成为骑士的概率，骑士可以手持武器，骑乘生物
- **武器配置**：为每种生物骑士设置默认武器
- **坐骑配置**：为每种生物设置骑士默认坐骑
- **坐骑系统**：可启用/禁用坐骑系统，调整坐骑生成概率

### 3. 支持的生物
- 僵尸
- 骷髅
- 掠夺者
- 猪灵
- 女巫
- 僵尸村民
- 僵尸猪灵
- 凋灵骷髅
- 卫道士
- 唤魔者
- 尸壳
- 猪灵蛮兵
- 通过配置文件添加的自定义生物

### 4. 无阵营限制
- 移除了坐骑阵营限制，任何骑士都可以骑乘任何类型的坐骑（尽量配置同阵营，因为我也不知道乱搭配会发生什么）
- 支持更自由的坐骑组合

### 5. 物品ID复制功能
- 鼠标悬停在物品上，按下快捷键（默认I键）可复制物品的英文ID到剪贴板
- 可在游戏自带的按键绑定功能中自定义复制按键

## 使用方法

### 配置界面
1. 进入游戏后，点击主菜单的"模组"按钮
2. 找到"custom knight"模组，点击"配置"按钮
3. 在配置界面中，您可以：
   - 在"生成概率"标签页调整各种生物的骑士生成概率
   - 在"坐骑系统"标签页启用/禁用坐骑系统并调整坐骑生成概率
   - 在"武器设置"标签页为每种生物设置默认武器
   - 在"坐骑设置"标签页为每种生物设置默认坐骑
或者输入指令/creativeknight config

### 添加自定义生物(其实我也没试过)
找到配置文件 ：
配置文件位于游戏目录的 config 文件夹中，文件名为 knightmod-common.toml 。
- 修改配置文件 ：
打开配置文件后，找到以下两个配置项：

- customCreatures ：自定义生物列表
- customCreaturesConfig ：自定义生物配置
- 添加自定义生物 ：

- 格式示例 ：
  ```
  # 自定义生物列表，格式: 
  modid:entity1,modid:entity2,...
  customCreatures = 
  "minecraft:stray,
  example:custom_creature"
  
  # 自定义生物配置，格式: 
  modid:entity1:spawnChance:weapo
  n:mountFaction,
  modid:entity2:spawnChance:weapo
  n:mountFaction,...
  customCreaturesConfig = 
  "minecraft:stray:0.
  2:minecraft:bow:undead,
  example:custom_creature:0.
  3:example:custom_sword:undead"
  ```
- 配置项说明 ：

- modid:entity ：生物的完整ID，例如 minecraft:stray
- spawnChance ：骑士生成概率，范围0-1
- weapon ：武器ID，例如 minecraft:bow
- mountFaction ：坐骑阵营，可选值： undead （不死生物）、 illager （灾厄村民）、 piglin （猪灵）、 witch （女巫）
- 保存配置文件 ：
修改完成后保存配置文件，然后重启游戏即可生效。

### 示例配置
```toml
# 自定义生物列表
customCreatures = "minecraft:stray,example:custom_creature"

# 自定义生物配置
customCreaturesConfig = "minecraft:stray:0.2:minecraft:bow:undead,example:custom_creature:0.3:example:custom_sword:undead"
```

## 技术特性

- **动态配置**：配置更改立即生效，无需重启游戏
- **兼容性**：与大多数模组兼容
- **性能优化**：高效的事件处理，对游戏性能影响最小
- **可扩展性**：支持通过配置文件添加自定义生物

## 安装方法

1. 确保您的Minecraft已安装Forge 1.20.1
2. 下载本模组的JAR文件
3. 将JAR文件放入游戏目录的 `mods` 文件夹中
4. 启动游戏，享受自定义骑士的乐趣！

## 作者

暴龙战神铁柱

## 联系方式

[B站个人主页](https://account.bilibili.com/account/home?spm_id_from=333.1007.0.0)
[github](https://github.com/CN-xinglong/creative_knight)

## 版本历史

- v1.0：初始版本，实现基本功能

## 许可证

All Rights Reserved