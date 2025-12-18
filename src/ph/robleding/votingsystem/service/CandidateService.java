package ph.robleding.votingsystem.service;

import ph.robleding.votingsystem.enums.Position;
import ph.robleding.votingsystem.model.Candidate;
import ph.robleding.votingsystem.model.Voter;
import ph.robleding.votingsystem.util.CandidateExporter;
import ph.robleding.votingsystem.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateService {
    private final String FILE = "candidates.dat";
    private final String CSV_FILE = "candidates.csv";

    private final List<Candidate> candidates;

    public CandidateService() {
        List<Candidate> loaded = FileUtil.loadFromFile(FILE);

        if (loaded == null || loaded.isEmpty()) {
            System.out.println("📥 Loading candidates from CSV...");
            loaded = CandidateExporter.importFromCSV(CSV_FILE);

            if (!loaded.isEmpty()) {
                System.out.println("✅ Candidates loaded from CSV: " + loaded.size());
                FileUtil.saveToFile(FILE, loaded); // 🔁 Cache for future runs
            } else {
                System.out.println("⚠️ No candidates found in CSV.");
            }
        }

        this.candidates = loaded != null ? loaded : new ArrayList<>();
    }

    public void saveCandidates() {
        FileUtil.saveToFile(FILE, candidates);
        CandidateExporter.exportAll(candidates); // 🔄 Keep CSV in sync
    }

    public boolean isCandidateAlreadyRegistered(Voter voter, Position position) {
        return candidates.stream()
            .anyMatch(c -> c.getName().equalsIgnoreCase(voter.getName())
                    && c.getBirthDate().equals(voter.getBirthDate())
                    && c.getPosition() == position);
    }

    public boolean submitCandidacy(Voter voter, Position position, String location) {
        if (isCandidateAlreadyRegistered(voter, position)) {
            return false;
        }

        Candidate c = new Candidate(
            voter.getName(),
            voter.getProvince(),
            voter.getCityOrMunicipality(),
            voter.getBirthDate(),
            voter.getPassword(),   // or "dummy" if you want
            position,
            location
        );

        candidates.add(c);
        saveCandidates();
        return true;
    }


    public boolean withdraw(String candidateName, Position position) {
        for (Candidate c : candidates) {
            if (c.getName().equalsIgnoreCase(candidateName) && c.getPosition() == position) {
                c.withdraw();
                saveCandidates();
                return true;
            }
        }
        return false;
    }

    public boolean concede(String candidateName, Position position) {
        for (Candidate c : candidates) {
            if (c.getName().equalsIgnoreCase(candidateName) && c.getPosition() == position) {
                c.concede();
                saveCandidates();
                return true;
            }
        }
        return false;
    }

    public void disqualify(String candidateName, Position position) {
        for (Candidate c : candidates) {
            if (c.getName().equalsIgnoreCase(candidateName) && c.getPosition() == position) {
                c.disqualify();
                break;
            }
        }
        saveCandidates();
    }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        saveCandidates();
    }

    public List<Candidate> getAllCandidates() {
        return candidates;
    }

    public List<Candidate> getCandidatesByPosition(Position position) {
        return candidates.stream()
                .filter(c -> c.getPosition() == position && !c.isDisqualified() && !c.hasWithdrawn())
                .collect(Collectors.toList());
    }
}
