# Session 2 Write a Proof of Concept for Moon Phases

**Goal of this lesson:** add a `getMoonPhase()` method that reports all eight moon phases, while keeping the existing `isNewMoon()`. We will use an enum to do this. Since enums may be new for you, we will spend a little extra time on that.

---

## Warm up

Run `./gradlew test` and confirm no errors before changing anything. Starting from a known state is a good habit.
Commit and push your code to GitHub, if you have not done this already.

---

## Step 1 Understanding enums

An enum is a type with a fixed, named set of values, such as the days of the week. Before enums were introduced in Java 5, Java code used `int` or `String` constants for this:

```  
public static final int STATUS_NEW = 0;  
public static final int STATUS_SHIPPED = 1;  
public static final int STATUS_DELIVERED = 2;  
```

That works, but a method that takes an `int status` will accept any int value.. An enum fixes this: the only legal values are the ones you list, and passing anything else is a compile error.

## **Declaring and using an enum**

The simplest enum is just a list of names. By convention, the names are written in upper case, like other constants.

```  
public enum OrderStatus {  
    NEW,   
    SHIPPED,  
    DELIVERED,   
    CANCELLED  
}  
```

An enum is really a special kind of class, and **each value you list is an instance of that class**. `OrderStatus.NEW` is an `OrderStatus` object, created automatically when the class loads. Every enum extends `java.lang.Enum`, which provides some built-in methods that all enums can use.

You refer to a value through the type name, and you can use the type anywhere you'd use any other type: fields, parameters, return values, and collections.   
```  
OrderStatus status = OrderStatus.NEW;

public void updateStatus(OrderStatus newStatus) {  
    this.status = newStatus;  
}  
```

You can never create an instance with new:  
```  
// Compiler error  
OrderStatus status = new OrderStatus();  
```

Each enum value exists exactly once in the running program, created when the class is loaded by the JVM. That means you can compare enum values with `==` rather than `.equals()`. Also, `status == OrderStatus.NEW` won't throw an exception if `status` is `null`.

## **Built-in methods**

Every enum gets a handful of useful methods for free.

