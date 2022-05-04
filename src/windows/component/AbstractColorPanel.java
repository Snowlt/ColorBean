package windows.component;

import colorpad.extend.ConvertBridge;

import javax.swing.*;
import java.util.function.Consumer;

/**
 * 调色面版的基础类
 *
 * @author Snow
 */
public abstract class AbstractColorPanel extends JPanel {

    protected boolean selfUpdate = false;
    protected Consumer<ConvertBridge> updateCallback;

    protected int sliderWidth = 280, sliderHeight = 40, txtWidth = 40, txtHeight = 20;

    @Override
    public int getHeight() {
        return 148;
    }

    AbstractColorPanel() {
        // 设置面板
        this.setLayout(null);
        this.setBounds(0, 0, 340, this.getHeight());
        this.setOpaque(false);
    }

    public void setUpdateCallback(Consumer<ConvertBridge> updateCallback) {
        selfUpdate = true;
        this.updateCallback = updateCallback;
        selfUpdate = false;
    }

    public final void updatePanel(ConvertBridge bridge) {
        selfUpdate = true;
        updateCurrentColor(bridge);
        selfUpdate = false;
    }

    protected final void autoUpdateOutside() {
        if (selfUpdate || updateCallback == null) {
            return;
        }
        selfUpdate = true;
        updateCallback.accept(getCurrentColor());
        selfUpdate = false;
    }

    /**
     * 内部调用 修改主窗体的信息
     */
    abstract protected ConvertBridge getCurrentColor();


    /**
     * 外部用 更新 RGB 面板的颜色数据为当前颜色
     *
     * @param bridge 新的颜色(包装对象)
     */
    abstract protected void updateCurrentColor(ConvertBridge bridge);
}
