
package swing;

import java.awt.*;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class transparantTextField extends JTextField {
    public transparantTextField() {
        setOpaque(false);
        setBorder(new EmptyBorder(9, 1, 9, 1));
        setBackground(new Color(0, 0, 0, 0));
        setForeground(new Color(255, 255, 255));
    }
}
