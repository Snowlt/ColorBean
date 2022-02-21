package colorpad.core.algorithm;

import colorpad.core.Basic;
import colorpad.core.model.*;

import static colorpad.core.ConversionManager.register;

/**
 * Default color model conversion algorithm
 */
public final class DefaultAlgorithm {

    private static final DefaultAlgorithm DA = new DefaultAlgorithm();

    public static DefaultAlgorithm getDefaultAlgorithm() {
        return DA;
    }

    public static void registerDefaultAlgorithm() {
        // Register default converter
        register(Rgb.class, Hsb.class, DA::RGBToHSB);
        register(Hsb.class, Rgb.class, DA::HSBToRGB);
        register(Rgb.class, Hsl.class, DA::RGBToHSL);
        register(Hsl.class, Rgb.class, DA::HSLToRGB);
        register(Rgb.class, Cmyk.class, DA::RGBToCMYK);
        register(Cmyk.class, Rgb.class, DA::CMYKToRGB);
        register(Rgb.class, YCrCb.class, DA::RGBToYCrCb);
        register(YCrCb.class, Rgb.class, DA::YCrCbToRGB);
        register(Rgb.class, Xyz.class, DA::RGBToXYZ);
        register(Xyz.class, Rgb.class, DA::XYZToRGB);
        // HSB and HSL
        register(Hsb.class, Hsl.class, DA::HSBToHSL);
        register(Hsl.class, Hsb.class, DA::HSLToHSB);
        // XYZ and Lab
        register(Xyz.class, Lab.class, DA::XYZToLab);
        register(Lab.class, Xyz.class, DA::LabToXYZ);
    }

    @SuppressWarnings("DuplicatedCode")
    public Hsb RGBToHSB(Rgb rgb) {
        int max, min;
        double h, s, v;
        max = Math.max(Math.max(rgb.r(), rgb.g()), rgb.b());
        min = Math.min(Math.min(rgb.r(), rgb.g()), rgb.b());
        v = max / 255d;
        s = max != 0 ? (max - min) / (double) max : 0d;
        if (Basic.decimalEquals(s, 0d)) {
            h = 0d;
        } else {
            if (max == rgb.r()) {
                h = (double) (60 * (rgb.g() - rgb.b())) / (max - min);
                if (h < 0d) h += 360d;
            } else if (max == rgb.g()) {
                h = 120d + (double) (60 * (rgb.b() - rgb.r())) / (max - min);
            } else {
                // if (max == rgb.b())
                h = 240d + (double) (60 * (rgb.r() - rgb.g())) / (max - min);
            }
        }
        return new Hsb(h, s * 100d, v * 100d);
    }

    public Rgb HSBToRGB(Hsb hsb) {
        double r, g, b, h, s, v;
        h = hsb.h() % 360d;
        s = hsb.s() / 100d;
        v = hsb.b() / 100d;
        int i;
        double f, p, q, t;
        i = ((int) h / 60) % 6;
        f = h / 60d - i;
        p = v * (1d - s);
        q = v * (1d - f * s);
        t = v * (1d - (1d - f) * s);
        switch (i) {
            case 0: {
                r = v;
                g = t;
                b = p;
                break;
            }

            case 1: {
                r = q;
                g = v;
                b = p;
                break;
            }

            case 2: {
                r = p;
                g = v;
                b = t;
                break;
            }

            case 3: {
                r = p;
                g = q;
                b = v;
                break;
            }

            case 4: {
                r = t;
                g = p;
                b = v;
                break;
            }

            default: {
                // case 5
                r = v;
                g = p;
                b = q;
                break;
            }
        }
        return new Rgb((int) Math.round(r * 255), (int) Math.round(g * 255), (int) Math.round(b * 255));
    }

    // HSL - RGB
    @SuppressWarnings("DuplicatedCode")
    public Hsl RGBToHSL(Rgb rgb) {
        int max, min;
        double h, s, l;
        max = Math.max(Math.max(rgb.r(), rgb.g()), rgb.b());
        min = Math.min(Math.min(rgb.r(), rgb.g()), rgb.b());
        l = (max + min) / 255d / 2d;
        if (max == min || Basic.decimalEquals(l, 0d)) {
            s = 0d;
        } else if (l <= 0.5d) {
            s = (max - min) / (double) (max + min);
        } else {
            s = (max - min) / (double) (510 - (max + min));
        }
        if (max == min) {
            h = 0d;
        } else {
            if (max == rgb.r()) {
                h = (double) (60 * (rgb.g() - rgb.b())) / (max - min);
                if (h < 0d) h += 360d;
            } else if (max == rgb.g()) {
                h = 120d + (double) (60 * (rgb.b() - rgb.r())) / (max - min);
            } else {
                //if (max == rgb.b())
                h = 240d + (double) (60 * (rgb.r() - rgb.g())) / (max - min);
            }
        }
        return new Hsl(h, s * 100d, l * 100d);
    }

