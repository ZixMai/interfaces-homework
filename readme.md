# Тема: «Подписки: биллинг, пауза, шаринг»

## Краткое описание

Это мини-проект про маркетплейс цифровых подписок, где пользователи подключают музыку, видео и облачное хранилище. Нужно
реализовать биллинг за месяц с учётом пробного периода, пауз и совместного использования. Также предусматривается расчёт
стоимости по объёму услуг (например, дополнительный терабайт в облаке) и валидные ограничения (лимиты участников,
корректные даты). Итог — корректные деньги, прозрачные правила списаний и понятные ошибки при нарушениях.

## Цели

## Основные классы (3 шт.)

1. `MusicPlan` — музыкальная подписка
2. `VideoPlan` — видеосервис
3. `CloudPlan` — облачное хранилище

Базовый родитель:

* `abstract class Subscription`

    * поля: `String id`, `String title`, `BigDecimal monthlyPrice`, `LocalDate startDate`, `boolean active`
    * методы: `activate()`, `cancel()`, `isActive()`, `price()`

Интерфейсы (поведения):

* `Billable`

  ```java
  interface Billable {
      float monthlyCharge(int forMonth); // расчёт списания за месяц (с учётом статуса/скидок)
  }
  ```
* `Pausable`

  ```java
  interface Pausable {
      void pause(int from, int to); // прекратить подписку с from до to 
      boolean isPausedOn(int date); // проверить, прекращена ли дата на момент времени
  }
  ```
* `Sharable`

  ```java
  // интерфейс для предоставления доступа людям к подписке
  interface Sharable {
      int maxProfiles();
      void addMember(String userId);
      void removeMember(String userId);
  }
  ```
* `TrialSupport`

  ```java
  // интерфейс для подписок с пробным периодом
  interface TrialSupport {
      int trialDays();
      boolean isInTrial(int date);
  }
  ```

## Правила по типам

### MusicPlan implements Billable, TrialSupport

* 14 дней trial (0₽). После trial человек платит полную цену.
* Поле: `int trialDays = 14`.

### VideoPlan implements Billable, Pausable, Sharable

* Можно ставить на паузу (пауза — 0₽ за дни паузы, пропорционально месяцу).
* Так как реализуется sharable, то должно быть до `maxProfiles` участников.
* Доп. поле: `int maxProfiles = 4`.

### CloudPlan implements Billable

* Базовая цена за 1 ТБ. За каждый начатый доп. ТБ — доплата.
* Поля: `int storageTb`, `int baseTbPrice`, `int extraTbPrice`.

## Подсказки по сигнатурам

```java
class Subscription {
    private  String id;
    private  String title;
    protected BigDecimal monthlyPrice;
    private  LocalDate startDate;
    protected boolean active;
}

class MusicPlan extends Subscription implements Billable, TrialSupport {
    private  int trialDays = 14;

    @Override
    public BigDecimal monthlyCharge(LocalDate forMonth) { ...}

    @Override
    public int trialDays() {
        return trialDays;
    }

    @Override
    public boolean isInTrial(LocalDate date) { ...}
}

class VideoPlan extends Subscription implements Billable, Pausable, Sharable {
    private  int maxProfiles = 4;
    private  String[] members= new String[];
    private  PauseWindow[] pauses = new Pauses[5];

    @Override
    public int monthlyCharge(int forMonth) { /* пропорция активных дней */ }

    @Override
    public void pause(int from, int to) { ...}

    @Override
    public boolean isPausedOn(int date) { ...}

    @Override
    public int maxProfiles() {
        return maxProfiles;
    }

    @Override
    public void addMember(String userId) { ...}

    @Override
    public void removeMember(String userId) { ...}
}
```
