import java.math.BigDecimal;

interface Billable {
    BigDecimal monthlyCharge(int forMonth); // расчёт списания за месяц (с учётом статуса/скидок)
}

interface Pausable {
    void pause(int from, int to); // прекратить подписку с from до to
    boolean isPausedOn(int date); // проверить, прекращена ли дата на момент времени
}

// интерфейс для предоставления доступа людям к подписке
interface Sharable {
    int maxProfiles();
    void addMember(String userId);
    void removeMember(String userId);
}

// интерфейс для подписок с пробным периодом
interface TrialSupport {
    int trialDays();
    boolean isInTrial(int date);
}