    public Rgb HSLToRGB(Hsl hsl) {
        double hue = hsl.h();
        double saturation = hsl.s();
        double lightness = hsl.l();
        int[] cRgb = new int[3], tRgb = new int[3];
        double[] sRgb = new double[3];
        double q, p;
        if (Basic.decimalEquals(saturation, 0d)) {
            for (int i = 0; i < cRgb.length - 1; i++)
                cRgb[i] = (int) Math.round(lightness * 255d / 100d);
        } else {
            if (lightness <= 50d) {
                q = (lightness * (100d + saturation) / 10000d);
            } else {
                q = ((lightness + saturation) / 100d - lightness * saturation / 10000d);
            }

            p = (2d * lightness / 100d - q);
            tRgb[0] = (int) Math.round(hue + 120d);
            tRgb[1] = (int) Math.round(hue);
            tRgb[2] = (int) Math.round(hue - 120d);
            for (int i = 0; i < tRgb.length - 1; i++) {
                if (tRgb[i] < 0) {
                    tRgb[i] += 360;
                } else if (tRgb[i] > 360) {
                    tRgb[i] -= 360;
                }

                if (tRgb[i] < 60) {
                    sRgb[i] = p + (q - p) * (6 * tRgb[i] / 360d);
                } else if (tRgb[i] < 180) {
                    sRgb[i] = q;
                } else if (tRgb[i] < 240) {
                    sRgb[i] = p + (q - p) * (6 * (240 - tRgb[i]) / 360d);
                } else {
                    sRgb[i] = p;
                }
                cRgb[i] = Basic.fRound(sRgb[i] * 255d);
            }
        }

        return new Rgb(cRgb[0], cRgb[1], cRgb[2]);
    }

    // HSB/HSV - HSL
    public Hsl HSBToHSL(Hsb hsb) {
        double s, l;
        l = hsb.b() * (200d - hsb.s()) / 200d;
        if (Basic.decimalEquals(l, 0d) || Basic.decimalEquals(l, 100d)) {
            s = 0d;
        } else {
            s = (hsb.b() - l) / Math.min(l, 100d - l) * 100d;
        }
        return new Hsl(hsb.h(), s, l);
    }

    public Hsb HSLToHSB(Hsl hsl) {
        double s, b;
        b = hsl.l() + hsl.s() * Math.min(hsl.l(), 100d - hsl.l()) / 100d;
        s = Basic.decimalEquals(b, 0d) ? 0d : 200d - 200d * hsl.l() / b;
        return new Hsb(hsl.h(), s, b);
    }

    // CMYK - RGB
    public Cmyk RGBToCMYK(Rgb rgb) {
        int c, m, y, k;
        // RGB转CMYK
        c = 255 - rgb.r();
        m = 255 - rgb.g();
        y = 255 - rgb.b();
        k = Math.min(Math.min(c, m), y);
        // CMYK色彩修正
        if (k == 255) {
            c = Basic.fRound(c / 255d * 100d);
            m = Basic.fRound(m / 255d * 100d);
            y = Basic.fRound(y / 255d * 100d);
            k = 100;
        } else {
            c = Basic.fRound((c - k) / (double) (255 - k) * 100d);
            m = Basic.fRound((m - k) / (double) (255 - k) * 100d);
            y = Basic.fRound((y - k) / (double) (255 - k) * 100d);
            k = Basic.fRound(k / 255d * 100d);
        }
        return new Cmyk(c, m, y, k);
    }

    public Rgb CMYKToRGB(Cmyk cmyk) {
        int r, g, b;
        r = (int) Math.round((double) (225 * (100 - cmyk.c()) * (100 - cmyk.k())) / 10000d);
        g = (int) Math.round((double) (225 * (100 - cmyk.m()) * (100 - cmyk.k())) / 10000d);
        b = (int) Math.round((double) (225 * (100 - cmyk.y()) * (100 - cmyk.k())) / 10000d);
        return new Rgb(r, g, b);
    }

    // YCrCb - RGB
    public YCrCb RGBToYCrCb(Rgb rgb) {
        final int delta = 128;
        double y, cr, cb;
        y = (rgb.r() * 299 + rgb.g() * 587 + rgb.b() * 114) / 1000d;
        cr = (500000 * rgb.r() - 418688 * rgb.g() - 81312 * rgb.b()) / 1000000d + delta;
        cb = (-168736 * rgb.r() - 331264 * rgb.g() + 500000 * rgb.b()) / 1000000d + delta;
        return new YCrCb(Basic.fRound(y), Basic.fRound(cr), Basic.fRound(cb));
    }

    public Rgb YCrCbToRGB(YCrCb yCrCb) {
        final int delta = 128;
        double r, g, b;
        r = yCrCb.y() + 1.402d * (yCrCb.cr() - delta);
        g = yCrCb.y() - 0.344136d * (yCrCb.cb() - delta) - 0.714136d * (yCrCb.cr() - delta);
        b = yCrCb.y() + 1.772d * (yCrCb.cb() - delta);
        return new Rgb(Basic.fRound(r), Basic.fRound(g), Basic.fRound(b));
    }

