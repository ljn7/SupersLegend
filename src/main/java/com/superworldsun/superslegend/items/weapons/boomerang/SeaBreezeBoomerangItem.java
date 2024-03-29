package com.superworldsun.superslegend.items.weapons.boomerang;

import com.superworldsun.superslegend.entities.projectiles.boomerang.SeaBreezeBoomerangEntity;

public class SeaBreezeBoomerangItem extends AbstractBoomerangItem {
    public SeaBreezeBoomerangItem() {
        super(new Properties());
    }

    @Override
    protected BoomerangConsructor getProjectileConstructor() {
        return SeaBreezeBoomerangEntity::new;
    }
}
