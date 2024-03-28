package com.superworldsun.superslegend.items.weapons.boomerang;

import com.superworldsun.superslegend.entities.projectiles.boomerang.BoomerangEntity;

public class BoomerangItem extends AbstractBoomerangItem {
    public BoomerangItem() {
        super(new Properties());
    }

    @Override
    protected BoomerangConsructor getProjectileConstructor() {
        return BoomerangEntity::new;
    }
}
