describe('Register Page', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should create an account successfully', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        message: "User registered successfully!"
      }
    }).as('registerRequest'); 

    cy.get("input[formControlName=firstName]").type("Ahmid");
    cy.get("input[formControlName=lastName]").type("Ait Ouali");
    cy.get("input[formControlName=email]").type("ahmid.aitouali@laposte.net");
    cy.get("input[formControlName=password]").type("testAhmid123!");

    cy.get("button[type=submit]").click();

    cy.wait('@registerRequest');

    cy.url().should('include', 'login');
  });

  it('should have button disabled because of invalid input and display error on submit', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        error: 'aie',
      }
    }).as('registerFailure'); 

    cy.get("input[formControlName=firstName]").type("Ahmid");
    cy.get("input[formControlName=lastName]").type("Ait Ouali");

    cy.get("button[type=submit]").should("be.disabled");

    cy.get("input[formControlName=email]").type("Ahmid");
    cy.get("input[formControlName=password]").type("testAhmid123!");

    cy.get("button[type=submit]").should("be.disabled");

    cy.get("input[formControlName=email]").clear().type("ahmid.aitouali@laposte.net");

    cy.get("button[type=submit]").should("be.enabled");

    cy.get("button[type=submit]").click();

    cy.wait('@registerFailure');

    cy.get(".error").should("be.visible").and("contain", "An error occurred");
  });
});
