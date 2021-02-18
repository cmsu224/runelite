//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket;

public enum SocketLog {
    INFO("<col=008000>"),
    ERROR("<col=b4281e>");

    private String prefix;

    private SocketLog(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
