package com.bookstore.stepdefs;

import com.bookStore.base.User;
import com.bookStore.service.SignInService;
import com.bookStore.service.SignUpService;
import com.bookStore.utils.ExtentReportUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.Assert.*;

public class AuthTestNGTests {

    private int uniqueId;
    private String uniqueUsername;
    private String password;

    private final Map<String, String> usernameMap = new HashMap<>();
    private final Map<String, Integer> idMap = new HashMap<>();
    private Response response;

    @BeforeClass
    public void initTestData() {
        ExtentReportUtil.initReport();
        ExtentReportUtil.createTest("Auth API TestNG Suite");
        uniqueId = generateRandomId();
        uniqueUsername = generateRandomUsername();
        password = generateRandomPassword();
    }

    @Test(priority = 1)
    public void testLoginWithoutSignup() {
        User user = new User(generateRandomId(), generateRandomUsername(), generateRandomPassword());
        response = SignInService.login(user);
        int status = response.getStatusCode();
        assertEquals(status, 401);
        ExtentReportUtil.step("INFO", "Login attempt with non-existent user");
    }

    @Test(priority = 2)
    public void testLoginWithMissingParams() {
        User user = new User(0, "", "vakq");
        response = SignInService.login(user);
        assertEquals(response.getStatusCode(), 422);
        ExtentReportUtil.step("INFO", "Login with missing email parameter");
    }

    @Test(priority = 3)
    public void testSignupWithValidCredentials() {
        User user = new User(uniqueId, uniqueUsername, password);
        response = SignUpService.signUp(user);
        assertEquals(response.getStatusCode(), 200);
        String message = JsonPath.from(response.getBody().asString()).getString("message");
        assertEquals(message, "User signed up successfully");
        ExtentReportUtil.step("PASS", "Signup successful: " + uniqueUsername);
    }

    @Test(priority = 4)
    public void testLoginWithValidCredentials() {
        User user = new User(uniqueId, uniqueUsername, password);
        response = SignInService.login(user);
        assertEquals(response.getStatusCode(), 200);
        String token = JsonPath.from(response.getBody().asString()).getString("access_token");
        assertNotNull(token);
        ExtentReportUtil.step("PASS", "Login successful, token received: " + token);
    }

    @Test(priority = 5)
    public void testSignupWithDuplicateEmail() {
        User user = new User(uniqueId + 1, uniqueUsername, password);
        response = SignUpService.signUp(user);
        assertEquals(response.getStatusCode(), 409); // Assuming conflict
        ExtentReportUtil.step("INFO", "Signup with duplicate email: " + user.getEmail());
    }

    @Test(priority = 6)
    public void testSignupWithPasswordOnly() {
        User user = new User(uniqueId + 2, "", password);
        response = SignUpService.signUp(user);
        assertEquals(response.getStatusCode(), 422);
        ExtentReportUtil.step("INFO", "Signup with missing email");
    }

    @Test(priority = 7)
    public void testDynamicUserSignupAndLogin() {
        String prefix = "dynamic";
        String dynamicUsername = prefix + "_" + generateRandomUsername();
        int dynamicId = generateRandomId();
        usernameMap.put(prefix, dynamicUsername);
        idMap.put(prefix, dynamicId);

        User signupUser = new User(dynamicId, dynamicUsername, password);
        Response signupResp = SignUpService.signUp(signupUser);
        assertEquals(signupResp.getStatusCode(), 200);
        ExtentReportUtil.step("PASS", "Dynamic user signed up: " + dynamicUsername);

        User loginUser = new User(dynamicId, dynamicUsername, password);
        Response loginResp = SignInService.login(loginUser);
        assertEquals(loginResp.getStatusCode(), 200);
        ExtentReportUtil.step("PASS", "Dynamic user login successful: " + dynamicUsername);
    }

    @AfterClass
    public void tearDownReport() {
        ExtentReportUtil.flushReport();
    }

    // -------- Utility Methods --------
    private String generateRandomUsername() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateRandomPassword() {
        return "Pwd@" + new Random().nextInt(99999);
    }

    private int generateRandomId() {
        return (int) (System.currentTimeMillis() % 100000);
    }
}
