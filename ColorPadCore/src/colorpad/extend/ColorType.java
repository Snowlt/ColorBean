package colorpad.extend;

/**
 * All available shown types of color
 *
 * @author Snow
 */
public enum ColorType {
    RGB("RGB"),
    HEX("Hex"),
    GRAY("Gray"),
    HSB("HSB"),
    HSL("HSL"),
    CMYK("CMYK"),
    YCrCb("YCrCb"),
    CIELAB("Lab"),
    XYZ("XYZ"),
    ;

    ColorType(String name) {
        this.name = name;
    }

    private final String name;

    /**
     * Display name
     */
    public String displayName() {
        return name;
    }
}
