package ph.robleding.votingsystem.model;

public class Admin extends User {

    public Admin(String name,
                 String province,
                 String cityOrMunicipality,
                 String birthDate,
                 String password) {

        super(name, province, cityOrMunicipality, birthDate, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
