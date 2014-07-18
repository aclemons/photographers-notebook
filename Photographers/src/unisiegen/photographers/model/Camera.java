package unisiegen.photographers.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by aboden on 16.07.14.
 */

@XStreamAlias("camera")
public class Camera {

    public String name;
    public int value;
    public int def;

    public Camera(String name, int value, int def) {
        this.name = name;
        this.value = value;
        this.def = def;
    }

    public Camera() {
        // Empty constructor needed for XStream Library.
    }

}
