package unisiegen.photographers.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;

/**
 * Created by aboden on 17.07.14.
 */
@XStreamAlias("equipment-set")
public class Equipment {

    @XStreamAlias("photographers-notebook-version")
    public String version = "";
    @XStreamAlias("lenses")
    public ArrayList<Lens> lenses = new ArrayList<Lens>();
    @XStreamAlias("cameras")
    public ArrayList<Camera> cameras = new ArrayList<Camera>();
    @XStreamAlias("film-format")
    public ArrayList<Setting> filmFormat = new ArrayList<Setting>();
    @XStreamAlias("film-empfindlichkeit")
    public ArrayList<Setting> filmEmpfindlichkeit = new ArrayList<Setting>();
    @XStreamAlias("brennweite")
    public ArrayList<Setting> brennweite = new ArrayList<Setting>();
    @XStreamAlias("nahzubehoer")
    public ArrayList<Setting> nahzubehoer = new ArrayList<Setting>();
    @XStreamAlias("filter")
    public ArrayList<Setting> filter = new ArrayList<Setting>();
    @XStreamAlias("blitz")
    public ArrayList<Setting> blitz = new ArrayList<Setting>();
    @XStreamAlias("sonder")
    public ArrayList<Setting> sonder = new ArrayList<Setting>();
    @XStreamAlias("fokus")
    public ArrayList<Setting> fokus = new ArrayList<Setting>();
    @XStreamAlias("blende")
    public ArrayList<Setting> blende = new ArrayList<Setting>();
    @XStreamAlias("zeit")
    public ArrayList<Setting> zeit = new ArrayList<Setting>();
    @XStreamAlias("messung")
    public ArrayList<Setting> messung = new ArrayList<Setting>();
    @XStreamAlias("plusminus")
    public ArrayList<Setting> plusminus = new ArrayList<Setting>();
    @XStreamAlias("makro")
    public ArrayList<Setting> makro = new ArrayList<Setting>();
    @XStreamAlias("makro-vf")
    public ArrayList<Setting> makrovf = new ArrayList<Setting>();
    @XStreamAlias("filter-vf")
    public ArrayList<Setting> filterVF= new ArrayList<Setting>();
    @XStreamAlias("makro-vf2")
    public ArrayList<Setting> makroVF2 = new ArrayList<Setting>();
    @XStreamAlias("filter-vf2")
    public ArrayList<Setting> filterVF2 = new ArrayList<Setting>();
    @XStreamAlias("blitzkorr")
    public ArrayList<Setting> blitzKorr = new ArrayList<Setting>();
    @XStreamAlias("film-typ")
    public ArrayList<Setting> filmTyp = new ArrayList<Setting>();

    public Equipment() {
        // Empty constructor needed for XStream library.
    }

}
