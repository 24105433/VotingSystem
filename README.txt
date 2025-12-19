# 🗳️ Robleding Voting System

A comprehensive console-based electronic voting system built with Java, designed to simulate Philippine electoral processes with multi-level election support (national, provincial, and municipal).

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Installation & Setup](#installation--setup)
- [Usage Guide](#usage-guide)
- [User Roles](#user-roles)
- [Electoral Positions](#electoral-positions)
- [Data Management](#data-management)
- [Technical Details](#technical-details)
- [Project Structure](#project-structure)
- [Contributors](#contributors)

---

## 🎯 Overview

The Robleding Voting System is a secure, file-based electronic voting application that manages the complete electoral process from voter registration to result tallying. The system supports multiple electoral levels and implements proper authentication, vote validation, and data persistence.

### Key Highlights

- ✅ Multi-level elections (National, Provincial, Municipal)
- 🔐 Secure password hashing (SHA-256)
- 💾 Persistent data storage (.dat files)
- 📊 CSV export capabilities
- 🎫 Vote receipt generation
- 👥 Role-based access control (Admin/Voter)
- 🗂️ Comprehensive candidate management

---

## ✨ Features

### For Voters
- **Voter Registration**: Self-registration with validation (18+ age requirement)
- **Secure Login**: Password-protected authentication
- **Position-Based Voting**: Vote for candidates by electoral position
- **Location-Aware Ballots**: Only see candidates eligible in your location
- **Multiple Selection**: Choose up to 12 senators and 10 councilors
- **Vote Receipt**: View detailed voting record after submission
- **One Person, One Vote**: System prevents duplicate voting

### For Administrators
- **Election Management**: Start/stop elections
- **Candidate Administration**: Add, disqualify, view candidates
- **Voter Masterlist**: View voters by country, province, or city
- **Real-time Tally**: View vote counts by position and location
- **Data Export**: Export voters, candidates, and results to CSV
- **System Maintenance**: Rebuild vote counts, purge data
- **Raw Data Access**: View all system data files with credentials

---

## 🏗️ System Architecture

### Package Structure

```
ph.robleding.votingsystem
├── App.java                    # Application entry point
├── DataSeeder.java             # Sample data generator
│
├── model/                      # Data models
│   ├── User.java               # Abstract base class for all users
│   ├── Admin.java              # Administrator model
│   ├── Voter.java              # Voter model
│   ├── Candidate.java          # Candidate model (extends Voter)
│   ├── Vote.java               # Vote record model
│   └── ElectionState.java      # Election status tracker
│
├── service/                    # Business logic layer
│   ├── UserService.java        # User authentication & management
│   ├── CandidateService.java   # Candidate operations
│   ├── VoteService.java        # Vote processing & validation
│   └── ElectionService.java    # Election state management
│
├── ui/                         # User interface layer
│   ├── ConsoleUI.java          # Main console interface
│   └── PasswordField.java      # Secure password input
│
├── util/                       # Utility classes
│   ├── FileUtil.java           # File I/O operations
│   ├── FileConstants.java      # File path constants
│   ├── ElectionConstants.java  # System constants
│   ├── InputValidator.java     # Input validation
│   ├── PasswordUtil.java       # Password hashing
│   ├── VoterExporter.java      # Voter CSV export
│   ├── CandidateExporter.java  # Candidate CSV export
│   └── ResultExporter.java     # Election results export
│
└── enums/                      # Enumerations
    └── Position.java           # Electoral positions enum
```

### Data Flow

```
User Input → ConsoleUI → Service Layer → Model Layer → File Storage
                ↓                           ↓
          Validation              State Management
```

---

## 🚀 Installation & Setup

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Command line terminal or IDE (IntelliJ IDEA, Eclipse, VS Code)

### Setup Steps

1. **Clone or Download the Project**
   ```bash
   git clone <repository-url>
   cd robleding-voting-system
   ```

2. **Compile the Project**
   ```bash
   javac -d bin ph/robleding/votingsystem/**/*.java
   ```

3. **Seed Initial Data** (Optional but recommended)
   ```bash
   java ph.robleding.votingsystem.DataSeeder
   ```

   This creates:
   - 2 admin accounts
   - 7 sample voters
   - 60+ candidates across all positions
   - CSV export files

4. **Run the Application**
   ```bash
   java ph.robleding.votingsystem.App
   ```

### Default Admin Credentials

After running the DataSeeder, you can login as:

| Username     | Password   |
|-------------|-----------|
| admin1      | adminpass |
| superadmin  | root      |

### Sample Voter Credentials

| Name               | Password | Location           |
|--------------------|----------|--------------------|
| Juan Dela Cruz     | juan123  | Quezon City, NCR   |
| Maria Clara        | maria321 | Manila, NCR        |
| Jose Rizal         | josehero | Calamba, CALABARZON|
| Andres Bonifacio   | revolta  | Tondo, NCR         |
| Maridel Roble      | 123      | Mandaue, Cebu      |

---

## 📖 Usage Guide

### Main Menu Flow

```
┌─────────────────────────────┐
│      Main Menu              │
├─────────────────────────────┤
│ 1. Login                    │
│ 2. Register as Voter        │
│ 3. Exit                     │
└─────────────────────────────┘
```

### Voter Workflow

1. **Register** (if new voter)
   - Provide: Name, Province, City, Birth Date (YYYY-MM-DD), Password
   - System validates age (must be 18+)

2. **Login**
   - Enter name and password
   - System authenticates credentials

3. **Vote** (when election is ongoing)
   - View candidates by position
   - Select candidates (single choice for most positions)
   - Choose up to 12 Senators and 10 Councilors
   - Review and confirm ballot
   - Receive vote receipt

4. **View Receipt**
   - Check recorded votes anytime after voting

### Admin Workflow

1. **Login** with admin credentials

2. **Manage Election**
   - Start election (enables voting)
   - Stop election (disables voting)
   - View current status

3. **Manage Candidates**
   - View all candidates or filter by position/location
   - Add new candidates manually
   - Disqualify candidates (when election is stopped)

4. **View Tally**
   - Nationwide results
   - Filter by specific position
   - Filter by province or city
   - Real-time vote counts

5. **View Voters Masterlist**
   - Whole country view
   - Filter by province
   - Filter by city/municipality
   - See voting status

6. **System Maintenance**
   - Rebuild vote tally from vote records
   - Export data to CSV files
   - Purge all data (with confirmation)

7. **View Raw Data Files**
   - View all voters with passwords
   - View all admins with passwords
   - View all candidates data
   - View all vote records
   - View election state

---

## 👥 User Roles

### Admin
- Manages election lifecycle
- Oversees candidates and voters
- Views real-time results
- Performs system maintenance
- **Cannot vote**

### Voter
- Registers in the system
- Casts votes during elections
- Views personal vote receipt
- **One vote per election**

### Candidate
- Registered as voters first
- Can be added by admin
- Has associated position and location
- Can be disqualified/withdrawn/conceded
- Accumulates votes during election

---

## 🏛️ Electoral Positions

### National Level (All voters can vote)
- **President** (1 choice)
- **Vice President** (1 choice)
- **Senator** (up to 12 choices)

### Provincial Level (Location-specific)
- **Governor** (1 choice)
- **Vice Governor** (1 choice)

### Municipal/City Level (Location-specific)
- **Mayor** (1 choice)
- **Vice Mayor** (1 choice)
- **Councilor** (up to 10 choices)

### Vote Validation Rules

1. **Location Matching**: Voters only see candidates from their province/city
2. **Age Requirement**: Must be 18 years or older to register
3. **Single Vote**: One complete ballot per voter per election
4. **Candidate Status**: Cannot vote for disqualified or withdrawn candidates
5. **Selection Limits**:
   - Senator: Maximum 12 selections
   - Councilor: Maximum 10 selections
   - Others: Single selection only

---

## 💾 Data Management

### File Storage Structure

```
📁 Project Root
├── voters.dat              # Binary: Voter records
├── candidates.dat          # Binary: Candidate records
├── votes.dat               # Binary: Vote records
├── election.dat            # Binary: Election state
├── admins.dat              # Binary: Admin accounts
├── voters.csv              # CSV: Voter export
├── candidates.csv          # CSV: Candidate export
└── results.csv             # CSV: Election results
```

### Data Persistence

- **Binary Files (.dat)**: Java serialization for efficient storage
- **CSV Files**: Human-readable exports for external use
- **Auto-save**: Changes persist immediately
- **Transaction Safety**: Vote counts rebuild from vote records

### CSV Export Formats

**voters.csv**
```
Name,Province,City/Municipality,BirthDate,HasVoted
Juan Dela Cruz,NCR,Quezon City,2000-05-15,YES
```

**candidates.csv**
```
Name,Province,City/Municipality,BirthDate,Position,Location,Disqualified,Withdrawn,Conceded
Bongbong Marcos,Ilocos Norte,Batac,1957-09-13,PRESIDENT,Nationwide,NO,NO,NO
```

**results.csv**
```
Position,Candidate,Location,Votes,Conceded,Withdrawn,Disqualified
PRESIDENT,Bongbong Marcos,Nationwide,150,false,false,false
```

---

## 🔧 Technical Details

### Security Features

1. **Password Hashing**
   - Algorithm: SHA-256
   - Base64 encoding
   - No plain text storage

2. **Authentication**
   - Hash comparison for login
   - Session-based access control
   - Role-based permissions

3. **Vote Integrity**
   - One vote per voter enforcement
   - Vote record immutability
   - Vote-count verification system

### Input Validation

- **Name**: Minimum 2 characters, letters/spaces/hyphens/apostrophes only
- **Location**: Minimum 2 characters, letters/spaces/periods
- **Password**: Minimum 6 characters
- **Birth Date**: ISO format (YYYY-MM-DD), 18+ years old
- **Position**: Enum-based validation
- **Vote Count**: Numeric validation with range checks

### Design Patterns Used

1. **MVC Architecture**: Separation of UI, business logic, and data models
2. **Service Layer Pattern**: Encapsulated business logic
3. **Singleton Behavior**: Single scanner instance, centralized file access
4. **Strategy Pattern**: Different voting rules per position type
5. **Factory Pattern**: Position enum with fromString() method

### Data Structures

- **Lists**: Voters, Candidates, Admins storage
- **Maps**: Vote records (voterId → Vote object)
- **Enums**: Electoral positions with string conversion
- **Sets**: Duplicate prevention in multi-selection voting

### Key Algorithms

1. **Vote Counting**
   - Real-time increment on vote casting
   - Rebuild capability from vote records
   - Support for comma-separated multiple selections

2. **Location Filtering**
   - Normalized city name comparison
   - Case-insensitive matching
   - Removal of "City"/"Municipality" suffixes

3. **Sorting**
   - Vote tally: Highest votes first, then alphabetical
   - Candidate lists: By position, then name
   - Voter masterlist: Province → City → Last name

---

## 📂 Project Structure

### Core Components

**App.java**
- Application entry point
- Initializes ConsoleUI

**DataSeeder.java**
- Generates sample data
- Creates admin accounts
- Populates candidates across all positions
- Exports initial CSV files

**ConsoleUI.java** (Main Interface - 34KB)
- Handles all user interactions
- Menu navigation
- Input collection and validation
- Display formatting

### Model Layer

**User.java** (Abstract)
- Base class for all user types
- ID generation (UUID)
- Authentication logic
- Location information

**Voter.java**
- Extends User
- Tracks voting status
- Basic voter information

**Candidate.java**
- Extends Voter (candidates are voters too)
- Position and location
- Vote counting
- Status flags (disqualified, withdrawn, conceded)

**Vote.java**
- Voter ID reference
- Position → Candidate name mapping
- Receipt generation

**Admin.java**
- Extends User
- Administrator privileges

**ElectionState.java**
- Tracks if election is ongoing
- Serializable for persistence

### Service Layer

**UserService.java**
- User authentication
- Voter registration
- Admin management
- CSV export for voters

**CandidateService.java**
- Candidate CRUD operations
- Position-based queries
- Vote count management
- Disqualification/withdrawal/concession

**VoteService.java**
- Vote casting
- Duplicate vote prevention
- Receipt retrieval
- Vote record persistence

**ElectionService.java**
- Election start/stop
- State management
- Status queries

### Utility Layer

**FileUtil.java**
- Generic file I/O for serialization
- List and single object persistence
- Error handling

**PasswordUtil.java**
- SHA-256 hashing
- Base64 encoding

**InputValidator.java**
- Date validation
- Age verification
- Name/location validation
- Password strength
- Number range validation

**VoterExporter.java**
- CSV export for voters
- Append and bulk export

**CandidateExporter.java**
- CSV export for candidates
- CSV import from file

**ResultExporter.java**
- Election results to CSV
- Position-based grouping

**ElectionConstants.java**
- Max senators: 12
- Max councilors: 10
- Min name length: 2
- Min password length: 6
- Min voting age: 18
- UI formatting constants

**FileConstants.java**
- File path definitions
- Centralized file naming

### Enums

**Position.java**
- 8 electoral positions
- String conversion utility

---

## 🎓 Learning Objectives Demonstrated

### Object-Oriented Programming
- ✅ Inheritance (User → Voter → Candidate)
- ✅ Abstract classes (User)
- ✅ Polymorphism (getRole() method)
- ✅ Encapsulation (private fields, public methods)
- ✅ Interfaces (Serializable)

### Data Structures
- ✅ ArrayList (dynamic collections)
- ✅ HashMap (vote storage)
- ✅ HashSet (duplicate prevention)
- ✅ Enum (position types)

### File I/O
- ✅ Object serialization
- ✅ CSV file handling
- ✅ File existence checking
- ✅ Append operations

### Software Design
- ✅ MVC architecture
- ✅ Service layer pattern
- ✅ Separation of concerns
- ✅ Code reusability
- ✅ Constants management

### Algorithm Design
- ✅ Searching and filtering
- ✅ Sorting (multi-level)
- ✅ Vote counting
- ✅ Input validation
- ✅ String manipulation

---

## 📊 Sample Output

### Voter Interface
```
🗳️ BALLOT
══════════════════════════════════════════════════════════════════════
Enter the NUMBER of your choice (or press Enter to skip)
For SENATOR and COUNCILOR, you can select multiple candidates.
══════════════════════════════════════════════════════════════════════

📌 PRESIDENT
──────────────────────────────────────────────────────────────────────
  [1] Bongbong Marcos (Nationwide)
  [2] Leni Robredo (Nationwide)
  [3] Manny Pacquiao (Nationwide)
  [4] Isko Moreno (Nationwide)
  [5] Ping Lacson (Nationwide)
Your choice: 2
✓ Selected: Leni Robredo

📌 SENATOR
──────────────────────────────────────────────────────────────────────
Select up to 12 senators (enter numbers separated by commas, e.g., 1,3,5):
Your choices: 1,3,5,7,9,11
✓ Selected 6 senators: Loren Legarda, Risa Hontiveros, Jinggoy Estrada...
```

### Admin Tally View
```
📊 Tally for Cebu

📌 Position: GOVERNOR
   - Gwen Garcia (Cebu): 245 votes
   - Agnes Magpale (Cebu): 189 votes

📌 Position: MAYOR
   - Jonas Cortes (Mandaue City): 156 votes
   - Glenn Bercede (Mandaue City): 98 votes
```

---

## ⚠️ Known Limitations

1. **Single Election**: System supports only one active election at a time
2. **No Network**: Console-based, single-machine operation
3. **No GUI**: Text-based interface only
4. **File-Based**: Not suitable for large-scale deployments
5. **Manual Backup**: No automatic data backup mechanism

---

## 🔮 Future Enhancements

- [ ] Multi-election support with election IDs
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Web-based interface
- [ ] Real-time vote monitoring dashboard
- [ ] Voter pre-registration approval workflow
- [ ] Blockchain vote verification
- [ ] Mobile app integration
- [ ] Automated backup system
- [ ] Advanced analytics and reporting
- [ ] Email notification system

---

## 📝 Development Notes

### Testing Recommendations

1. **User Registration**
   - Test with valid and invalid dates
   - Test age validation (under 18)
   - Test duplicate registration prevention

2. **Voting Process**
   - Test single position voting
   - Test multiple selection (Senator, Councilor)
   - Test location filtering
   - Test duplicate vote prevention

3. **Admin Functions**
   - Test election start/stop
   - Test candidate disqualification
   - Test vote tally accuracy
   - Test data export

4. **Data Persistence**
   - Test system restart with existing data
   - Test vote count rebuild
   - Test CSV import/export

### Maintenance Tasks

- Regular backup of .dat files
- Periodic CSV exports for audit
- Vote count verification
- System log review

---

## 🤝 Contributors

- **Developer**: Rob Leding
- **Course**: [Your Course Name]
- **Institution**: [Your Institution]
- **Date**: December 2025

---

## 📄 License

This project is created for educational purposes as part of academic coursework.

---

## 📞 Support

For questions or issues:
- Check the code documentation
- Review this README thoroughly
- Contact: ph.robleding@example.com (update with actual contact)

---

## 🙏 Acknowledgments

- Philippine electoral system inspiration
- Java community for best practices
- Academic instructors and peers

---

**Made with ☕ and 💻 in the Philippines**

*"Technology empowering democracy, one vote at a time."*