| Method | What it does | Example Result|
|----|----|----|
| values() | Returns all values (enum instances), in declaration order |OrderStatus.values() returns an array of enum instances [NEW, SHIPPED, DELIVERED, CANCELLED[|  
| valueOf(String)| Converts a string to a value (enum instance) | valueOf("SHIPPED") returns OrderStatus.SHIPPED|  
|name()| Converts a value (enum instance) to a string | [OrderStatus.SHIPPED.name](http://OrderStatus.SHIPPED.name)() returns "SHIPPED"|  
|toString()| Same as name(), unless you override it| OrderStatus.SHIPPED.toString() returns "SHIPPED"|  
|ordinal()| returns the value's position, counting from 0| OrderStatus.SHIPPED.ordinal() returns 1

`values()` is handy for looping over every option, for example to fill a drop-down list:

```  
for (OrderStatus s : OrderStatus.values()) {  
    System.out.println(s);  
}  
```

## **Adding fields, constructors, and methods**

Because an enum is a class, it can contain data and methods. Each value can pass arguments to the constructor, much like calling `new`.

```  
public enum OrderStatus {  
    NEW("New order", false),  
    SHIPPED("On its way", false),  
    DELIVERED("Delivered", true),  
    CANCELLED("Cancelled", true);   // note the semicolon

    private final String label;  
    private final boolean finished;

    OrderStatus(String label, boolean finished) {  
        this.label = label;  
        this.finished = finished;  
    }

    public String getLabel() { return label; }  
    public boolean isFinished() { return finished; }  
}  
```

A few rules to notice in this example:

* The list of values must come first in the declaration. When anything follows it, the list ends with a semicolon.  
* The constructor is always private, even without the keyword. You can't write `new OrderStatus(...)`; the listed values are the only instances that will ever exist.  
* The fields are `final`. Every part of the program shares the same `DELIVERED` object, so its data should never change.

## **Enums in switch**

Enums and `switch` work well together. Inside the `case` labels you write just the value name, without the type prefix.

```  
// standard form for switches  
switch (status) {  
    case NEW:   
        System.out.println("Preparing your order");  
        break;  
    case SHIPPED:   
          System.out.println("Track your package");  
          break;  
    case DELIVERED:  
    case CANCELLED:  
        System.out.println("Order closed");  
        break;  
}  
```

```  
// new format, starting with Java 14  
switch (status) {  
    case NEW -> System.out.println("Preparing your order");  
    case SHIPPED -> System.out.println("Track your package");  
    case DELIVERED, CANCELLED -> System.out.println("Order closed");  
}

// alternate new format, assign a value directly to String  
String message = switch (status) {  
    case NEW -> "Preparing your order";  
    case SHIPPED -> "Track your package";  
    case DELIVERED, CANCELLED -> "Order closed";  
};  
```

## **A few things to watch out for**

You don't need to master these yet, but they catch people out in real projects.

* **Don't rely on `ordinal()`.** The number changes if anyone reorders or inserts a value. Never save it to a database or file. If you need a stable code, add your own field for it.  
* **`valueOf` is strict.** It's case-sensitive, so `valueOf("shipped")` fails. It throws an exception for an unknown name, so handle that when the text comes from a user or a file.  
* **Renaming a value can break things.** Saved data, JSON, and database columns often store the name. A rename is a bigger change than it looks.  
* **Keep enums immutable.** Each value is shared across the whole program, so a changeable field behaves like a global variable.  
* **Override `toString()` for display text, not `name()`.** `name()` can't be overridden, and other code may depend on it matching the declaration.

## **When and how to use enums**

Consider using an enum whenever a value must be one of a small, known set of choices that you know when you write the code. Good signs include:

* You're about to write several related `int` or `String` constants.  
* A method parameter only makes sense for a few specific values, like a size of `SMALL`, `MEDIUM`, or `LARGE`.  
* You're comparing strings to decide what to do, such as `if (role.equals("admin"))`. A typo in a string compiles fine; a typo in an enum doesn't.  
* You have a `boolean` that may grow a third option. `status == ACTIVE` reads better than `isActive`, and it leaves room for `SUSPENDED` later.

Don't use an enum when the set of values changes while the program runs or is managed by users or data. Product categories stored in a database, or a list of countries loaded from a file, belong in data, not in code. Adding a value to an enum means changing and redeploying the program.

When you do write one, keep it simple:

1. Start with just the names. Add fields and methods only when calling code keeps asking the same question about a value.  
2. Keep fields `final` and give them getters, with no setters.  
3. Compare values with `==`.  
4. Use a switch expression that lists every value, so the compiler tells you when a new value needs handling.  
5. Store and send the name, or your own code field, never the ordinal.

## Step 2 The `MoonPhase` enum

The Moon has Eight phases, but they aren't all the same kind of thing. New, First Quarter, Full, and Last quarter last  for a single day. The rest are the days between each phase.

```  
package org.example;

/**  
 * The eight phases of the lunar cycle, declared in the order they occur.  
 * There are 4 principal phases (NEW, …) and 4 phases that span multiple days (WAXING_CRESCENT, …)  
 */  
public enum MoonPhase {  
    NEW,  
    WAXING_CRESCENT,  
    FIRST_QUARTER,  
    WAXING_GIBBOUS,  
    FULL,  
    WANING_GIBBOUS,  
    LAST_QUARTER,  
    WANING_CRESCENT;  
}
```


---

## Step 3 Update MoonPhaseCalculator

```  
public interface MoonPhaseCalculator {
    /**
     * Legacy method to determine if the given date falls on a new moon
     * @param date the date to check
     * @return true if new moon, otherwise false
     */
    default boolean isNewMoon(LocalDate date) {
        return MoonPhase.NEW_MOON == getMoonPhase(date);
    }
    /**
     * Determine the phase of the moon on the given date
     * @param date the date to check
     * @return the MoonPhase corresponding to the date
     */
    MoonPhase getMoonPhase(LocalDate date);
} 
```

**Some things to notice:**

- **There is now exactly one source of truth.** `isNewMoon()` can't disagree with `getPhase()`, because it asks `getPhase()`. Any implementation added later gets a correct `isNewMoon()`.  
- **Using `default` in an interface method.** A default interface method contains code. You can override it in a derived class, or leave it in place.  
- **Adding `getMoonPhase()` to the interface breaks every derived class.** You will need to add this to MoonPhaseCalculatorBasic..  
- **Last session's test still passes, untouched.** You changed the internals of `isNewMoon()` and a test you wrote a week ago confirmed you didn't break it. That is the main reason for writing tests, to check for regressions.

---

## Step 4 Write the failing test, and load the data

As always, the test comes first. Pick a date you know is a waxing crescent — a day or two after a new moon in your table:  
```  
@Test  
void dayAfterNewMoonIsWaxingCrescent() {  
    MoonPhaseCalculator calculator = new MoonPhaseCalculatorBasic();  
    MoonInfo info = calculator.getPhase(LocalDate.of(2026, 9, 13));  
    assertEquals(Phase.WAXING_CRESCENT, info.phase());  
}  
```

Write a second one for a principal phase itself — the new moon date — asserting `Phase.NEW`. Those two cases are the whole problem in miniature: *on* a published date, and *between* two of them.

### The data

The table holds all 8 of the phases. Every day calculation is worked out from them.

Source: [https://aa.usno.navy.mil/data/MoonPhases](https://aa.usno.navy.mil/data/MoonPhases)

```  
// Principal moon phases, 2026-2027, from the US Naval Observatory.  
// Source times are UTC. We treat each phase as covering the whole UTC day  
// it falls on. That is a simplification: a phase reached at 23:50 UTC really  
// only describes the last ten minutes of that day. Good enough until we take  
// time zones seriously in a later lesson.  
   private static final String[[ moonPhases = {  
        // new             1st qtr              full                   last qtr  
        "2026-09-10", "2026-09-18", "2026-09-26", "2026-10-03",  
        "2026-10-10", "2026-10-18", "2026-10-26", "2026-11-01",  
        "2026-11-09", "2026-11-17", "2026-11-24", "2026-12-01",  
        "2026-12-09", "2026-12-17", "2026-12-24", "2026-12-30",  
        "2027-01-07", "2027-01-15", "2027-01-22", "2027-01-29",  
        "2027-02-06", "2027-02-14", "2027-02-20", "2027-02-28",  
        "2027-03-08", "2027-03-15", "2027-03-22", "2027-03-30"  
    };  
```

---

## Step 5 Make the test pass

In `getPhase()`, first exclude all dates outside of the range of the table. You can use `localDate.isBefore()` and
`localDate.isAfter()` for this.

Next, write a loop that compares the date parameter to every value in the table of moonPhases until a match is found.
If the date matches a table entry, the answer is that phase. 
Otherwise, check whether the date falls between the current table date and the next table date. 
If that happens, the answer is the corresponding transitional phase.
Because you already checked for dates outside the table range, you are guaranteed to find a match.

```  
    @Override
    public MoonPhase getMoonPhase(LocalDate date) {
        // if date is out of range of our table, just return null
        if (date.isBefore(LocalDate.parse(moonPhases[0])) ||
        date.isAfter(LocalDate.parse(moonPhases[moonPhases.length-1]))) {
            return null;
        }
        for (int i = 0, phaseIndx = 0; i < moonPhases.length; i += 2, phaseIndx=(phaseIndx+1)%4) {
            if (date.isEqual(LocalDate.parse(moonPhases[i]))) {
                return MoonPhase.values()[phaseIndx];
            } else if (isSpanPhase(i, date)) {
                return MoonPhase.values()[phaseIndx];
            }
            // what if it is neither? This can never happen
        }
        return null;
    }

    private boolean isSpanPhase(int i, LocalDate date) {
        return date.isAfter(LocalDate.parse(moonPhases[i])) &&
                date.isBefore(LocalDate.parse(moonPhases[i+1]));
    }

```

It's clumsy and verbose, but it works. A working ugly version you can refactor beats an elegant one you're still debugging.

---

## Step 6 Commit and push

Save your work to your GitHub repository.
