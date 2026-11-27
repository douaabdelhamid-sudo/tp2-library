package library.model;

public class StudentMember extends Member {
    private final String level;

    public StudentMember(String memberNumber, String name, String email, String level) {
        super(memberNumber, name, email);
        this.level = level;
    }

    @Override
    public int maxLoans() {
        return 5;
    }

    public String getLevel() {
        return level;
    }
}