package com.bookstore.stepdefs;

import com.bookStore.base.Book;
import com.bookStore.base.User;
import com.bookStore.service.BookService;
import com.bookStore.service.SignInService;
import com.bookStore.service.SignUpService;
import com.bookStore.utils.ExtentReportUtil;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class BookManagementTestNG {

    private Response response;
    private Book book;
    private int createdBookId;
    private String accessToken;

    private final String email = "bookflow_user_" + System.currentTimeMillis() + "@mail.com";
    private final String password = "Book@123";

    @BeforeClass
    public void signUpAndLogin() {
        ExtentReportUtil.initReport();
        ExtentReportUtil.createTest("Book Management API Suite");

        User user = new User((int) (System.currentTimeMillis() % 100000), email, password);

        Response signUpResp = SignUpService.signUp(user);
        assertEquals(signUpResp.getStatusCode(), 200);
        ExtentReportUtil.logValidation("User Signup", 200, signUpResp.getStatusCode(), "User created successfully", signUpResp.getBody().asString(), true);

        Response loginResp = SignInService.login(user);
        accessToken = JsonPath.from(loginResp.getBody().asString()).getString("access_token");
        assertNotNull(accessToken);
        assertEquals(loginResp.getStatusCode(), 200);
        ExtentReportUtil.logValidation("User Login", 200, loginResp.getStatusCode(), "Login successful", loginResp.getBody().asString(), true);
    }

    @Test(priority = 1)
    public void testCreateBook() {
        book = new Book("The TestNG Book", "Test Author", 2025, "A complete book about testing with TestNG.");
        ExtentReportUtil.step("INFO", "Prepared book payload: " + book);

        response = BookService.createBook(book, accessToken);
        int status = response.getStatusCode();
        String body = response.getBody().asString();

        ExtentReportUtil.step("INFO", "Book creation request response:\nStatus: " + status + "\nBody: " + body);

        assertEquals(status, 200);
        createdBookId = JsonPath.from(body).getInt("id");
        String returnedName = JsonPath.from(body).getString("name");

        boolean passed = status == 200 && returnedName.equals(book.getName());
        ExtentReportUtil.logValidation("Book Creation", 200, status, book.getName(), returnedName, passed);

        assertEquals(returnedName, book.getName());
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBook")
    public void testFetchBookById() {
        response = BookService.getBookById(createdBookId, accessToken);
        int actualCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        String fetchedName = JsonPath.from(responseBody).getString("name");

        ExtentReportUtil.step("INFO", "Fetching book by ID: " + createdBookId + " → Body: " + responseBody);

        boolean passed = actualCode == 200 && book.getName().equals(fetchedName);
        ExtentReportUtil.logValidation("Fetch Book by ID", 200, actualCode, book.getName(), fetchedName, passed);

        assertEquals(actualCode, 200);
        assertEquals(fetchedName, book.getName());
    }

    @AfterClass
    public void teardown() {
        ExtentReportUtil.flushReport();
    }
}
