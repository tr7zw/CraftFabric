package io.github.craftfabric.craftfabric.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Preconditions;

import io.github.craftfabric.craftfabric.AbstractServerImpl;
import io.github.craftfabric.craftfabric.link.CraftLink;
import io.github.craftfabric.craftfabric.world.CraftWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public abstract class CraftEntity implements org.bukkit.entity.Entity {

    protected final AbstractServerImpl server;
    protected Entity handle;

    public CraftEntity(final AbstractServerImpl server, final Entity entity) {
        this.server = server;
        this.handle = entity;
    }

    public Entity getHandle() {
        return handle;
    }

    public void setHandle(final Entity entity) {
        this.handle = entity;
    }

    public static CraftEntity getEntity(AbstractServerImpl server, Entity entity) {
        /* TODO
        if (entity instanceof LivingEntity) {
            // Players
            if (entity instanceof EntityHuman) {
                if (entity instanceof PlayerEntity) {
                    return new CraftPlayer(server, (PlayerEntity) entity);
                } else {
                    return new CraftHumanEntity(server, (EntityHuman) entity);
                }
            }
            // Water Animals
            else if (entity instanceof EntityWaterAnimal) {
                if (entity instanceof EntitySquid) {
                    return new CraftSquid(server, (EntitySquid) entity);
                } else if (entity instanceof EntityFish) {
                    if (entity instanceof EntityCod) {
                        return new CraftCod(server, (EntityCod) entity);
                    } else if (entity instanceof EntityPufferFish) {
                        return new CraftPufferFish(server, (EntityPufferFish) entity);
                    } else if (entity instanceof EntitySalmon) {
                        return new CraftSalmon(server, (EntitySalmon) entity);
                    } else if (entity instanceof EntityTropicalFish) {
                        return new CraftTropicalFish(server, (EntityTropicalFish) entity);
                    } else {
                        return new CraftFish(server, (EntityFish) entity);
                    }
                } else if (entity instanceof EntityDolphin) {
                    return new CraftDolphin(server, (EntityDolphin) entity);
                } else {
                    return new CraftWaterMob(server, (EntityWaterAnimal) entity);
                }
            } else if (entity instanceof EntityCreature) {
                // Animals
                if (entity instanceof EntityAnimal) {
                    if (entity instanceof EntityChicken) {
                        return new CraftChicken(server, (EntityChicken) entity);
                    } else if (entity instanceof EntityCow) {
                        if (entity instanceof EntityMushroomCow) {
                            return new CraftMushroomCow(server, (EntityMushroomCow) entity);
                        } else {
                            return new CraftCow(server, (EntityCow) entity);
                        }
                    } else if (entity instanceof EntityPig) {
                        return new CraftPig(server, (EntityPig) entity);
                    } else if (entity instanceof EntityTameableAnimal) {
                        if (entity instanceof EntityWolf) {
                            return new CraftWolf(server, (EntityWolf) entity);
                        } else if (entity instanceof EntityOcelot) {
                            return new CraftOcelot(server, (EntityOcelot) entity);
                        } else if (entity instanceof EntityParrot) {
                            return new CraftParrot(server, (EntityParrot) entity);
                        }
                    } else if (entity instanceof EntitySheep) {
                        return new CraftSheep(server, (EntitySheep) entity);
                    } else if (entity instanceof EntityHorseAbstract) {
                        if (entity instanceof EntityHorseChestedAbstract) {
                            if (entity instanceof EntityHorseDonkey) {
                                return new CraftDonkey(server, (EntityHorseDonkey) entity);
                            } else if (entity instanceof EntityHorseMule) {
                                return new CraftMule(server, (EntityHorseMule) entity);
                            } else if (entity instanceof EntityLlama) {
                                return new CraftLlama(server, (EntityLlama) entity);
                            }
                        } else if (entity instanceof EntityHorse) {
                            return new CraftHorse(server, (EntityHorse) entity);
                        } else if (entity instanceof EntityHorseSkeleton) {
                            return new CraftSkeletonHorse(server, (EntityHorseSkeleton) entity);
                        } else if (entity instanceof EntityHorseZombie) {
                            return new CraftZombieHorse(server, (EntityHorseZombie) entity);
                        }
                    } else if (entity instanceof EntityRabbit) {
                        return new CraftRabbit(server, (EntityRabbit) entity);
                    } else if (entity instanceof EntityPolarBear) {
                        return new CraftPolarBear(server, (EntityPolarBear) entity);
                    } else if (entity instanceof EntityTurtle) {
                        return new CraftTurtle(server, (EntityTurtle) entity);
                    } else {
                        return new CraftAnimals(server, (EntityAnimal) entity);
                    }
                }
                // Monsters
                else if (entity instanceof EntityMonster) {
                    if (entity instanceof EntityZombie) {
                        if (entity instanceof EntityPigZombie) {
                            return new CraftPigZombie(server, (EntityPigZombie) entity);
                        } else if (entity instanceof EntityZombieHusk) {
                            return new CraftHusk(server, (EntityZombieHusk) entity);
                        } else if (entity instanceof EntityZombieVillager) {
                            return new CraftVillagerZombie(server, (EntityZombieVillager) entity);
                        } else if (entity instanceof EntityDrowned) {
                            return new CraftDrowned(server, (EntityDrowned) entity);
                        } else {
                            return new CraftZombie(server, (EntityZombie) entity);
                        }
                    } else if (entity instanceof EntityCreeper) {
                        return new CraftCreeper(server, (EntityCreeper) entity);
                    } else if (entity instanceof EntityEnderman) {
                        return new CraftEnderman(server, (EntityEnderman) entity);
                    } else if (entity instanceof EntitySilverfish) {
                        return new CraftSilverfish(server, (EntitySilverfish) entity);
                    } else if (entity instanceof EntityGiantZombie) {
                        return new CraftGiant(server, (EntityGiantZombie) entity);
                    } else if (entity instanceof EntitySkeletonAbstract) {
                        if (entity instanceof EntitySkeletonStray) {
                            return new CraftStray(server, (EntitySkeletonStray) entity);
                        } else if (entity instanceof EntitySkeletonWither) {
                            return new CraftWitherSkeleton(server, (EntitySkeletonWither) entity);
                        } else {
                            return new CraftSkeleton(server, (EntitySkeletonAbstract) entity);
                        }
                    } else if (entity instanceof EntityBlaze) {
                        return new CraftBlaze(server, (EntityBlaze) entity);
                    } else if (entity instanceof EntityWitch) {
                        return new CraftWitch(server, (EntityWitch) entity);
                    } else if (entity instanceof EntityWither) {
                        return new CraftWither(server, (EntityWither) entity);
                    } else if (entity instanceof EntitySpider) {
                        if (entity instanceof EntityCaveSpider) {
                            return new CraftCaveSpider(server, (EntityCaveSpider) entity);
                        } else {
                            return new CraftSpider(server, (EntitySpider) entity);
                        }
                    } else if (entity instanceof EntityEndermite) {
                        return new CraftEndermite(server, (EntityEndermite) entity);
                    } else if (entity instanceof EntityGuardian) {
                        if (entity instanceof EntityGuardianElder) {
                            return new CraftElderGuardian(server, (EntityGuardianElder) entity);
                        } else {
                            return new CraftGuardian(server, (EntityGuardian) entity);
                        }
                    } else if (entity instanceof EntityVex) {
                        return new CraftVex(server, (EntityVex) entity);
                    } else if (entity instanceof EntityIllagerAbstract) {
                        if (entity instanceof EntityIllagerWizard) {
                            if (entity instanceof EntityEvoker) {
                                return new CraftEvoker(server, (EntityEvoker) entity);
                            } else if (entity instanceof EntityIllagerIllusioner) {
                                return new CraftIllusioner(server, (EntityIllagerIllusioner) entity);
                            } else {
                                return new CraftSpellcaster(server, (EntityIllagerWizard) entity);
                            }
                        } else if (entity instanceof EntityVindicator) {
                            return new CraftVindicator(server, (EntityVindicator) entity);
                        } else {
                            return new CraftIllager(server, (EntityIllagerAbstract) entity);
                        }
                    } else {
                        return new CraftMonster(server, (EntityMonster) entity);
                    }
                } else if (entity instanceof EntityGolem) {
                    if (entity instanceof EntitySnowman) {
                        return new CraftSnowman(server, (EntitySnowman) entity);
                    } else if (entity instanceof EntityIronGolem) {
                        return new CraftIronGolem(server, (EntityIronGolem) entity);
                    } else if (entity instanceof EntityShulker) {
                        return new CraftShulker(server, (EntityShulker) entity);
                    }
                } else if (entity instanceof EntityVillager) {
                    return new CraftVillager(server, (EntityVillager) entity);
                } else {
                    return new CraftCreature(server, (EntityCreature) entity);
                }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof EntitySlime) {
                if (entity instanceof EntityMagmaCube) {
                    return new CraftMagmaCube(server, (EntityMagmaCube) entity);
                } else {
                    return new CraftSlime(server, (EntitySlime) entity);
                }
            }
            // Flying
            else if (entity instanceof EntityFlying) {
                if (entity instanceof EntityGhast) {
                    return new CraftGhast(server, (EntityGhast) entity);
                } else if (entity instanceof EntityPhantom) {
                    return new CraftPhantom(server, (EntityPhantom) entity);
                } else {
                    return new CraftFlying(server, (EntityFlying) entity);
                }
            } else if (entity instanceof EntityEnderDragon) {
                return new CraftEnderDragon(server, (EntityEnderDragon) entity);
            }
            // Ambient
            else if (entity instanceof EntityAmbient) {
                if (entity instanceof EntityBat) {
                    return new CraftBat(server, (EntityBat) entity);
                } else {
                    return new CraftAmbient(server, (EntityAmbient) entity);
                }
            } else if (entity instanceof EntityArmorStand) {
                return new CraftArmorStand(server, (EntityArmorStand) entity);
            } else {
                return new CraftLivingEntity(server, (EntityLiving) entity);
            }
        } else if (entity instanceof EntityComplexPart) {
            EntityComplexPart part = (EntityComplexPart) entity;
            if (part.owner instanceof EntityEnderDragon) {
                return new CraftEnderDragonPart(server, (EntityComplexPart) entity);
            } else {
                return new CraftComplexPart(server, (EntityComplexPart) entity);
            }
        } else if (entity instanceof EntityExperienceOrb) {
            return new CraftExperienceOrb(server, (EntityExperienceOrb) entity);
        } else if (entity instanceof EntityTippedArrow) {
            if (((EntityTippedArrow) entity).isTipped()) {
                return new CraftTippedArrow(server, (EntityTippedArrow) entity);
            } else {
                return new CraftArrow(server, (EntityArrow) entity);
            }
        } else if (entity instanceof EntitySpectralArrow) {
            return new CraftSpectralArrow(server, (EntitySpectralArrow) entity);
        } else if (entity instanceof EntityArrow) {
            if (entity instanceof EntityThrownTrident) {
                return new CraftTrident(server, (EntityThrownTrident) entity);
            } else {
                return new CraftArrow(server, (EntityArrow) entity);
            }
        } else if (entity instanceof EntityBoat) {
            return new CraftBoat(server, (EntityBoat) entity);
        } else if (entity instanceof EntityProjectile) {
            if (entity instanceof EntityEgg) {
                return new CraftEgg(server, (EntityEgg) entity);
            } else if (entity instanceof EntitySnowball) {
                return new CraftSnowball(server, (EntitySnowball) entity);
            } else if (entity instanceof EntityPotion) {
                if (!((EntityPotion) entity).isLingering()) {
                    return new CraftSplashPotion(server, (EntityPotion) entity);
                } else {
                    return new CraftLingeringPotion(server, (EntityPotion) entity);
                }
            } else if (entity instanceof EntityEnderPearl) {
                return new CraftEnderPearl(server, (EntityEnderPearl) entity);
            } else if (entity instanceof EntityThrownExpBottle) {
                return new CraftThrownExpBottle(server, (EntityThrownExpBottle) entity);
            }
        } else if (entity instanceof EntityFallingBlock) {
            return new CraftFallingBlock(server, (EntityFallingBlock) entity);
        } else if (entity instanceof EntityFireball) {
            if (entity instanceof EntitySmallFireball) {
                return new CraftSmallFireball(server, (EntitySmallFireball) entity);
            } else if (entity instanceof EntityLargeFireball) {
                return new CraftLargeFireball(server, (EntityLargeFireball) entity);
            } else if (entity instanceof EntityWitherSkull) {
                return new CraftWitherSkull(server, (EntityWitherSkull) entity);
            } else if (entity instanceof EntityDragonFireball) {
                return new CraftDragonFireball(server, (EntityDragonFireball) entity);
            } else {
                return new CraftFireball(server, (EntityFireball) entity);
            }
        } else if (entity instanceof EntityEnderSignal) {
            return new CraftEnderSignal(server, (EntityEnderSignal) entity);
        } else if (entity instanceof EntityEnderCrystal) {
            return new CraftEnderCrystal(server, (EntityEnderCrystal) entity);
        } else if (entity instanceof EntityFishingHook) {
            return new CraftFishHook(server, (EntityFishingHook) entity);
        } else if (entity instanceof EntityItem) {
            return new CraftItem(server, (EntityItem) entity);
        } else if (entity instanceof EntityWeather) {
            if (entity instanceof EntityLightning) {
                return new CraftLightningStrike(server, (EntityLightning) entity);
            } else {
                return new CraftWeather(server, (EntityWeather) entity);
            }
        } else if (entity instanceof EntityMinecartAbstract) {
            if (entity instanceof EntityMinecartFurnace) {
                return new CraftMinecartFurnace(server, (EntityMinecartFurnace) entity);
            } else if (entity instanceof EntityMinecartChest) {
                return new CraftMinecartChest(server, (EntityMinecartChest) entity);
            } else if (entity instanceof EntityMinecartTNT) {
                return new CraftMinecartTNT(server, (EntityMinecartTNT) entity);
            } else if (entity instanceof EntityMinecartHopper) {
                return new CraftMinecartHopper(server, (EntityMinecartHopper) entity);
            } else if (entity instanceof EntityMinecartMobSpawner) {
                return new CraftMinecartMobSpawner(server, (EntityMinecartMobSpawner) entity);
            } else if (entity instanceof EntityMinecartRideable) {
                return new CraftMinecartRideable(server, (EntityMinecartRideable) entity);
            } else if (entity instanceof EntityMinecartCommandBlock) {
                return new CraftMinecartCommand(server, (EntityMinecartCommandBlock) entity);
            }
        } else if (entity instanceof EntityHanging) {
            if (entity instanceof EntityPainting) {
                return new CraftPainting(server, (EntityPainting) entity);
            } else if (entity instanceof EntityItemFrame) {
                return new CraftItemFrame(server, (EntityItemFrame) entity);
            } else if (entity instanceof EntityLeash) {
                return new CraftLeash(server, (EntityLeash) entity);
            } else {
                return new CraftHanging(server, (EntityHanging) entity);
            }
        } else if (entity instanceof EntityTNTPrimed) {
            return new CraftTNTPrimed(server, (EntityTNTPrimed) entity);
        } else if (entity instanceof EntityFireworks) {
            return new CraftFirework(server, (EntityFireworks) entity);
        } else if (entity instanceof EntityShulkerBullet) {
            return new CraftShulkerBullet(server, (EntityShulkerBullet) entity);
        } else if (entity instanceof EntityAreaEffectCloud) {
            return new CraftAreaEffectCloud(server, (EntityAreaEffectCloud) entity);
        } else if (entity instanceof EntityEvokerFangs) {
            return new CraftEvokerFangs(server, (EntityEvokerFangs) entity);
        } else if (entity instanceof EntityLlamaSpit) {
            return new CraftLlamaSpit(server, (EntityLlamaSpit) entity);
        }
        */
        //TODO normaly unused since all the above should be used. But maybe usefull if mods are in use that add new Entities?
        /* Response (i509):
         *
         * I'd say the best approach for allowing modded entities is to have a sort of "createCraftEntity" method (albeit protected method) in a special "EntityCraftLink<E extend Entity>" that is invoked at the TAIL of EntityMixin.
         * All the implementations of craft entity would just override that method to their pleasing to create their own CraftXYZEntity. And if they dont, well the last override will take effect.
         * So if someone adds a new Illager, craftfabric would still hold a "CraftIllagerEntity" on that entity so events can fire and bukkit plugins see an illager entity.
         * However this would prevent us from making every entity impl bukkit does abstract.
         * So in that case we may not have the option to make CraftLivingEntity abstract at all, or be forced to implement a sort of "CraftLivingEntityImpl". that LivingEntity will return by default
         *
         * Response End
         */
        if (entity instanceof LivingEntity) {
            return new CraftLivingEntity(server, (LivingEntity) entity);
        }
        throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass()));
    }

    @Override
    public @NotNull Location getLocation() {
        return new Location(getWorld(), handle.getX(), handle.getY(), handle.getZ(), handle.yaw, handle.pitch);
    }

    @Override
    public Location getLocation(Location location) {
        if (location != null) {
            location.setWorld(getWorld());
            location.setX(handle.getX());
            location.setY(handle.getY());
            location.setZ(handle.getZ());
            location.setYaw(handle.yaw);
            location.setPitch(handle.pitch);
        }
        return location;
    }

    @Override
    public @NotNull Vector getVelocity() {
        return new Vector(handle.getVelocity().getX(), handle.getVelocity().getY(), handle.getVelocity().getZ());
    }

    @Override
    public void setVelocity(@NotNull Vector velocity) {
        Objects.requireNonNull(velocity, "velocity");
        velocity.checkFinite();
        handle.setVelocity(velocity.getX(), velocity.getBlockY(), velocity.getBlockZ());
    }

    @Override
    public double getHeight() {
        return handle.getHeight();
    }

    @Override
    public double getWidth() {
        return handle.getWidth();
    }

    @Override
    public @NotNull BoundingBox getBoundingBox() {
        net.minecraft.util.math.Box box = handle.getBoundingBox();
        return new BoundingBox(box.x1, box.y1, box.z1, box.x2, box.y2, box.z2);
    }

    @Override
    public boolean isOnGround() {
        /* FIXME
        if (entity instanceof EntityArrow) {
            return ((EntityArrow) entity).inGround;
        }
        */
        return handle.onGround;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull World getWorld() {
        return ((CraftLink<World>) (Object) handle.getEntityWorld()).getCraftHandler();

    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

	@Override
	public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause) {
		Preconditions.checkArgument(location != null, "location");
		location.checkFinite();
		ChunkPos chunkPos_1 = new ChunkPos(new BlockPos(location.getX(), location.getY(), location.getZ()));
		((ServerWorld) ((CraftWorld) location.getWorld()).getHandle()).getChunkManager()
				.addTicket(ChunkTicketType.POST_TELEPORT, chunkPos_1, 1, getEntityId());
		leaveVehicle();
		if (handle instanceof ServerPlayerEntity) {
			if (((ServerPlayerEntity) handle).isSleeping()) {
				((ServerPlayerEntity) handle).wakeUp();
			}

			if (location.getWorld().equals(getWorld())) {
				System.out.println("Sameworld! " + location.getWorld().getName());
				((ServerPlayerEntity) handle).networkHandler.teleportRequest(location.getX(), location.getY(), location.getZ(), location.getYaw(),
						location.getPitch(), new HashSet<>());
			} else {
				System.out.println("OtherWorld! " + location.getWorld().getName());
				((ServerPlayerEntity) handle).teleport(((ServerWorld) ((CraftWorld) location.getWorld()).getHandle()), location.getX(), location.getY(), location.getZ(), location.getYaw(),
						location.getPitch());
			}

			handle.setHeadYaw(location.getYaw());
		} else {
			float float_3 = MathHelper.wrapDegrees(location.getYaw());
			float float_4 = MathHelper.wrapDegrees(location.getPitch());
			float_4 = MathHelper.clamp(float_4, -90.0F, 90.0F);
			if (location.getWorld().equals(getWorld())) {
				handle.updatePositionAndAngles(location.getX(), location.getY(), location.getZ(), float_3, float_4);
				handle.setHeadYaw(float_3);
			} else {
				handle.detach();
				handle.dimension = ((CraftWorld) location.getWorld()).getHandle().dimension.getType();
				Entity entity_2 = handle;
				handle = handle.getType().create(((CraftWorld) location.getWorld()).getHandle());
				if (handle == null) {
					handle = entity_2;
					return false;
				}

				handle.copyFrom(entity_2);
				handle.updatePositionAndAngles(location.getX(), location.getY(), location.getZ(), float_3, float_4);
				handle.setHeadYaw(float_3);
				((ServerWorld) ((CraftWorld) location.getWorld()).getHandle()).removeEntity(handle);
				entity_2.removed = true;
			}
		}
		return true;
	}

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    @Override
    public @NotNull List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        List<Entity> notchEntityList = handle.world.getEntities(handle, handle.getBoundingBox().expand(x, y, z), null);
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<>(notchEntityList.size());

        for (Entity entity : notchEntityList) {
            bukkitEntityList.add(getEntity(server, entity));
        }
        return bukkitEntityList;
    }
}
