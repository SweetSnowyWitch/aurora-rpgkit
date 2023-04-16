package com.github.sweetsnowywitch.csmprpgkit.magic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class Aspect implements SpellElement, Comparable<Aspect> {
    public enum Kind {
        EFFECT,
        REACTION,
    }

    public static final int DEFAULT_COLOR = ColorHelper.Argb.getArgb(0xFF, 0x99, 0xD9, 0xEA); /* light blue, kinda */

    public final Identifier id;

    private final Kind kind;
    private final ImmutableMap<String, @NotNull Float> scales;
    private final int color;
    private final boolean primary;
    private final Identifier texturePath;

    public Aspect(Identifier id, Kind kind, ImmutableMap<String, @NotNull Float> scales, int color, boolean primary) {
        this.id = id;
        this.kind = kind;
        this.scales = scales;
        this.color = color;
        this.primary = primary;
        this.texturePath = new Identifier(id.getNamespace(), "textures/magic/aspects/"+id.getPath()+".png");
    }

    public Kind getKind() {
        return kind;
    }

    public float getBaseCost(String key) {
        return Objects.requireNonNull(scales.getOrDefault(key, (float)0));
    }

    public int getColor() {
        return this.color;
    }

    public boolean isPrimary() {
        return this.primary;
    }

    public Identifier getTexturePath() {
        return this.texturePath;
    }

    @Override
    public String toString() {
        return "Aspect[id=" + id + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aspect aspect = (Aspect) o;
        return id.equals(aspect.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(@NotNull Aspect o) {
        return this.id.compareTo(o.id);
    }
}
