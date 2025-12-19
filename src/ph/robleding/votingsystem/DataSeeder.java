package ph.robleding.votingsystem;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.*;
import ph.robleding.votingsystem.util.CandidateExporter;
import ph.robleding.votingsystem.util.FileUtil;
import ph.robleding.votingsystem.util.VoterExporter;

import java.util.ArrayList;
import java.util.List;

public class DataSeeder {

    public static void main(String[] args) {
        seedAdmins();
        seedVotersAndCandidates();
        System.out.println("✅ Data seeded successfully!");
    }

    private static void seedAdmins() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin("admin1", "NCR", "Quezon City", "1970-01-01", "adminpass"));
        admins.add(new Admin("superadmin", "NCR", "Manila", "1969-12-31", "root"));

        FileUtil.saveToFile("admins.dat", admins);
        System.out.println("✅ Admins seeded");
    }

    private static void seedVotersAndCandidates() {
        List<Voter> voters = new ArrayList<>();
        List<Candidate> candidates = new ArrayList<>();

        // 🗳️ Sample Voters
        Voter voter1 = new Voter("Juan Dela Cruz", "NCR", "Quezon City", "2000-05-15", "juan123");
        Voter voter2 = new Voter("Maria Clara", "NCR", "Manila", "1999-09-09", "maria321");
        Voter voter3 = new Voter("Jose Rizal", "CALABARZON", "Calamba", "1998-06-19", "josehero");
        Voter voter4 = new Voter("Andres Bonifacio", "NCR", "Tondo", "1997-11-30", "revolta");
        Voter voter5 = new Voter("Maridel Roble", "Cebu", "Mandaue", "1990-03-15", "123");
        Voter voter6 = new Voter("Marianito Roble", "Cebu", "Danao", "1988-08-13", "123");
        Voter voter7 = new Voter("Jowen Marfil", "Bohol", "Tagbilaran", "1995-07-20", "123");

        voters.add(voter1);
        voters.add(voter2);
        voters.add(voter3);
        voters.add(voter4);
        voters.add(voter5);
        voters.add(voter6);
        voters.add(voter7);

        // 🇵🇭 PRESIDENTIAL CANDIDATES
        candidates.add(new Candidate("Bongbong Marcos", "Ilocos Norte", "Batac", "1957-09-13", "bbm2022",
                Position.PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Leni Robredo", "Camarines Sur", "Naga", "1964-04-23", "leni2022",
                Position.PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Manny Pacquiao", "Sarangani", "Kiamba", "1978-12-17", "pacman2022",
                Position.PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Isko Moreno", "NCR", "Manila", "1974-10-24", "yorme2022",
                Position.PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Ping Lacson", "Cavite", "Imus", "1948-06-01", "ping2022",
                Position.PRESIDENT, "Nationwide"));

        // 🇵🇭 VICE PRESIDENTIAL CANDIDATES
        candidates.add(new Candidate("Sara Duterte", "Davao del Sur", "Davao City", "1978-05-31", "inday2022",
                Position.VICE_PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Kiko Pangilinan", "Rizal", "San Juan", "1963-08-24", "kiko2022",
                Position.VICE_PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Tito Sotto", "NCR", "Manila", "1948-08-24", "tito2022",
                Position.VICE_PRESIDENT, "Nationwide"));
        candidates.add(new Candidate("Willie Ong", "Pampanga", "San Fernando", "1963-10-24", "willie2022",
                Position.VICE_PRESIDENT, "Nationwide"));

        // 🏛️ SENATORIAL CANDIDATES
        candidates.add(new Candidate("Loren Legarda", "Antique", "San Jose", "1960-01-28", "loren2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Chiz Escudero", "Sorsogon", "Sorsogon City", "1969-10-10", "chiz2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Risa Hontiveros", "NCR", "Quezon City", "1966-02-24", "risa2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Raffy Tulfo", "NCR", "Manila", "1960-03-12", "raffy2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Jinggoy Estrada", "NCR", "San Juan", "1963-02-17", "jinggoy2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Robin Padilla", "Camarines Sur", "Naga", "1969-11-23", "robin2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Alan Cayetano", "NCR", "Taguig", "1970-10-28", "alan2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("JV Ejercito", "NCR", "San Juan", "1969-09-03", "jv2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Villar Mark", "Las Piñas", "Las Piñas City", "1985-03-13", "mark2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Gatchalian Win", "Valenzuela", "Valenzuela City", "1974-10-17", "win2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Bong Go", "Davao del Sur", "Davao City", "1974-06-14", "bong2022",
                Position.SENATOR, "Nationwide"));
        candidates.add(new Candidate("Joel Villanueva", "Bulacan", "Bocaue", "1963-03-11", "joel2022",
                Position.SENATOR, "Nationwide"));

        // 🏛️ CEBU PROVINCIAL CANDIDATES
        candidates.add(new Candidate("Gwen Garcia", "Cebu", "Barili", "1962-02-02", "gwen2022",
                Position.GOVERNOR, "Cebu"));
        candidates.add(new Candidate("Agnes Magpale", "Cebu", "Balamban", "1960-05-15", "agnes2022",
                Position.GOVERNOR, "Cebu"));

        candidates.add(new Candidate("Hilario Davide III", "Cebu", "Danao", "1959-12-20", "junjun2022",
                Position.VICE_GOVERNOR, "Cebu"));

        // 🏛️ BOHOL PROVINCIAL CANDIDATES
        candidates.add(new Candidate("Erico Aumentado", "Bohol", "Jagna", "1973-01-01", "aris2022",
                Position.GOVERNOR, "Bohol"));

        candidates.add(new Candidate("Victor De La Serna", "Bohol", "Tagbilaran", "1965-08-10", "victor2022",
                Position.VICE_GOVERNOR, "Bohol"));

        // 🏛️ MANDAUE CITY CANDIDATES
        candidates.add(new Candidate("Jonas Cortes", "Cebu", "Mandaue", "1975-06-15", "jonas2022",
                Position.MAYOR, "Mandaue City"));
        candidates.add(new Candidate("Glenn Bercede", "Cebu", "Mandaue", "1970-09-20", "glenn2022",
                Position.MAYOR, "Mandaue City"));

        candidates.add(new Candidate("Carlo Fortuna", "Cebu", "Mandaue", "1978-04-12", "carlo2022",
                Position.VICE_MAYOR, "Mandaue City"));

        // Councilors for Mandaue
        candidates.add(new Candidate("Nerissa Soon-Ruiz", "Cebu", "Mandaue", "1965-11-08", "nerissa2022",
                Position.COUNCILOR, "Mandaue City"));
        candidates.add(new Candidate("Malcolm Sanchez", "Cebu", "Mandaue", "1980-07-22", "malcolm2022",
                Position.COUNCILOR, "Mandaue City"));
        candidates.add(new Candidate("Jimmy Lumapas", "Cebu", "Mandaue", "1972-03-30", "jimmy2022",
                Position.COUNCILOR, "Mandaue City"));

        // 🏛️ CEBU CITY CANDIDATES
        candidates.add(new Candidate("Mike Rama", "Cebu", "Cebu City", "1958-11-07", "mike2022",
                Position.MAYOR, "Cebu City"));
        candidates.add(new Candidate("Margot Osmena", "Cebu", "Cebu City", "1963-09-16", "margot2022",
                Position.MAYOR, "Cebu City"));

        candidates.add(new Candidate("Raymond Garcia", "Cebu", "Cebu City", "1975-03-22", "raymond2022",
                Position.VICE_MAYOR, "Cebu City"));

        // Councilors for Cebu City
        candidates.add(new Candidate("Joy Young", "Cebu", "Cebu City", "1970-05-14", "joy2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Franklyn Ong", "Cebu", "Cebu City", "1968-08-20", "frank2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Jerry Guardo", "Cebu", "Cebu City", "1972-12-10", "jerry2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Nestor Archival", "Cebu", "Cebu City", "1965-06-25", "nestor2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Phillip Zafra", "Cebu", "Cebu City", "1978-04-18", "phillip2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Joel Garganera", "Cebu", "Cebu City", "1969-09-30", "joel2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Jocelyn Pesquera", "Cebu", "Cebu City", "1974-11-05", "jocelyn2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Donaldo Hontiveros", "Cebu", "Cebu City", "1971-02-14", "donaldo2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Pastor Alcover Jr", "Cebu", "Cebu City", "1966-07-22", "pastor2022",
                Position.COUNCILOR, "Cebu City"));
        candidates.add(new Candidate("Raul Alcoseba", "Cebu", "Cebu City", "1973-01-19", "raul2022",
                Position.COUNCILOR, "Cebu City"));

        // ✅ Add all candidates as voters too
        for (Candidate c : candidates) {
            // Check if candidate is not already in voters list
            boolean alreadyVoter = voters.stream()
                    .anyMatch(v -> v.getName().equalsIgnoreCase(c.getName()) &&
                            v.getBirthDate().equals(c.getBirthDate()));

            if (!alreadyVoter) {
                Voter voterFromCandidate = new Voter(
                        c.getName(),
                        c.getProvince(),
                        c.getCityOrMunicipality(),
                        c.getBirthDate(),
                        c.getPassword()
                );
                voters.add(voterFromCandidate);
            }
        }

        // 💾 Save to .dat files
        FileUtil.saveToFile("voters.dat", voters);
        FileUtil.saveToFile("candidates.dat", candidates);

        // ✅ Export to CSV files
        VoterExporter.exportAll(voters);
        CandidateExporter.exportAll(candidates);

        System.out.println("✅ Voters and candidates seeded");
        System.out.println("   - " + voters.size() + " voters (including candidates)");
        System.out.println("   - " + candidates.size() + " candidates");
    }
}