    // XYZ - RGB
    public Xyz RGBToXYZ(Rgb rgb) {
        // Observer = 2°, Illuminant = D65
        double x, y, z, cR, cG, cB;
        // Gamma calculation for RGB
        // Original Gamma formula:
        // n > 0.04045 ? (n + 0.055) / 1.055 ^ 2.4 : n / 12.92
        if (rgb.r() > 10) {
            cR = Math.pow((rgb.r() / 255d + 0.055d) / 1.055d, 2.4d);
        } else {
            cR = (rgb.r() * 10) / 32946d;
        }
        if (rgb.g() > 10) {
            cG = Math.pow((rgb.g() / 255d + 0.055d) / 1.055d, 2.4d);
        } else {
            cG = (rgb.g() * 10) / 32946d;
        }
        if (rgb.b() > 10) {
            cB = Math.pow((rgb.b() / 255d + 0.055d) / 1.055d, 2.4d);
        } else {
            cB = (rgb.b() * 10) / 32946d;
        }
        // XYZ calculation
        x = cR * 0.4124d + cG * 0.3576d + cB * 0.1805d;
        y = cR * 0.2126d + cG * 0.7152d + cB * 0.0722d;
        z = cR * 0.0193d + cG * 0.1192d + cB * 0.9505d;
        return new Xyz(x, y, z);
    }

    public Rgb XYZToRGB(Xyz xyz) {
        // Observer = 2°, Illuminant = D65
        double cR, cG, cB;
        cR = xyz.x() * 3.2406d - xyz.y() * 1.5372d - xyz.z() * 0.4986d;
        cG = xyz.x() * -0.9689d + xyz.y() * 1.8758d + xyz.z() * 0.0415d;
        cB = xyz.x() * 0.0557d - xyz.y() * 0.204d + xyz.z() * 1.057d;
        // Reverse Gamma calculation
        if (cR > 0.0031308d) {
            cR = Math.pow(cR, 0.4166667d) * 1.055d - 0.055d;
        } else {
            cR *= 12.92d;
        }
        if (cG > 0.0031308d) {
            cG = Math.pow(cG, 0.4166667d) * 1.055d - 0.055d;
        } else {
            cG *= 12.92d;
        }
        if (cB > 0.0031308d) {
            cB = Math.pow(cB, 0.4166667d) * 1.055d - 0.055d;
        } else {
            cB *= 12.92d;
        }
        return new Rgb(Basic.fRound(cR * 255), Basic.fRound(cG * 255), Basic.fRound(cB * 255));
    }

    // CIE-Lab - XYZ
    public Lab XYZToLab(Xyz xyz) {
        double x = xyz.x();
        double y = xyz.y();
        double z = xyz.z();
        double fX, fY, fZ, cL, cA, cB;
        x /= 0.950456d;
        z /= 1.088754d;
        if (x > 0.008856d) {
            fX = Math.pow(x, 0.333333d);
        } else {
            fX = 7.787d * x + 0.137931d;
        }
        if (y > 0.008856d) {
            fY = Math.pow(y, 0.333333d);
        } else {
            fY = 7.787d * y + 0.137931d;
        }
        if (z > 0.008856d) {
            fZ = Math.pow(z, 0.333333d);
        } else {
            fZ = 7.787d * z + 0.137931d;
        }
        // 计算CIE-Lab
        if (y > 0.008856d) {
            cL = 116d * fY - 16d;
        } else {
            cL = 903.3d * y;
        }
        cA = 500d * (fX - fY);
        cB = 200d * (fY - fZ);
        return new Lab(cL, cA, cB);
    }

    public Xyz LabToXYZ(Lab lab) {
        double l = lab.l();
        double a = lab.a();
        double b = lab.b();
        double x, y, z, fX, fY, fZ;
        // Y and f(Y)
        if (l > 7.99959d) {
            // Calculate f(Y) first
            fY = (l + 16d) / 116d;
            if (fY > 0.2068927d) {
                y = Math.pow(fY, 3d);
            } else {
                y = (fY - 0.137931d) / 7.787d;
            }
        } else {
            // Calculate Y first
            y = l / 903.3d;
            if (y > 0.008856d) {
                fY = Math.pow(y, 0.333333d);
            } else {
                fY = 7.787d * y + 0.137931d;
            }
        }
        // f(X) and f(Z)
        fX = a / 500d + fY;
        fZ = fY - b / 200d;
        // X and Z
        if (fX > 0.2068927d) {
            x = Math.pow(fX, 3d);
        } else {
            x = (fX - 0.137931d) / 7.787d;
        }
        if (fZ > 0.2068927d) {
            z = Math.pow(fZ, 3d);
        } else {
            z = (fZ - 0.137931d) / 7.787d;
        }
        x *= 0.950456d;
        z *= 1.088754d;
        return new Xyz(x, y, z);
    }

}
