package com.dragoncorpse;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.RotationAxis;

/** Vanilla dragon renderer, but frozen (no flapping) with an extra pose transform for the corpse. */
public class DragonCorpseRenderer extends EnderDragonEntityRenderer {
    // 0.5 = wings drooped down + mouth slightly open. (0.0 = wings nearly flat)
    private static final float WING_POSE = 0.5f;

    public DragonCorpseRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnderDragonEntity dragon, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        if (dragon instanceof DragonCorpseEntity corpse) {
            // force wings to stay still, no matter what
            corpse.lastWingPosition = WING_POSE;
            corpse.wingPosition = WING_POSE;

            matrices.translate(0.0, corpse.getCorpseYOffset(), 0.0);
            // head points where the player was facing when spawned
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - yaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-corpse.getCorpsePitch()));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(corpse.getCorpseRoll()));
        }
        super.render(dragon, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}
