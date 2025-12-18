package ph.robleding.votingsystem.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class User implements Serializable {

    private final String id;
    private final String name;
    private final String province;
    private final String cityOrMunicipality;
    private final String birthDate;
    private String password;
    
    public String getPassword() {
        return password;
    }

    protected User(
            String name,
            String province,
            String cityOrMunicipality,
            String birthDate,
            String password
    ) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.province = province;
        this.cityOrMunicipality = cityOrMunicipality;
        this.birthDate = birthDate;
        this.password = password;
    }

    // 🔐 Authentication
    public boolean authenticate(String passwordInput) {
        return this.password.equals(passwordInput);
    }

    // 🎭 Role (ADMIN / VOTER / CANDIDATE)
    public abstract String getRole();

    // 🔎 Getters (read-only identity data)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getCityOrMunicipality() {
        return cityOrMunicipality;
    }

    public String getBirthDate() {
        return birthDate;
    }

    // 🔑 Password management
    public void setPassword(String password) {
        this.password = password;
    }
}
