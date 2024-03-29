package com.superworldsun.superslegend.items.weapons.boomerang;

import com.superworldsun.superslegend.entities.projectiles.boomerang.MagicBoomerangEntity;

public class MagicBoomerangItem extends AbstractBoomerangItem {
    public MagicBoomerangItem() {
        super(new Properties());
    }

    @Override
    protected BoomerangConsructor getProjectileConstructor() {
        return MagicBoomerangEntity::new;
    }
}
