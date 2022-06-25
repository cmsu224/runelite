package net.runelite.client.plugins.maz.unusedplugins.entityhiderextendedmaz;

import com.google.inject.Provides;
import java.util.*;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.BLClient;
import net.runelite.api.BLEntity;
import net.runelite.api.Client;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
        name = "EntityHiderExtendedPlugin",
        description = "shhh",
        tags = {"dead","hide","npc"}
)

public class EntityHiderExtendedPlugin extends Plugin
{
    private static final Logger log = LoggerFactory.getLogger(EntityHiderExtendedPlugin.class);
    @Inject
    private Client client;
    @Inject
    private EntityHiderExtendedConfig config;
    @Inject
    private PluginManager pluginManager;
    private HashSet<String> getNpcsToHide = null;
    private HashSet<String> getNpcsToHideOnDeath = null;
    private HashSet<Integer> getNpcsByAnimationToHideOnDeath = null;
    private HashSet<Integer> getNpcsByIdToHideOnDeath = null;
    private HashSet<Integer> getGrahpicsObjectByIdToHide = new HashSet();

    public EntityHiderExtendedPlugin() {
    }

    @Provides
    EntityHiderExtendedConfig provideConfig(ConfigManager configManager) {
        return (EntityHiderExtendedConfig)configManager.getConfig(EntityHiderExtendedConfig.class);
    }

    private void setDeadNPCsHidden(boolean val) {
        if (this.client instanceof BLClient) {
            BLClient blClient = (BLClient)this.client;
            blClient.setDeadNPCsHidden(val);
        }

    }

    protected void startUp() {
        if (this.client instanceof BLClient) {
            BLClient blClient = (BLClient)this.client;
            this.getNpcsToHide = blClient.getNpcsToHide();
            this.getNpcsToHideOnDeath = blClient.getNpcsToHideOnDeath();
            this.getNpcsByAnimationToHideOnDeath = blClient.getNpcsByAnimationToHideOnDeath();
            this.getNpcsByIdToHideOnDeath = blClient.getNpcsByIdToHideOnDeath();
            this.updateConfig();
            this.getNpcsToHide.addAll(Text.fromCSV(this.config.hideNPCsNames().toLowerCase()));
            this.getNpcsToHideOnDeath.addAll(Text.fromCSV(this.config.hideNPCsOnDeath().toLowerCase()));
            parseAndAddSave(Text.fromCSV(this.config.hideNPCsByID()), this.getNpcsByIdToHideOnDeath);
            parseAndAddSave(Text.fromCSV(this.config.hideNPCsByAnimationId()), this.getNpcsByAnimationToHideOnDeath);
            parseAndAddSave(Text.fromCSV(this.config.hideGrahpicsObjectById()), this.getGrahpicsObjectByIdToHide);
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    this.pluginManager.stopPlugin(this);
                } catch (PluginInstantiationException var2) {
                    log.error("error stopping plugin", var2);
                }

            });
        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("ehextended")) {
            this.updateConfig();
            if (event.getOldValue() == null || event.getNewValue() == null) {
                return;
            }

            if (event.getKey().equals("hideNPCsNames")) {
                this.getNpcsToHide.clear();
                this.getNpcsToHide.addAll(Text.fromCSV(this.config.hideNPCsNames().toLowerCase()));
            }

            if (event.getKey().equals("hideNPCsOnDeath")) {
                this.getNpcsByIdToHideOnDeath.clear();
                this.getNpcsToHideOnDeath.addAll(Text.fromCSV(this.config.hideNPCsOnDeath().toLowerCase()));
            }

            if (event.getKey().equals("hideNPCsByID")) {
                this.getNpcsByIdToHideOnDeath.clear();
                parseAndAddSave(Text.fromCSV(this.config.hideNPCsByID()), this.getNpcsByIdToHideOnDeath);
            }

            if (event.getKey().equals("hideNPCsByAnimationId")) {
                this.getNpcsByAnimationToHideOnDeath.clear();
                parseAndAddSave(Text.fromCSV(this.config.hideNPCsByAnimationId()), this.getNpcsByAnimationToHideOnDeath);
            }

            if (event.getKey().equals("hideGrahpicsObjectById")) {
                this.getGrahpicsObjectByIdToHide.clear();
                parseAndAddSave(Text.fromCSV(this.config.hideGrahpicsObjectById()), this.getGrahpicsObjectByIdToHide);
            }
        }

    }

    private static void parseAndAddSave(Collection<String> source, Collection<Integer> collection) {
        Iterator var2 = source.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();

            try {
                int val = Integer.parseInt(s);
                collection.add(val);
            } catch (NumberFormatException var5) {
                log.warn("Config entry could not be parsed, entry: {}", s);
            }
        }

    }

    private void updateConfig() {
        this.setDeadNPCsHidden(this.config.hideDeadNPCs());
    }

    protected void shutDown() {
        try {
            this.setDeadNPCsHidden(false);
            Text.fromCSV(this.config.hideNPCsNames().toLowerCase()).forEach((s) -> {
                this.getNpcsToHide.remove(s);
            });
            Text.fromCSV(this.config.hideNPCsOnDeath().toLowerCase()).forEach((s) -> {
                this.getNpcsToHideOnDeath.remove(s);
            });
            Text.fromCSV(this.config.hideNPCsByID()).forEach((id) -> {
                this.getNpcsByIdToHideOnDeath.remove(Integer.parseInt(id));
            });
            Text.fromCSV(this.config.hideNPCsByAnimationId()).forEach((id) -> {
                this.getNpcsByAnimationToHideOnDeath.remove(Integer.parseInt(id));
            });
            Text.fromCSV(this.config.hideGrahpicsObjectById()).forEach((id) -> {
                this.getGrahpicsObjectByIdToHide.remove(Integer.parseInt(id));
            });
        } catch (NullPointerException | AbstractMethodError var2) {
        }

    }

    @Subscribe
    protected void onGraphicsObjectCreated(GraphicsObjectCreated graphicsObjectCreated) {
        try {
            if (this.getGrahpicsObjectByIdToHide.contains(graphicsObjectCreated.getGraphicsObject().getId()) && graphicsObjectCreated.getGraphicsObject() instanceof BLEntity) {
                BLEntity entity = (BLEntity)graphicsObjectCreated.getGraphicsObject();
                entity.setHidden(true);
            }
        } catch (NoSuchMethodError var3) {
            log.debug("Couldn't hide graphics object", var3);
        }

    }

}