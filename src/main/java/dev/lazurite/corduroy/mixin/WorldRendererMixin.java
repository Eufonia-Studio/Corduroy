package dev.lazurite.corduroy.mixin;

import dev.lazurite.corduroy.impl.camera.CorduroyCamera;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;",
                    ordinal = 3
            )
    )
    public Entity getFocusedEntity(Camera camera) {
        if (client.gameRenderer.getCamera() instanceof CorduroyCamera) {
            if (((CorduroyCamera) client.gameRenderer.getCamera()).shouldRenderPlayer()) {
                return client.player;
            }
        }

        return camera.getFocusedEntity();
    }
}
