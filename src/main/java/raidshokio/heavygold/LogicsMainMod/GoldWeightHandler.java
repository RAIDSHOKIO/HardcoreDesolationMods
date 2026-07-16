package raidshokio.hardcoredesolation;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

public class GoldWeightHandler
{
    HashMap<UUID, PlayerFlags> PlayerFlagsMap = new HashMap<> ();

    private static class PlayerFlags
    {
        boolean Gold_low_messageSent = false;
        boolean Gold_medium_messageSent = false;
        boolean Gold_high_messageSent = false;
        
        boolean Iron_low_messageSent = false;
        boolean Iron_medium_messageSent = false;
        boolean Iron_high_messageSent = false;

        boolean Coal_low_messageSent = false;
        boolean Coal_medium_messageSent = false;
        boolean Coal_high_messageSent = false;
    }

    public void handle(ServerPlayer player)
    {

        UUID playerUUID = player.getUUID();

        PlayerFlags flags = PlayerFlagsMap.get(playerUUID);

        if (flags == null) {
            flags = new PlayerFlags();
            PlayerFlagsMap.put(playerUUID, flags);
        }

        // Подсчёт предметов в инвентаре
        int nuggets_Count = player.getInventory().countItem(Items.GOLD_NUGGET);
        int gold_Count = player.getInventory().countItem(Items.GOLD_INGOT);
        int blocks_Count = player.getInventory().countItem(Items.GOLD_BLOCK);

        int iron_nuggets_Count = player.getInventory().countItem(Items.IRON_NUGGET);
        int iron_Count = player.getInventory().countItem(Items.IRON_INGOT);
        int iron_blocks_Count = player.getInventory().countItem(Items.IRON_BLOCK);

        int coal_Count = player.getInventory().countItem(Items.COAL);
        int charcoal_Count = player.getInventory().countItem(Items.CHARCOAL);
        int coalBlocks_Count = player.getInventory().countItem(Items.COAL_BLOCK);

        // Общий вес: самородок = 1, слиток = 9, блок = 81
        int totalWeight_GOLD = (nuggets_Count * 1) + (gold_Count * 9) + (blocks_Count * 81);

        int totalWeight_IRON = (iron_nuggets_Count * 1) + (iron_Count * 9) + (iron_blocks_Count * 81);

        int totalWeight_COAL = (coal_Count * 1) + (charcoal_Count * 1) + (coalBlocks_Count * 81);

        if(totalWeight_COAL > 0)
        {
            int amplifier = totalWeight_COAL * 5 / 2592;

            // ------------------ ЭФФЕКТ ЗАМЕДЛЕНИЯ ------------------
            // Замедление включается при весе >= 9 (один слиток) и работает всегда,
            // независимо от сообщений. Сила замедления: amplifier = вес * 5 / 2592,
            // что при 32 блоках (2592) даёт amplifier = 5 (VI уровень), и дальше не растёт.

            if (totalWeight_COAL >= 9)
            {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));
            }

            // ------------------ СООБЩЕНИЯ О НАГРУЗКЕ ------------------
            // Проверка идёт от большего порога к меньшему, чтобы при достижении
            // высокого порога не срабатывали низкие. Сообщения отправляются только
            // один раз за период ношения золота (до полной разгрузки).

            // Максимальная нагрузка (вес >= 2592)
            if(totalWeight_COAL >= 2592 && !flags.Coal_high_messageSent)
            {
                flags.Coal_high_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: максимум "));
            }

            // Средняя нагрузка (вес >= 1296) – только если максимум ещё не достигнут
            else if(totalWeight_COAL >= 1296 && !flags.Coal_medium_messageSent)
            {
                flags.Coal_medium_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: среднее"));
            }

