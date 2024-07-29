import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionService: SessionService;

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'YOGA',
      description: 'yoga yoga',
      date: new Date('2024-07-28T00:00:00.000+00:00'),
      teacher_id: 1,
      users: [2],
      createdAt: new Date('2024-07-27T22:35:48'),
      updatedAt: new Date('2024-07-28T16:26:24')
    },
    {
      id: 2,
      name: 'PILATES',
      description: 'pilates pilates',
      date: new Date('2024-08-01T00:00:00.000+00:00'),
      teacher_id: 2,
      users: [3],
      createdAt: new Date('2024-07-28T10:00:00'),
      updatedAt: new Date('2024-07-28T18:00:00')
    }
  ];

  const adminUser: SessionInformation = {
    token: 'admin-token',
    type: 'admin',
    id: 1,
    username: 'adminUser',
    firstName: 'Admin',
    lastName: 'User',
    admin: true
  };

  const regularUser: SessionInformation = {
    token: 'user-token',
    type: 'user',
    id: 2,
    username: 'regularUser',
    firstName: 'Regular',
    lastName: 'User',
    admin: false
  };



  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of(mockSessions))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [SessionService,
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the list of sessions', async () => {
    sessionService.logIn(adminUser);
    await fixture.whenStable();
    fixture.detectChanges();

    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(mockSessions.length);

    mockSessions.forEach((session, index) => {
      expect(sessionElements[index].textContent).toContain(session.name);
      expect(sessionElements[index].textContent).toContain(session.description);
    })
  });

  it('should display the Create button if user is an admin', async () => {
    sessionService.logIn(adminUser);
    await fixture.whenStable();
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).not.toBeNull();
  });

  it('should display the Detail and Edit buttons for each session if user is an admin', async () => {
    sessionService.logIn(adminUser);

    await fixture.whenStable();
    fixture.detectChanges();

    const detailButtons = fixture.nativeElement.querySelectorAll('button[ng-reflect-router-link="detail,1"]');
    const editButtons = fixture.nativeElement.querySelectorAll('button[ng-reflect-router-link="update,1"]');

    expect(detailButtons.length).toBe(1);
    expect(editButtons.length).toBe(1);
  });

  it('should not display the Edit button if user is not an admin', () => {
    sessionService.logIn(regularUser);
    fixture.detectChanges();
    const editButtons = fixture.nativeElement.querySelectorAll('button[ng-reflect-router-link="update,1"]');
    expect(editButtons.length).toBe(0);
  });
});
