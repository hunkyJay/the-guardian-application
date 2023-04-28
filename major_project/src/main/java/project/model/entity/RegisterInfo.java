package project.model.entity;

/**
 * The register information for registering the api token
 * Serve as a record with attributes register information, and link
 */
public class RegisterInfo implements Entity{
    String information;
    String link;

    public RegisterInfo(String information, String link) {
        this.information = information;
        this.link = link;
    }

    @Override
    public String getEntityInfo() {
        return information;
    }

    public String getLink(){
        return link;
    }

}
