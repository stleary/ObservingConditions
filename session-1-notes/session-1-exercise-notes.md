# Session 1 Create a Project That Checks New Moon Dates

**Goal of this lesson:** Create a Git repo containing a Gradle build, a wrapper, one deliberately failing test, and the crudest code that makes it pass.

**Not a goal:** complete check for moon phases. We only need a sample of test data at this time. This will be fixed in future lessons.

---

## Step 1 Project setup

You will need these tools and accounts: 

- JDK 21 (at least v17 for recent gradle versions)  
- IntelliJ IDEA Community Edition (or VSCode, Eclipse, etc)  
- Gradle (v7.3 or greater, needed once to generate the gradlew wrapper)  
- Git, and a GitHub account with an empty repo created


Create a new GitHub project (ObservingConditions or similar). Make it public and select options to include a readme, .gitignore for Java, and a license (MIT recommended)

Clone the repo to your laptop, build it once, then commit to GitHub  
```
git clone <get your GitHub URL by clicking the Code button>
cd ObservingConditions
gradle init 		# allow creation in a non-empty folder, then accept all default values.
                # Your choice whether to use Groovy or Kotlin
./gradlew build # this is a sanity check, that everything is set up and working
# Important! You have to tell .gitignore to store the gradle-wrapper.jar file in the repo
# Add this line to .gitignore: !gradle/wrapper/gradle-wrapper.jar
git add .  
git commit -m "main initial commit"
git push 		# Keep the repo in sync with the code
```
---

**Some things to notice about build.gradle:**

- **Why do we use Gradle (or Maven)?**   
  - Combines all setup steps into a single build tool (reproducibility)  
  - Provides a way to specify, download, and use external libraries (dependency management)  
  - Creates a standard lifecycle with compile/test/package steps (continuous integration)  
- **`plugins` vs `dependencies`.** One extends the build tool; the other is code the program uses. New developers tend to conflate these.  
- **`testImplementation`.** JUnit isn't shipped to production. What would happen if `implementation` was used instead?  
- **The three coordinates.** `group:name:version`. This is the same addressing scheme behind every Gradle dependency. Group: the company name. Name: the app name. Version: the release version. Note: This appears only in the Groovy *build.gradle*. For Kotlin, you must look in *gradle/libs.versions.toml*  
- **`mavenCentral()` in a Gradle file.** Note that Central refers to a repository, but Maven is just another build tool. Gradle only uses the former, and ignores the latter.

**Why we commit a script (gradlew) whose only job is to download a build tool (gradle).** Anyone who clones this builds it with the *same* Gradle version, having installed nothing. This means that anyone can recreate your build with the same Gradle software versions you used.

From here on out, we will only use `./gradlew`, never `gradle`.  
---

## Step 2 Write a failing test

**Do this before you write any actual code.**

Create `MoonPhaseCalculatorBasicTest` and write one test against a real new-moon date:
```
@Test 
void knownNewMoonIsIdentifiedAsNew() {
    MoonPhaseCalculator calculator \= new MoonPhaseCalculator();  
    assertTrue(calculator.isNewMoon(LocalDate.of(2026, 9, 10)));
}

Execute *./gradlew test*  
   
It won't compile because `MoonPhaseCalculator` doesn't exist. A compilation failure is a legitimate red. The unit test drives the class into existence along with its method signature. You have just designed an API before implementing it.

Let the IDE stub the class and method. Just throw an exception from the method. Run and watch it fail for the *right* reason now.

### Data Source

**Pull some New Moon dates from the US Naval Observatory:**  
[https://aa.usno.navy.mil/data/MoonPhases](https://aa.usno.navy.mil/data/MoonPhases)

Bring over dates from 2026 and 2027\. For example,  
**year	month	day**  
2026	9	10

**We write the test first for several reasons:** 

- The design doc maps cleanly to the test cases, so you can go from design, to test, to implementation without forgetting anything.  
- It guarantees that you will have at least some unit tests in place. For many developer jobs, this is a requirement.  
- It improves the app because you are not trying to code everything at once. This helps you write modular, well-structured code, one section at a time.

---

## Step 3 Make the test pass, crudely

Create a new class, MoonPhaseCalculatorBasic, that extends MoonPhaseCalculator. Include a private static final List of the date strings from your data source, using this format: "2026-09-10", …

Override the isNewMoon() method and check whether the incoming date is in your list. Return true if it is found, otherwise return false.

Roughly where they should land:  
```
public class MoonPhaseCalculatorBasic extends MoonPhaseCalculator {

    private static final String\[\] newMoonDates \= {
        "2026-01-17", "2026-02-16", "2026-03-18", "2026-04-16", "2026-05-16",
        "2026-06-14", "2026-07-14", "2026-08-12", "2026-09-10", "2026-10-10",  
        "2026-11-08", "2026-12-08", "2027-01-06", "2027-02-05", "2027-03-07",   
        "2027-04-05", "2027-05-05", "2027-06-03", "2027-07-03", "2027-08-01",   
        "2027-08-31", "2027-09-30", "2027-10-30", "2027-11-28", "2027-12-28"  
    };   
    private static final List\<String\> newMoonDates2026And2027 \= List.of(newMoonDates);

    @Override
    public boolean isNewMoon(LocalDate date) {
        boolean result \= false;  
        // convert localdate to a date string in the format yyyy-MM-dd  
        String dateString \= date.toString();  
        if (newMoonDates2026And2027.contains(dateString)) {
            result \= true;
        }
        return result;
    }
}
```
Finally, update your test to create a MoonPhaseCalculatorBasic, and execute the build again.

**Understanding the MoonPhaseCalculator classes:**

- You probably have not used *LocalDate* yet. Dates and times are problematic in Java, and in most other programming languages. It gets complicated quickly, so you only need to learn enough to solve the current problem.  
- We use a base class that does nothing except throw an exception. This is to notify us if we ever accidentally create the wrong object in a test. The *MoonPhaseCalculatorBasic* class is our first implementation that actually checks the date and returns a result. We will derive another class later, which will find a better way to find the new Moon.  
- Could you implement the base class as an interface? You can\! This works better because it makes it clear to the developer that we are not actually implementing anything in this class. Try it and see. Notice that the error of creating an instance of the interface is now a compile-time error, instead of a run-time exception. Letting errors like this found at compile time instead of test time is a valid design choice. 

---

## Step 4 Commit and push

Include a comment with the commit that explains the changes in 1 sentence. Confirm it's visible on GitHub. Notice that many working files have been created, but are not included in the commit. This is because they are filtered out by .gitignore, which we created for Java apps, when the repo was first set up. View the contents of .gitignore to confirm.

---

## Success criteria

By now you should be able to say, unprompted:

- why `gradlew` is committed but `build/` is not  
- what happens if a dependency is declared `testImplementation` instead of `implementation`  
- why the test was written before the class existed

## Homework

1. Add some tests with invalid new Moon dates, and make sure they fail.  
2. Try a valid 2028 date, and observe the test fail (it will always return false)

