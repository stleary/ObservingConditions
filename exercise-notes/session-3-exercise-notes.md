# Session 3 Replace Selenium with a REST API

**Goal of this lesson:** add a new calculator, `MoonPhaseCalculatorRest`, that gets the moon phase from the US Naval Observatory REST API instead of driving a web browser. We will use the JSON-java library to read the response. Since REST and JSON may be new for you, we will spend a little extra time on them.


---

## Warm up

Run `./gradlew test` and confirm no errors before changing anything.
Notice how long it takes. The Selenium test starts Chrome nine times, once for every assertion. 

---

## Step 1 Look at the API in a browser

Paste this into your browser:

[https://aa.usno.navy.mil/api/rstt/oneday?date=2026-09-27&coords=0,0](https://aa.usno.navy.mil/api/rstt/oneday?date=2026-09-27&coords=0,0)

This is the same observatory, and the similar data, that the Selenium calculator reads. The difference is who it is written for. The web page is for people. The API is for programs. Think of a REST API as a web address that returns data instead of a page.

**The parts of the URL:**

| Part | What it is |
|----|----|
| `https://aa.usno.navy.mil` | The server |
| `/api/rstt/oneday` | The path. This picks the service we want: Sun and Moon data for one day |
| `?` | Everything after this is a query parameter |
| `date=2026-09-27` | First parameter, a name and a value |
| `&` | Separates one parameter from the next |
| `coords=0,0` | Second parameter, latitude and longitude |

The API requires `coords` because it also reports rise and set times, which depend on where you are standing. The phase of the Moon does not, so we use `0,0`.

Change the date in the address bar and reload. You are already using the API. All our Java code has to do is the same thing you just did by hand.

---

## Step 2 Understanding JSON

The response is JSON, a text format for structured data. Here is a shortened copy of what you saw in the browser:

```
{
  "apiversion": "4.0.1",
  "properties": {
    "data": {
      "closestphase": {
        "day": 26,
        "month": 9,
        "phase": "Full Moon",
        "time": "16:49",
        "year": 2026
      },
      "curphase": "Waning Gibbous",
      "day": 27,
      "fracillum": "99%",
      "isdst": false,
      "moondata": [
        { "phen": "Set", "time": "06:11" },
        { "phen": "Rise", "time": "18:36" }
      ],
      "month": 9,
      "year": 2026
    }
  },
  "type": "Feature"
}
```

JSON has only a few rules:

* **An object** is wrapped in `{ }` and holds key/value pairs. The key is always a string in double quotes. This is like a Java `Map`.  
* **An array** is wrapped in `[ ]` and holds a list of values. This is like a Java `List`.  
* **A value** can be a string (`"Full Moon"`), a number (`42`), a boolean (`false`), `null`, an object, or an array.  
* Objects and arrays can be nested inside each other, as deep as you like.

**Find the value we want.** It is `curphase`, the current phase. To reach it, start at the outer object and work inward:

```
outer object  ->  "properties"  ->  "data"  ->  "curphase"
```

Our code will follow exactly this path.

---

## Step 3 Add the JSON-java dependency

Java does not include a JSON parser, so we need a library. Add this line to the `dependencies` block of `app/build.gradle.kts`:

```
    // JSON-java library, for parsing JSON
    implementation("org.json:json:20260814")
```

Run `./gradlew build` to download it.

**Some things to notice:**

- **The three coordinates again.** `group:name:version`, the same as Session 1. The group is `org.json`, the name is `json`, and the version is a release date.  
- **`implementation`, not `testImplementation`.** The application itself parses JSON, not just the tests.  
- **You did not download a jar file.** Gradle found it in Maven Central and did that for you.

---

## Step 4 Understanding JSON-java

JSON-java has two main classes. `JSONObject` holds a JSON object, and `JSONArray` holds a JSON array. You create one from some text, and then ask it for values by name.

Here is a small example, not related to the Moon:

```
String jsonText = """
    {
      "orderId": 1234,
      "status": "SHIPPED",
      "gift": false,
      "customer": {
        "name": "Pat",
        "city": "Austin"
      },
      "items": ["telescope", "eyepiece", "star chart"]
    }
    """;

JSONObject order = new JSONObject(jsonText);

int orderId = order.getInt("orderId");                 // 1234
String status = order.getString("status");             // "SHIPPED"
boolean gift = order.getBoolean("gift");               // false

JSONObject customer = order.getJSONObject("customer");
String city = customer.getString("city");              // "Austin"

JSONArray items = order.getJSONArray("items");
String first = items.getString(0);                     // "telescope"
```

The `"""` is a Java text block. It lets you write a string across several lines, with double quotes inside it, and no `\"` or `+` needed. It is very handy for JSON.

## **Common methods**

| Method | What it does | Example Result |
|----|----|----|
| new JSONObject(String) | Parses JSON text into an object | new JSONObject(jsonText) |
| getString(name) | Returns a string value | order.getString("status") returns "SHIPPED" |
| getInt(name) | Returns a number value as an int | order.getInt("orderId") returns 1234 |
| getBoolean(name) | Returns a boolean value | order.getBoolean("gift") returns false |
| getJSONObject(name) | Returns a nested object | order.getJSONObject("customer") |
| getJSONArray(name) | Returns a nested array | order.getJSONArray("items") |
| has(name) | Checks whether a name exists | order.has("coupon") returns false |
| optString(name, default) | Like getString(), but returns the default if the name is missing | order.optString("coupon", "none") returns "none" |

## **A few things to watch out for**

* **Names are case-sensitive.** The API calls it `curphase`, all lower case. If you ask for `curPhase`, you get `JSONException: JSONObject["curPhase"] not found.` Always copy the name from the real response, not from memory.  
* **`get` methods throw, `opt` methods don't.** `getString("coupon")` throws a `JSONException` when the name is missing. `optString("coupon", "none")` returns the default instead. Use `get` when the value must be there, and `opt` when it might not be.  
* **`JSONException` is unchecked.** The compiler will not remind you to catch it.  
* **The type has to match.** In our Moon data, `"day": 27` is a number, but `"fracillum": "99%"` is a string. Calling `getString("day")` throws an exception.

---

## Step 5 Write the failing test

As always, the test comes first. Create a new test class, `MoonPhaseCalculatorRestTest`. This test does not use the network at all. It hands a saved copy of the response to the parsing method:

```
package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoonPhaseCalculatorRestTest {

    // A shortened copy of the real response for 2026-09-27
    private static final String SAMPLE_RESPONSE = """
        {
          "apiversion": "4.0.1",
          "properties": {
            "data": {
              "closestphase": {
                "day": 26,
                "month": 9,
                "phase": "Full Moon",
                "time": "16:49",
                "year": 2026
              },
              "curphase": "Waning Gibbous",
              "day": 27,
              "fracillum": "99%",
              "month": 9,
              "year": 2026
            }
          },
          "type": "Feature"
        }
        """;

    @Test
    void parsesCurphaseFromResponse() {
        MoonPhaseCalculatorRest calculator = new MoonPhaseCalculatorRest();
        assertEquals(MoonPhase.WANING_GIBBOUS, calculator.parseMoonPhase(SAMPLE_RESPONSE));
    }
}
```

Execute `./gradlew test`. It won't compile, because `MoonPhaseCalculatorRest` doesn't exist. Let the IDE create the class and the method, and return `null` from both `getMoonPhase()` and `parseMoonPhase()`. Run it again and watch it fail for the *right* reason: expected `WANING_GIBBOUS` but was `null`.

**Some things to notice:**

- **Why test with a saved response?** A test that needs the internet is slow, and it can fail when your code is fine: the wifi is off, or the observatory's server is down. This test checks *our* code and nothing else.  
- **The variable type is `MoonPhaseCalculatorRest`, not `MoonPhaseCalculator`.** `parseMoonPhase()` is not part of the interface, so the interface type can't see it.

---

## Step 6 Make the test pass

Follow the path from Step 2, one object at a time, then convert the string to a `MoonPhase`. The loop is the same idea as the Selenium calculator, except that we can use `equals()` instead of `contains()`, because the API gives us the phase name and nothing else.

```
    /**
     * Finds the curphase value in the JSON document and converts it to a MoonPhase.
     * Not private, so that the unit test can call it without using the network.
     */
    MoonPhase parseMoonPhase(String json) {
        JSONObject root = new JSONObject(json);
        JSONObject properties = root.getJSONObject("properties");
        JSONObject data = properties.getJSONObject("data");
        String curPhase = data.getString("curphase");

        for (MoonPhase moonPhase : MoonPhase.values()) {
            if (moonPhase.getName().equals(curPhase)) {
                return moonPhase;
            }
        }
        return null;
    }
```

Run the test. It should pass.

**Some things to notice:**

- **No `public` or `private` on the method.** This is package-private. Any class in the `org.example` package can call it, including the test. Nothing outside the package can.  
- **Compare this to the Selenium version.** There the phase was buried in a sentence on a web page, found with the CSS selector `h4 + p + p`. If the observatory redesigns the page, that selector breaks. Here we ask for the value by name.  


---

## Step 7 Call the API

Now the part that uses the network. First, add a second test to `MoonPhaseCalculatorRestTest`. You will need to import `java.time.LocalDate`.

```
    @Test
    void dayAfterFullMoonIsWaningGibbous() {
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorRest();
        assertEquals(MoonPhase.WANING_GIBBOUS, calculator.getMoonPhase(LocalDate.of(2026, 9, 27)));
    }
```

Run it and watch it fail. Then finish the class. Java has had an HTTP client built in since Java 11, so there is no new dependency.

```
package org.example;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Gets the phase of the moon for a given date from the US Naval Observatory REST API
 */
public class MoonPhaseCalculatorRest implements MoonPhaseCalculator {

    private static final String API_URL = "https://aa.usno.navy.mil/api/rstt/oneday";

    @Override
    public MoonPhase getMoonPhase(LocalDate date) {
        try {
            String json = fetchJson(date);
            return parseMoonPhase(json);
        } catch (Exception e) {
            // the site is down, the network is off, or the response was not what we expected
            System.out.println("Unable to get moon phase: " + e);
            return null;
        }
    }

    /**
     * Calls the REST API and returns the response body, which is a JSON document
     */
    private String fetchJson(LocalDate date) throws Exception {
        // date.toString() is already in the yyyy-MM-dd format the API wants
        String url = API_URL + "?date=" + date + "&coords=0,0";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Unexpected status code: " + response.statusCode());
        }
        return response.body();
    }

    // parseMoonPhase() from Step 6 goes here
}
```

Run the tests. Both should pass.

**Some things to notice:**

- **Request and response.** Your code builds a request, sends it, and gets back a response. The response has a status code and a body. This is all your browser did in Step 1.  
- **`GET`.** The HTTP method for "give me some data". It is what the browser sends when you type in an address.  
- **Status code 200 means OK.** You have probably seen 404, which means not found. Anything other than 200 is not the answer we asked for, so we don't try to parse it.  
- **The timeout.** `send()` waits until the server answers. Without a timeout, a server that never answers means a program that never finishes.  
- **`throws Exception`.** `send()` can throw checked exceptions, such as `IOException`. `fetchJson()` passes them up, and `getMoonPhase()` catches them in one place.  
- **Returning `null` when something goes wrong.** This is the same thing the Selenium calculator does. The caller can't tell the difference between "the site is down" and "we don't know this phase". That is not good enough for a real service, and we will come back to it.  
- **Two small methods instead of one big one.** One gets the text and one reads it. This is good design because one method should only do one thing. Also, that split is what let us test the parsing without the network.

---

## Step 8 Swap the calculator in the old test

Open `MoonPhaseCalculatorTest` and change one line in `testPhase()`:

```
        MoonPhaseCalculator calculator = new MoonPhaseCalculatorRest();
```

Run `./gradlew test` and compare the time to what you wrote down in the warm up.

Both calculators get their data from the same observatory, so we expect the same answers. If an assertion fails, don't change it to make the test pass. Paste that date into the browser URL from Step 1 and read what the API returned. Then decide which one is right, and why.

**Some things to notice:**

- **A one-line change.** Nothing else in the test knows or cares which calculator it has. This is the payoff for writing `MoonPhaseCalculator` as an interface back in Session 1.  
- **No Chrome, no `Thread.sleep()`.** We are not waiting for a page to draw. We ask a question and get an answer.  
- **The Selenium class is still there.** We didn't delete working code. We just stopped using it.

---

## Step 9 Commit and push

Save your work to your GitHub repository.

---

## Success criteria

By now you should be able to say, unprompted:

- what the `?` and `&` mean in a URL  
- how to get from the outer JSON object to `curphase`, and what happens if you ask for `curPhase`  
- the difference between `getString()` and `optString()`  
- why one of your new tests does not use the network  

## Homework

1. The loop that turns a name into a `MoonPhase` now appears in two classes. Move it into the `MoonPhase` enum as a static method, `fromName(String name)`, and call it from both calculators. Your tests will tell you if you broke anything.  
2. The response also has `fracillum`, the percent of the Moon that is lit. Write a failing test for a new method, `parseIllumination(String json)`, that returns `99` for the sample response. Then make it pass. Notice that the value is a string with a percent sign, not a number.  
3. Paste the URL into your browser with a date that doesn't exist, like `2026-02-30`. What comes back? What does `getMoonPhase()` do with it?  
4. Turn off your wifi and run `./gradlew test`. Which tests pass, and which fail? Why?
