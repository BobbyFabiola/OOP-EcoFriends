package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class TitleScreen extends JPanel {

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 25;
    public final int maxScreenRow = 12;

    private Image backgroundImage;

    Font minecraft;

    public TitleScreen(JFrame window) {

        // Load the background image
        try {
            InputStream is = getClass().getResourceAsStream("/player/images/titlebg.png");
            if (is != null) {
                backgroundImage = ImageIO.read(is);
            } else {
                throw new IOException("Failed to load the background image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            InputStream is = getClass().getResourceAsStream("/player/images/Minecraftia-Regular.ttf");
            minecraft = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(minecraft);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setOpaque(false);

        JLabel titleLabel = new JLabel("EcoFriends");
        titleLabel.setFont(new Font(minecraft.getName(), Font.BOLD, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("Catch falling corns with Bara to save the environment!");
        descriptionLabel.setFont(new Font(minecraft.getName(), Font.PLAIN, 25));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);



        JLabel playLabel = new JLabel(new ImageIcon(getClass().getResource("/player/images/playbutton.png")));
        JLabel helpLabel = new JLabel(new ImageIcon(getClass().getResource("/player/images/helpbutton.png")));

        playLabel.setOpaque(false);
        helpLabel.setOpaque(false);

        playLabel.addMouseListener(new DarkeningMouseListener(playLabel, "/player/images/playbutton.png"));
        helpLabel.addMouseListener(new DarkeningMouseListener(helpLabel, "/player/images/helpbutton.png"));

        playLabel.setPreferredSize(new Dimension(100, 100));
        helpLabel.setPreferredSize(new Dimension(100, 100));



        playLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startGame(window);
            }
        });

        helpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showHelpPopup(window);
            }
        });

        JPanel imagePanel = new JPanel(new FlowLayout());
        imagePanel.setOpaque(false); // This line ensures that the panel is transparent
        imagePanel.add(playLabel);
        imagePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        imagePanel.add(helpLabel);

        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(descriptionLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(imagePanel);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private void startGame(JFrame window) {
        window.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.revalidate();
        window.repaint();
        gamePanel.startGameThread();

        gamePanel.requestFocusInWindow();
    }

    private void showHelpPopup(JFrame parent) {
        JFrame helpFrame = new JFrame("Help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(500, 400);
        helpFrame.setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("EcoFriends Help");
        titleLabel.setFont(new Font(minecraft.getName(), Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea helpText = new JTextArea("\n\nDirections:\n\n" +
                "Use 'A' and 'D' keys to move Bara left and right.\n" +
                "Catch falling corns to save the environment!\n\n" +
                "TIP: Avoid missing too many corns.");
        helpText.setEditable(false);
        helpText.setOpaque(false);
        helpText.setFont(new Font(minecraft.getName(), Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JLabel closeButton = new JLabel(new ImageIcon(getClass().getResource("/player/images/closebutton.png")));

        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(closeButton);
                frame.dispose();
            }
        });

        closeButton.addMouseListener(new DarkeningMouseListener(closeButton, "/player/images/closebutton.png"));

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(closeButton);

        helpFrame.add(contentPanel);
        helpFrame.setVisible(true);
    }

    private static class DarkeningMouseListener extends MouseAdapter {
        private final JLabel label;
        private final ImageIcon originalIcon;
        private final ImageIcon darkerIcon;

        public DarkeningMouseListener(JLabel label, String imagePath) {
            this.label = label;
            this.originalIcon = new ImageIcon(getClass().getResource(imagePath));
            this.darkerIcon = createDarkerIcon(originalIcon);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            label.setIcon(darkerIcon);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setIcon(originalIcon);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            label.setIcon(darkerIcon);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            label.setIcon(originalIcon);
        }



        private ImageIcon createDarkerIcon(ImageIcon originalIcon) {

            Image originalImage = originalIcon.getImage();
            BufferedImage bufferedImage = new BufferedImage(
                    originalImage.getWidth(null),
                    originalImage.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, null);
            g.dispose();

            // Apply a RescaleOp to darken the image
            RescaleOp op = new RescaleOp(0.8f, 0, null);
            Image darkerImage = op.filter(bufferedImage, null);

            return new ImageIcon(darkerImage);
        }
    }

}
