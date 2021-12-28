//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

public class DeferredCheck {
    private int tick;
    private int anim;
    private boolean piety;

    public DeferredCheck(int tick, int anim, boolean piety) {
        this.tick = tick;
        this.anim = anim;
        this.piety = piety;
    }

    public int getTick() {
        return this.tick;
    }

    public int getAnim() {
        return this.anim;
    }

    public boolean isPiety() {
        return this.piety;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public void setAnim(int anim) {
        this.anim = anim;
    }

    public void setPiety(boolean piety) {
        this.piety = piety;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DeferredCheck)) {
            return false;
        } else {
            DeferredCheck other = (DeferredCheck)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getTick() != other.getTick()) {
                return false;
            } else if (this.getAnim() != other.getAnim()) {
                return false;
            } else {
                return this.isPiety() == other.isPiety();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeferredCheck;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getTick();
        result = result * 59 + this.getAnim();
        result = result * 59 + (this.isPiety() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "AzEasyTobPlugin.DeferredCheck(tick=" + this.getTick() + ", anim=" + this.getAnim() + ", piety=" + this.isPiety() + ")";
    }
}
