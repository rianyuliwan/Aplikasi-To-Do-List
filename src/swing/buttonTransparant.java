
package swing;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class buttonTransparant extends JButton {
    public buttonTransparant() {
        setOpaque(false);
        setBorder(new EmptyBorder(9, 1, 9, 1));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(new Color(255, 255, 255));
    }
}
