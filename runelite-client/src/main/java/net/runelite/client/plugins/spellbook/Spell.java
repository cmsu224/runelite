//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.spellbook;

class Spell {
    private int widget;
    private int x;
    private int y;
    private int size;
    private String name;

    public Spell() {
    }

    public int getWidget() {
        return this.widget;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public void setWidget(int widget) {
        this.widget = widget;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        int var10000 = this.getWidget();
        return "Spell(widget=" + var10000 + ", x=" + this.getX() + ", y=" + this.getY() + ", size=" + this.getSize() + ", name=" + this.getName() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Spell)) {
            return false;
        } else {
            Spell other = (Spell)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getWidget() != other.getWidget()) {
                return false;
            } else if (this.getX() != other.getX()) {
                return false;
            } else if (this.getY() != other.getY()) {
                return false;
            } else if (this.getSize() != other.getSize()) {
                return false;
            } else {
                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Spell;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getWidget();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getSize();
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        return result;
    }
}
