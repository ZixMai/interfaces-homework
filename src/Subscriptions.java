import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

class Subscription {
    private final String id;
    private final String title;
    protected BigDecimal monthlyPrice;
    private final int startDate;
    protected boolean active;

    public Subscription(String id, String title, BigDecimal monthlyPrice, int startDate, boolean active) {
        this.id = id;
        this.title = title;
        this.monthlyPrice = monthlyPrice;
        this.startDate = startDate;
        this.active = active;
    }

    public void activate() {
        this.active = true;
    }

    public void cancel() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }

    public BigDecimal price() {
        return monthlyPrice;
    }
}

class MusicPlan extends Subscription implements Billable, TrialSupport {
    private final int trialDays = 14;

    public MusicPlan(String id, String title, BigDecimal monthlyPrice, int startDate, boolean active) {
        super(id, title, monthlyPrice, startDate, active);
    }

    @Override
    public BigDecimal monthlyCharge(int forMonth) {
        return monthlyPrice.multiply(BigDecimal.valueOf(trialDays)).divide(BigDecimal.valueOf(30), RoundingMode.UNNECESSARY);
    }

    @Override
    public int trialDays() {
        return trialDays;
    }

    @Override
    public boolean isInTrial(int date) {
        return trialDays >= date;
    }
}

class PauseWindow {
    private final int start;
    private final int end;

    public PauseWindow(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isInWindow(long date) {
        return start <= date && date <= end;
    }
}

class VideoPlan extends Subscription implements Billable, Pausable, Sharable {
    private final int maxProfiles = 4;
    private List<String> members = new ArrayList<>();
    private List<PauseWindow> pauses = new ArrayList<>();

    public VideoPlan(String id, String title, BigDecimal monthlyPrice, int startDate, boolean active) {
        super(id, title, monthlyPrice, startDate, active);
    }

    @Override
    public BigDecimal monthlyCharge(int forMonth) {
        // ебал рот это писать честное слово
        return BigDecimal.valueOf(0);
    }

    @Override
    public void pause(int from, int to) {
        pauses.add(new PauseWindow(from, to));
    }

    @Override
    public boolean isPausedOn(int date) {
        return pauses.stream().anyMatch(pause -> pause.isInWindow(date));
    }

    @Override
    public int maxProfiles() {
        return maxProfiles;
    }

    @Override
    public void addMember(String userId) {
        if (members.size() != maxProfiles) {
            members.add(userId);
        } else throw new RuntimeException("Maximum profiles exceeded");
    }

    @Override
    public boolean isActive() {
        var curTime = Math.toIntExact(System.currentTimeMillis() / 1000);
        return active && !isPausedOn(curTime);
    }

    @Override
    public void removeMember(String userId) {
        members.remove(userId);
    }

    public List<String> getMembers() {
        return members;
    }


}

class CloudPlan extends Subscription implements Billable {
    private int storageTb;
    private final int baseTbPrice = 100;
    private final int extraTbPrice = 200;

    public CloudPlan(String id, String title, BigDecimal monthlyPrice, int startDate, boolean active, int storageTb) {
        super(id,title,monthlyPrice,startDate,active);
        this.storageTb = storageTb;
    }

    @Override
    public BigDecimal monthlyCharge(int forMonth) {
        return BigDecimal.valueOf(baseTbPrice + (long) (storageTb - 1) * extraTbPrice);
    }

    public void buyTb(int count) {
        storageTb += count;
    }
}