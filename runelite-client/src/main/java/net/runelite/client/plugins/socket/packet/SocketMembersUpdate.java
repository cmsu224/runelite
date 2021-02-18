//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.packet;

import java.util.List;

public class SocketMembersUpdate {
    private List<String> members;

    public SocketMembersUpdate(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return this.members;
    }
}
