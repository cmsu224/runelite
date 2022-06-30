package net.runelite.client.plugins.maz.plugins.gameobjectremover;


import com.google.inject.Provides;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "<html><font color=#FF0000>[Maz] GameObjectHide",
        description = "Removes listed game objects. Input game object IDs seperated by a comma -> 1234,4567",
        tags = {"game object, hide, remove"},
        enabledByDefault = false
)
public class GameObjectRemoverPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private GameObjectRemoverConfig config;
    private Set<Integer> objectIds = new HashSet();
    private Set<Integer> objectIds2 = new HashSet();

    public GameObjectRemoverPlugin() {
    }

    @Provides
    GameObjectRemoverConfig getConfig(ConfigManager configManager) {
        return (GameObjectRemoverConfig)configManager.getConfig(GameObjectRemoverConfig.class);
    }

    public void startUp() {
        this.objectIds.clear();
        this.objectIds2.clear();
        this.populateList();
    }

    public void shutDown() {
        this.objectIds.clear();
        this.objectIds2.clear();
        this.refreshScene();
    }

    private void populateList() {
        if (this.config.objectList().trim().length() > 0) {
            String[] var1 = this.config.objectList().trim().split(",");
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String objectID = var1[var3];

                try {
                    this.objectIds.add(Integer.valueOf(objectID));
                } catch (NumberFormatException var6) {
                    var6.printStackTrace();
                }
            }
        }

        if (this.config.objectList2().trim().length() > 0) {
            String[] var1 = this.config.objectList2().trim().split(",");
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String objectID = var1[var3];

                try {
                    this.objectIds2.add(Integer.valueOf(objectID));
                } catch (NumberFormatException var6) {
                    var6.printStackTrace();
                }
            }
        }

    }

    public void refreshScene() {
        if (this.client.getGameState() != GameState.LOGIN_SCREEN && this.client.getGameState() != GameState.HOPPING && this.client.getGameState() != GameState.LOGIN_SCREEN_AUTHENTICATOR) {
            this.clientThread.invokeLater(() -> {
                this.client.setGameState(GameState.LOADING);
            });
        }

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("gameobjectremover")) {
            this.refreshScene();
            this.objectIds.clear();
            this.objectIds2.clear();
            this.populateList();
        }

    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        int objID = event.getGameObject().getId();
        if (this.objectIds2.contains(objID)) {
            this.removeGameObjectsFromScene(this.objectIds, 1);
        }
        if (this.objectIds.contains(objID)) {
            this.removeGameObjectsFromScene(this.objectIds, this.client.getPlane());
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        this.removeGameObjectsFromScene(this.objectIds, this.client.getPlane());
        this.removeGameObjectsFromScene(this.objectIds2, 1);
    }

    private void removeGameObjectsFromScene(Set<Integer> objectIds, int plane) {
        if (objectIds == null) {
            return;
        }
        Scene scene = client.getScene();
        Tile[][] tiles = scene.getTiles()[plane];
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[x][y];
                if (tile == null) {
                    continue;
                }

                for (GameObject gameObject : tile.getGameObjects()) {
                    if (gameObject == null) {
                        continue;
                    }

                    if (objectIds.contains(gameObject.getId())) {
                        scene.removeGameObject(gameObject);
                    }
                }
            }
        }
    }
}
