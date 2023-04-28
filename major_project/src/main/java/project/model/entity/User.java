package project.model.entity;

/**
 * The api user with related status
 * Serve as a record with attributes token, status, and user tier
 */
public class User implements Entity {
    private String token;
    private String status;
    private String userTier;

    public User(String status, String userTier) {
        this.status = status;
        this.userTier = userTier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public String getUserTier() {
        return userTier;
    }

    @Override
    public String getEntityInfo() {
        String info = "Current User: " + token + "\n" +
                "User Tier: " + userTier + "\n" +
                "Status: " + status + "\n";

        return info;
    }
}