            // Низкая нагрузка (вес >= 27) – только если более высокие пороги не достигнуты
            else if(totalWeight_COAL >= 27 && !flags.Coal_low_messageSent)
            {
                flags.Coal_low_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: низкая"));
            }
        }

        if(totalWeight_COAL < 27)
        {
            flags.Coal_high_messageSent = false;
            flags.Coal_medium_messageSent = false;
            flags.Coal_low_messageSent = false;
        }

        if(totalWeight_IRON > 0)
        {
            int amplifier = totalWeight_IRON * 5 / 2592;

            // ------------------ ЭФФЕКТ ЗАМЕДЛЕНИЯ ------------------
            // Замедление включается при весе >= 9 (один слиток) и работает всегда,
            // независимо от сообщений. Сила замедления: amplifier = вес * 5 / 2592,
            // что при 32 блоках (2592) даёт amplifier = 5 (VI уровень), и дальше не растёт.

            if (totalWeight_IRON >= 9)
            {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));
            }

            // ------------------ СООБЩЕНИЯ О НАГРУЗКЕ ------------------
            // Проверка идёт от большего порога к меньшему, чтобы при достижении
            // высокого порога не срабатывали низкие. Сообщения отправляются только
            // один раз за период ношения золота (до полной разгрузки).

            // Максимальная нагрузка (вес >= 2592)
            if (totalWeight_IRON >= 2592 && !flags.Iron_high_messageSent)
            {
                flags.Iron_high_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: максимум"));
            }
            // Средняя нагрузка (вес >= 1296) – только если максимум ещё не достигнут
            else if (totalWeight_IRON >= 1296 && !flags.Iron_medium_messageSent)
            {
                flags.Iron_medium_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: средние"));
            }
            // Низкая нагрузка (вес >= 27) – только если более высокие пороги не достигнуты
            else if (totalWeight_IRON >= 27 && !flags.Iron_low_messageSent)
            {
                flags.Iron_low_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: низкая"));
            }

            // ------------------ СБРОС ФЛАГОВ ------------------
            // Если вес упал ниже 27 (игрок почти разгрузился), сбрасываем все флаги,
            // чтобы при следующем наборе золота сообщения отправились заново.
        }

        if (totalWeight_IRON < 27)
        {
            flags.Iron_high_messageSent = false;
            flags.Iron_medium_messageSent = false;
            flags.Iron_low_messageSent = false;
        }

        if(totalWeight_GOLD > 0)
        {
            int amplifier = totalWeight_GOLD * 5 / 2592;

            // ------------------ ЭФФЕКТ ЗАМЕДЛЕНИЯ ------------------
            // Замедление включается при весе >= 9 (один слиток) и работает всегда,
            // независимо от сообщений. Сила замедления: amplifier = вес * 5 / 2592,
            // что при 32 блоках (2592) даёт amplifier = 5 (VI уровень), и дальше не растёт.

            if (totalWeight_GOLD >= 9)
            {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));
            }

            // ------------------ СООБЩЕНИЯ О НАГРУЗКЕ ------------------
            // Проверка идёт от большего порога к меньшему, чтобы при достижении
            // высокого порога не срабатывали низкие. Сообщения отправляются только
            // один раз за период ношения золота (до полной разгрузки).

            // Максимальная нагрузка (вес >= 2592)
            if (totalWeight_GOLD >= 2592 && !flags.Gold_high_messageSent)
            {
                flags.Gold_high_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: максимум"));
            }
            // Средняя нагрузка (вес >= 1296) – только если максимум ещё не достигнут
            else if (totalWeight_GOLD >= 1296 && !flags.Gold_medium_messageSent)
            {
                flags.Gold_medium_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: средние"));
            }
            // Низкая нагрузка (вес >= 27) – только если более высокие пороги не достигнуты
            else if (totalWeight_GOLD >= 27 && !flags.Gold_low_messageSent)
            {
                flags.Gold_low_messageSent = true;

                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN, 25, amplifier, false, false, true));

                player.sendSystemMessage(Component.literal("Нагрузка на тела: низкая"));
            }

            // ------------------ СБРОС ФЛАГОВ ------------------
            // Если вес упал ниже 27 (игрок почти разгрузился), сбрасываем все флаги,
            // чтобы при следующем наборе золота сообщения отправились заново.
        }

        if (totalWeight_GOLD < 27)
        {
            flags.Gold_high_messageSent = false;
            flags.Gold_medium_messageSent = false;
            flags.Gold_low_messageSent = false;
        }
    }
}
