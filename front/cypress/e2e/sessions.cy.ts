describe('Sessions page test', () => {
  const sessions = [
    {
      id: 1,
      name: "YOGA",
      date: "2024-07-10T00:00:00.000+00:00",
      teacher_id: 1,
      description: "test YOGA",
      users: [2],
      createdAt: "2024-07-29T19:24:57",
      updatedAt: "2024-07-29T19:24:58"
    },
    {
      id: 2,
      name: "ZEN",
      date: "2024-08-16T00:00:00.000+00:00",
      teacher_id: 1,
      description: "ZEN test",
      users: [],
      createdAt: "2024-08-01T19:33:43",
      updatedAt: "2024-08-01T19:33:43"
    }
  ];

  it('should display sessions as user', () => {
    /**
     * Connexion en tant que user
     */
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
    
    cy.intercept("POST", "/api/auth/login", user).as('loginRequest');

    cy.intercept('GET', `/api/user/${user.id}`, { ...user }).as('getUser');

    cy.intercept('GET', "/api/session", [...sessions]).as('getSessions');

    cy.intercept('GET', `/api/session/${sessions[0].id}`, [...sessions][0]).as('getSessionDetail');

    cy.intercept('POST', `/api/session/${sessions[0].id}/participate/${user.id}`, {
      statusCode: 200,
      body: null,
    }).as('postParticipation');

    cy.intercept("GET", "/api/teacher/1", {
      statusCode: 200,
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2024-07-29T14:07:32",
        updatedAt: "2024-07-29T14:07:32"
      }
    }).as('getTeacher');

    cy.visit('/login');

    cy.get("input[formControlName=email]").type("ahmid.aitouali@laposte.net");
    cy.get("input[formControlName=password]").type("testAhmid123!");
    cy.get("button[mat-raised-button]").click();

    cy.wait('@loginRequest');

    /**
     * test de l'affichage
     */
    cy.get('.m0').should('contain', 'Rentals available');
    cy.get('mat-card-title').should('contain', 'YOGA');
    cy.get('mat-card-subtitle').should('contain', 'Session on July 10, 2024');
    cy.get('p').eq(0).should('contain', 'test YOGA');

    cy.contains('button', 'Detail').click();

    cy.intercept('GET', `/api/session/${sessions[0].id}`, {
      id: 1,
      name: "YOGA",
      date: "2024-07-10T00:00:00.000+00:00",
      teacher_id: 1,
      description: "test YOGA",
      users: [2, 1],
      createdAt: "2024-07-29T19:24:57",
      updatedAt: "2024-08-07T14:06:17"
    }).as('getUpdatedSessionDetail');

    cy.get('h1').should('contain', 'Yoga');
    cy.contains('button', 'Participate').should('exist');
    cy.contains('button', 'Participate').click(); 
    cy.contains('span', 'Do not participate').should('exist');
    cy.contains('span', 'attendees').should('contain', '2 attendees');

    cy.intercept('DELETE', `/api/session/${sessions[0].id}/participate/${user.id}`, {
      statusCode: 200
    }).as('deleteParticipation');

    cy.intercept('GET', `/api/session/${sessions[0].id}`, {
      id: 1,
      name: "YOGA",
      date: "2024-07-10T00:00:00.000+00:00",
      teacher_id: 1,
      description: "POUR RESTE ZEN ",
      users: [2],
      createdAt: "2024-07-29T19:24:57",
      updatedAt: "2024-08-07T14:06:17"
    }).as('getFinalSessionDetail');

    cy.contains('button', 'Do not participate').click();
    cy.contains('span', 'Participate').should('exist');
  });

  it('should display sessions as admin', () => {
    /**
     * Connexion en tant qu'admin
     */
    const user = {
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

    cy.intercept("POST", "/api/auth/login", user).as('loginRequest');

    cy.intercept('GET', `/api/user/${user.id}`, { ...user }).as('getUser');

    cy.intercept('GET', "/api/session", [...sessions]).as('getSessions');

    cy.intercept('GET', `/api/session/${sessions[0].id}`, [...sessions][0]).as('getSessionDetail');

    cy.intercept('POST', `/api/session/1/participate/1`, user).as('postParticipation');

    cy.intercept("GET", "/api/teacher/1", {
      statusCode: 200,
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: "2024-07-29T14:07:32",
        updatedAt: "2024-07-29T14:07:32"
      }
    }).as('getTeacher');

    cy.visit('/login');

    cy.get("input[formControlName=email]").type("yoga@studio.com");
    cy.get("input[formControlName=password]").type("adminPassword123!");
    cy.get("button[mat-raised-button]").click();

    cy.wait('@loginRequest');

    /**
     * test de l'affichage
     */
    cy.get('.m0').should('contain', 'Rentals available');
    cy.get('mat-card-title').should('contain', 'YOGA');
    cy.get('mat-card-subtitle').should('contain', 'Session on July 10, 2024');
    cy.get('p').eq(0).should('contain', 'test YOGA');
    cy.contains('span', 'Create').should('exist');
    cy.contains('span', 'Edit').should('exist');

    cy.get('button').eq(1).click(); 

    cy.wait('@getSessionDetail');

    cy.get('h1').should('contain', 'Yoga');
    cy.contains('span', 'Delete').should('exist');

    cy.get('button').eq(0).click(); 
    cy.wait(100);

    cy.intercept("GET", "/api/teacher", {
      statusCode: 200,
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2024-07-29T14:07:32",
          updatedAt: "2024-07-29T14:07:32"
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2024-07-29T14:07:32",
          updatedAt: "2024-07-29T14:07:32"
        }
      ]
    }).as('getTeachers');

    cy.contains('span', 'Create').click(); 
    
    cy.wait(100);

    cy.contains('button', 'Save').should('be.disabled');
    cy.get('input[formControlName=name]').type('YOGA');
    cy.get('input[formControlName=date]').type('2024-07-25');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type('pour rester zen pdt le projet');
    cy.contains('button', 'Save').should('be.enabled');
  });
});
