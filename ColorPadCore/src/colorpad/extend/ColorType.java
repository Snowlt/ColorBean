package colorpad.extend;

/**
 * All available shown types of color
 *
 * @author Snow
 */
public enum ColorType {
    RGB("RGB"),
    HEX("Hex"),
    GRAYSCALE("Grayscale"),
    HSB("HSB"),
    HSL("HSL"),
    CMYK("CMYK"),
    Y_CR_CB("YCrCb"),
    CIE_LAB("Lab"),
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
