package com.example;

import com.example.pages.WebTablesPage;
import org.junit.jupiter.api.Test;

public class WebTablesTest extends BaseTest {

    @Test
    public void testAddAndSearchRecord() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String age = String.valueOf(faker.number().numberBetween(18, 65));
        String salary = String.valueOf(faker.number().numberBetween(1000, 10000));
        String department = faker.commerce().department();

        WebTablesPage webTablesPage = new WebTablesPage(page);
        webTablesPage
                .navigate()
                .clickAdd()
                .fillRegistrationForm(firstName, lastName, email, age, salary, department)
                .submitForm()
                .search(email)
                .softAssert(
                        assertion -> assertion.assertRecordVisible(email),
                        assertion -> assertion.assertRecordData(email, firstName, lastName, age, salary, department)
                );
    }

    @Test
    public void testEditRecord() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String age = "30";
        String salary = "5000";
        String department = "Engineering";

        String newFirstName = faker.name().firstName();

        WebTablesPage webTablesPage = new WebTablesPage(page);
        webTablesPage
                .navigate()
                .clickAdd()
                .fillRegistrationForm(firstName, lastName, email, age, salary, department)
                .submitForm()
                .editRecord(email)
                .fillRegistrationForm(newFirstName, lastName, email, age, salary, department)
                .submitForm()
                .search(email)
                .softAssert(
                        assertion -> assertion.assertRecordData(email, newFirstName, lastName, age, salary, department)
                );
    }

    @Test
    public void testDeleteRecord() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String age = "25";
        String salary = "3000";
        String department = "Sales";

        WebTablesPage webTablesPage = new WebTablesPage(page);
        webTablesPage
                .navigate()
                .clickAdd()
                .fillRegistrationForm(firstName, lastName, email, age, salary, department)
                .submitForm()
                .search(email)
                .deleteRecord(email)
                .softAssert(
                        assertion -> assertion.assertRecordNotVisible(email)
                );
    }
}
