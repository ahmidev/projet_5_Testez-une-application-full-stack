describe('Account Page Tests', () => {
  beforeEach(() => {
    cy.intercept("GET", "/api/session", []).as('getSession');
    
    cy.visit("/login");

    cy.get("input[formControlName=email]").type("ahmid.aitouali@laposte.net");
    cy.get("input[formControlName=password]").type("testAhmid123!");
  });

  it('should display a user and allow account deletion', () => {
    const user = {
      token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhaG1pZC5haXRvdWFsaUBsYXBvc3RlLm5ldCIsImlhdCI6MTcyMjk3NzYyOSwiZXhwIjoxNzIzMDY0MDI5fQ.Pi75FG38BqFQyb3yLLiD_yCp2D1ZIlEoRcAnhYVpx6XcFfngn3nrpSd_wzz9jZySJMK381KREBMS4MPCNmBegQ",
      type: 'Bearer',
      id: 1,
      email: 'ahmid.aitouali@laposte.net',
      firstName: 'Ahmid',
      lastName: 'Ait Ouali',
      admin: false,
      createdAt: '2024-04-15T19:37:41',
      updatedAt: '2024-04-15T15:33:42',
    };

    cy.intercept("POST", "/api/auth/login", {
      statusCode: 200,
      body: user
    }).as('loginRequest');

    cy.intercept("GET", `/api/user/${user.id}`, {
      statusCode: 200,
      body: user
    }).as('getUser');

    cy.get("button[mat-raised-button]").click();
    
    cy.wait('@loginRequest');

    cy.get("span").contains('Account').click();

    cy.wait('@getUser');

    cy.url().should('include', '/me');

    cy.get('p').eq(0).should("contain", "Name: Ahmid AIT OUALI");
    cy.get('p').eq(1).should("contain", "Email: ahmid.aitouali@laposte.net");
    cy.get('p').eq(3).should("contain", "April 15, 2024");
    cy.get('p').eq(4).should("contain", "April 15, 2024");

    cy.get('button[mat-icon-button]').click();
    cy.url().should('not.include', '/me'); 

    cy.get("span").contains('Account').click();
    cy.wait('@getUser');

    cy.intercept('DELETE', `/api/user/${user.id}`, {
      statusCode: 200
    }).as('deleteUser');

    cy.contains('button', 'delete', { timeout: 10000 }).should('be.visible').click();

    cy.wait('@deleteUser');
    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
    cy.url().should('include', '/'); 
  });

  it('should display an admin', () => {
    const adminUser = {
      token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhaG1pZC5haXRvdWFsaUBsYXBvc3RlLm5ldCIsImlhdCI6MTcyMjk3NzYyOSwiZXhwIjoxNzIzMDY0MDI5fQ.Pi75FG38BqFQyb3yLLiD_yCp2D1ZIlEoRcAnhYVpx6XcFfngn3nrpSd_wzz9jZySJMK381KREBMS4MPCNmBegQ",
      type: 'Bearer',
      id: 1,
      email: 'yoga@studio.com',
      firstName: 'Admin',
      lastName: 'Admin',
      admin: true,
      createdAt: '2024-04-15T19:37:41',
      updatedAt: '2024-04-15T15:33:42',
    };

    cy.get("input[formControlName=email]").clear().type("yoga@studio.com");
    cy.get("input[formControlName=password]").clear().type("adminPassword123!");

    cy.intercept("POST", "/api/auth/login", {
      statusCode: 200,
      body: adminUser
    }).as('loginRequest');

    cy.intercept('GET', `/api/user/${adminUser.id}`, {
      statusCode: 200,
      body: adminUser
    }).as('getUser');

    cy.get("button[mat-raised-button]").click();

    cy.wait('@loginRequest');
    
    cy.get("span").contains('Account').click();

    cy.wait('@getUser');

    cy.url().should('include', '/me');

    cy.get('p').eq(0).should("contain", "Name: Admin ADMIN");
    cy.get('p').eq(1).should("contain", "Email: yoga@studio.com");
    cy.get('p').eq(2).should("contain", "You are admin");
    cy.get('p').eq(3).should("contain", "April 15, 2024");
    cy.get('p').eq(4).should("contain", "April 15, 2024");

    cy.get('button').contains('delete').should('not.exist');
  });

  
});
