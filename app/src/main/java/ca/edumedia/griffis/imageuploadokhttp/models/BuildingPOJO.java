package ca.edumedia.griffis.imageuploadokhttp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for a Building JSON object.
 *
 * Before You Begin - install the Parceable plugin for Android Studio (1)
 *
 * Copy 'n paste the JSON structure for a Building: https://doors-open-ottawa.mybluemix.net/buildings/2
 *
 * Next, use this online tool to generate this POJO class: http://www.jsonschema2pojo.org/
 * Apply these settings:
 *   Package: mad9132.doo
 *   Class name: PlanetPOJO
 *   Target language: Java
 *   Source type: JSON
 *   Annotation style: None
 *   Check:
 *      Use primitive types
 *      Use double numbers
 *      Include getters and setters
 *
 * Finally, implement the Parceable interface (click 'BuildingPOJO' > Code > Generate... > Parceable
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 *
 * Reference
 * 1) "Model response data with POJO classes", Chapter 3. Requesting Data over the Web, Android
 *     App Development: RESTful Web Services by David Gassner
 */

public class BuildingPOJO implements Parcelable {

    private int buildingId;
    private String nameEN;
    private String categoryFR;
    private int categoryId;
    private String categoryEN;
    private String descriptionEN;

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getCategoryFR() {
        return categoryFR;
    }

    public void setCategoryFR(String categoryFR) {
        this.categoryFR = categoryFR;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryEN() {
        return categoryEN;
    }

    public void setCategoryEN(String categoryEN) {
        this.categoryEN = categoryEN;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.buildingId);
        dest.writeString(this.nameEN);
        dest.writeString(this.categoryFR);
        dest.writeInt(this.categoryId);
        dest.writeString(this.categoryEN);
        dest.writeString(this.descriptionEN);
    }

    public BuildingPOJO() {
    }

    protected BuildingPOJO(Parcel in) {
        this.buildingId = in.readInt();
        this.nameEN = in.readString();
        this.categoryFR = in.readString();
        this.categoryId = in.readInt();
        this.categoryEN = in.readString();
        this.descriptionEN = in.readString();
    }

    public static final Creator<BuildingPOJO> CREATOR = new Creator<BuildingPOJO>() {
        @Override
        public BuildingPOJO createFromParcel(Parcel source) {
            return new BuildingPOJO(source);
        }

        @Override
        public BuildingPOJO[] newArray(int size) {
            return new BuildingPOJO[size];
        }
    };
}