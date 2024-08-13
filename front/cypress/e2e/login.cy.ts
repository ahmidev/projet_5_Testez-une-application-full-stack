describe('Login Page Tests', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should redirect to sessions upon successful login', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MjI5Nzc2MjksImV4cCI6MTcyMzA2NDAyOX0.Pi75FG38BqFQyb3yLLiD_yCp2D1ZIlEoRcAnhYVpx6XcFfngn3nrpSd_wzz9jZySJMK381KREBMS4MPCNmBegQ",
        type: "Bearer",
        id: 1,
        username: "ahmid.aitouali@laposte.net",
        firstName: "Ahmid",
        lastName: "Ait Ouali",
        admin: true
      }
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "Session 1",
          date: "2024-07-10T00:00:00.000+00:00",
          teacher_id: 1,
          description: "First session",
          users: [2],
          createdAt: "2024-07-29T19:24:57",
          updatedAt: "2024-07-29T19:24:58"
        },
        {
          id: 2,
          name: "Session 2",
          date: "2024-08-16T00:00:00.000+00:00",
          teacher_id: 1,
          description: "Second session",
          users: [],
          createdAt: "2024-08-01T19:33:43",
          updatedAt: "2024-08-01T19:33:43"
        }
      ]
    }).as('sessionRequest');

    cy.get("button[mat-raised-button]").should("be.disabled");

    cy.get("input[formControlName=email]").type("ahmid.aitouali@laposte.net");
    cy.get("input[formControlName=password]").type("testAhmid123!");

    cy.get("button[mat-raised-button]").should("be.enabled");

    cy.get("button[mat-raised-button]").click();

    cy.wait('@loginRequest');

    cy.wait('@sessionRequest');

    cy.url().should('include', '/sessions');
  });

  it('should display an error on login failure', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Invalid credentials'
      }
    }).as('loginError');

    cy.get("button[mat-raised-button]").should("be.disabled");

    cy.get("input[formControlName=email]").type("ahmid.aitouali@laposte.net");
    cy.get("input[formControlName=password]").type("123toto");

    cy.get("button[mat-raised-button]").click();

    cy.wait('@loginError');

    cy.get(".error").should("be.visible").and("contain", "An error occurred");
  });
});
