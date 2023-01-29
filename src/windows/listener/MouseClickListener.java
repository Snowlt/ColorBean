package windows.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 处理点击事件掉监听器
 */
public class MouseClickListener implements MouseListener {

    private Consumer<MouseEvent> onClicked;

    public static MouseClickListener buildClicked(Consumer<MouseEvent> onClicked) {
        MouseClickListener listener = new MouseClickListener();
        listener.onClicked = Objects.requireNonNull(onClicked);
        return listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        onClicked.accept(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
