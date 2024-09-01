package mode;

import java.awt.*;

public class Rutinas2 {

    public static Font getFont(String letra, boolean negrita, int baseSize, int width, int height, float reduccion) {
        int scaledSize = (int) (baseSize * (Math.min(width, height) / reduccion));
        if (negrita) {
            return new Font(letra, Font.BOLD, scaledSize);
        } else {
            return new Font(letra, Font.PLAIN, scaledSize);
        }
    }

}
