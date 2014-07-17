package unisiegen.photographers.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;

/**
 * Created by aboden on 17.07.14.
 */
@XStreamAlias("equipment-Set")
public class Equipment {
    @XStreamAlias("lenses")
    public ArrayList<Lens> lenses = new ArrayList<Lens>();
    @XStreamAlias("cameras")
    public ArrayList<Camera> cameras = new ArrayList<Camera>();

    public Equipment() {
        // Empty constructor needed for XStream library.
    }

}
