//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
/*
package net.runelite.client.plugins.zeasyparty;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.socket.org.json.JSONArray;
import net.runelite.client.plugins.socket.org.json.JSONObject;
import net.runelite.client.plugins.socket.packet.SocketBroadcastPacket;
import net.runelite.client.plugins.socket.packet.SocketReceivePacket;
import net.runelite.client.plugins.zeasyparty.EasyPartyPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.UUID;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.ws.PartyService;

public class EasyPartyPanel extends PluginPanel {
    private static final String CREATE_BUTTON_TEXT = "Create party";
    private static final String JOIN_PARTY_TEXT = "Join party";
    private static final String CREATE_PARTY_SUCCESS = "Created a new party.";
    private static final String JOIN_PARTY_SUCCESS = "Joined the party.";
    private static final String EMPTY_PARTY_ID = "You have to enter a party id.";
    private static final String INVALID_PARTY_ID = "You entered an invalid party id.";
    private static final String COPY_SUCCESS = "Copied the party id to your clipboard.";
    private final JLabel currentPartyLabel = new JLabel("No party joined.", 0);
    private final JLabel messageLabel = new JLabel();
    private final JLabel copySuccessLabel = new JLabel();
    private final JTextField textFieldJoinParty = new JTextField();
    private UUID partyUUID;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    Client client;
    @Inject
    private EventBus eventBus;

    public EasyPartyPanel(PartyService partyService) {
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = 2;
        gridBagConstraints.weightx = 1.0D;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 8, 0);
        this.setErrorLabel("");
        this.partyUUID = UUID.randomUUID();
        partyService.changeParty(this.partyUUID);
        this.currentPartyLabel.setText(String.valueOf(this.partyUUID));
        this.revalidate();
        this.repaint();
        this.setSuccessLabel("Created a new party.");

        JSONArray data = new JSONArray();
        JSONObject jsonwp = new JSONObject();
        jsonwp.put("partyID", this.partyUUID);
        data.put(jsonwp);
        JSONObject payload = new JSONObject();
        payload.put("partyID", data);
        JButton buttonCreateParty = new JButton("Create party");
        buttonCreateParty.addActionListener((e) -> {
            createPartyId(partyService);
        });
        this.add(buttonCreateParty, gridBagConstraints);
        ++gridBagConstraints.gridy;
        JButton buttonJoinParty = new JButton("Join party");
        buttonJoinParty.addActionListener((e) -> {
            this.setErrorLabel("");
            if (this.textFieldJoinParty.getText().isEmpty()) {
                this.setErrorLabel("You have to enter a party id.");
            } else {
                try {
                    this.partyUUID = UUID.fromString(this.textFieldJoinParty.getText());
                    partyService.changeParty(this.partyUUID);
                    this.currentPartyLabel.setText(String.valueOf(this.partyUUID));
                    this.textFieldJoinParty.setText("");
                    this.revalidate();
                    this.repaint();
                    this.setSuccessLabel("Joined the party.");
                } catch (Exception var4) {
                    this.setErrorLabel("You entered an invalid party id.");
                }
            }

        });
        this.add(this.textFieldJoinParty, gridBagConstraints);
        ++gridBagConstraints.gridy;
        this.add(buttonJoinParty, gridBagConstraints);
        ++gridBagConstraints.gridy;
        this.add(this.messageLabel, gridBagConstraints);
        ++gridBagConstraints.gridy;
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new BoxLayout(partyPanel, 1));
        partyPanel.setBorder(new LineBorder(ColorScheme.DARKER_GRAY_COLOR));
        //partyPanel.addMouseListener(new 1(this, partyPanel));
        Border border = partyPanel.getBorder();
        Border margin = new EmptyBorder(10, 10, 10, 10);
        partyPanel.setBorder(new CompoundBorder(border, margin));
        JLabel copyLabel = new JLabel("Click to copy", 0);
        copyLabel.setFont(new Font(FontManager.getRunescapeFont().getName(), 0, 25));
        copyLabel.setAlignmentX(0.5F);
        this.currentPartyLabel.setAlignmentX(0.5F);
        partyPanel.add(copyLabel);
        partyPanel.add(this.currentPartyLabel);
        this.add(partyPanel, gridBagConstraints);
        ++gridBagConstraints.gridy;
        this.add(this.copySuccessLabel, gridBagConstraints);
        ++gridBagConstraints.gridy;
    }

    @Subscribe
    public void onSocketReceivePacket(SocketReceivePacket event) {
        try {
            JSONObject payload = event.getPayload();
            if (!payload.has("partyID")) {
                return;
            }

            JSONArray data = payload.getJSONArray("partyID");
            JSONObject jsonmsg = data.getJSONObject(0);
            String partyID = jsonmsg.getString("partyID");

            //sendChatMessage("recieved id: "+partyID);

        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public void createPartyId(PartyService partyService){
        this.setErrorLabel("");
        this.partyUUID = UUID.randomUUID();
        partyService.changeParty(this.partyUUID);
        this.currentPartyLabel.setText(String.valueOf(this.partyUUID));
        this.revalidate();
        this.repaint();
        this.setSuccessLabel("Created a new party.");

        JSONArray data = new JSONArray();
        JSONObject jsonwp = new JSONObject();
        jsonwp.put("partyID", this.partyUUID);
        data.put(jsonwp);
        JSONObject payload = new JSONObject();
        payload.put("partyID", data);

        //sendChatMessage("attempting to send this id: ");
        this.eventBus.post(new SocketBroadcastPacket(payload));
    }

    public void sendChatMessage(String chatMessage)
    {
        final String message = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(chatMessage)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }

    private void setSuccessLabel(String text) {
        this.messageLabel.setForeground(Color.GREEN);
        this.messageLabel.setText(text);
        this.resetMessageLabel();
    }

    private void setErrorLabel(String text) {
        this.messageLabel.setForeground(Color.RED);
        this.messageLabel.setText(text);
        this.resetMessageLabel();
    }

    private void setCopySuccessLabel() {
        this.copySuccessLabel.setForeground(Color.GREEN);
        this.copySuccessLabel.setText("Copied the party id to your clipboard.");
        //(new Timer()).schedule(new 2(this), 5000L);
    }

    private void resetMessageLabel() {
        //(new Timer()).schedule(new 3(this), 5000L);
    }
}
*/