package unisiegen.photographers.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by aboden on 16.07.14.
 */

@XStreamAlias("lens")
public class Lens {

    public String name;
    public String camera;

    public Lens(String name, String camera) {
        this.name = name;
        this.camera = camera;
    }

    public Lens() {
        // Empty constructor needed for XStream Library.
    }
}
