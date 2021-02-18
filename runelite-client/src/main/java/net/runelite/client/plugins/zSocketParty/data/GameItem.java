//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.zSocketParty.data;

import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.client.game.ItemManager;

public final class GameItem {
    private final int id;
    private final int qty;
    private final String name;
    private final boolean stackable;
    private final int price;

    public GameItem(Item item, ItemManager itemManager) {
        this(item.getId(), item.getQuantity(), itemManager);
    }

    public GameItem(int id, int qty, ItemManager itemManager) {
        this.id = id;
        this.qty = qty;
        ItemComposition c = itemManager.getItemComposition(id);
        this.name = c.getName();
        this.stackable = c.isStackable();
        this.price = itemManager.getItemPrice(c.getNote() != -1 ? c.getLinkedNoteId() : id);
    }

    public static GameItem[] convertItemsToGameItems(Item[] items, ItemManager itemManager) {
        GameItem[] output = new GameItem[items.length];

        for(int i = 0; i < items.length; ++i) {
            Item item = items[i];
            if (item != null && item.getId() != -1) {
                output[i] = new GameItem(item, itemManager);
            } else {
                output[i] = null;
            }
        }

        return output;
    }

    public int getId() {
        return this.id;
    }

    public int getQty() {
        return this.qty;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStackable() {
        return this.stackable;
    }

    public int getPrice() {
        return this.price;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof GameItem)) {
            return false;
        } else {
            GameItem other = (GameItem)o;
            if (this.getId() != other.getId()) {
                return false;
            } else if (this.getQty() != other.getQty()) {
                return false;
            } else {
                label33: {
                    Object this$name = this.getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label33;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label33;
                    }

                    return false;
                }

                if (this.isStackable() != other.isStackable()) {
                    return false;
                } else {
                    return this.getPrice() == other.getPrice();
                }
            }
        }
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getQty();
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        result = result * 59 + (this.isStackable() ? 79 : 97);
        result = result * 59 + this.getPrice();
        return result;
    }

    public String toString() {
        return "GameItem(id=" + this.getId() + ", qty=" + this.getQty() + ", name=" + this.getName() + ", stackable=" + this.isStackable() + ", price=" + this.getPrice() + ")";
    }

    public GameItem(int id, int qty, String name, boolean stackable, int price) {
        this.id = id;
        this.qty = qty;
        this.name = name;
        this.stackable = stackable;
        this.price = price;
    }
}
