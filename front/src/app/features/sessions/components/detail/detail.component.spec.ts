import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, flush, TestBed, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Router, Routes } from '@angular/router';
import { NgZone } from '@angular/core';

import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';
import { Session } from '../../interfaces/session.interface';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ListComponent } from '../list/list.component';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;
  let ngZone: NgZone;

  const routes: Routes = [
    { path: 'sessions', component: ListComponent } 
  ];
  const mockSession: Session = {
    id: 1,
    name: 'Yoga',
    description: 'yoga yoga',
    date: new Date('2024-07-28T00:00:00.000+00:00'),
    teacher_id: 1,
    users: [2],
    createdAt: new Date('2024-07-27T22:35:48'),
    updatedAt: new Date('2024-07-28T16:26:24')
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'Margot',
    lastName: 'DELAHAYE',
    createdAt: new Date('2024-07-27T22:23:47'),
    updatedAt: new Date('2024-07-27T22:23:47')
  };

  beforeEach(async () => {
    const mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      declarations: [DetailComponent, ListComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: { detail: jest.fn().mockReturnValue(of(mockTeacher)) } }
      ]
    })
    .compileComponents();

    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display session information', async () => {
    fixture.detectChanges();
    await fixture.whenStable();

    fixture.detectChanges();

    const sessionNameElement = fixture.nativeElement.querySelector('mat-card-title h1');
    expect(sessionNameElement).toBeTruthy();
    expect(sessionNameElement.textContent).toContain(mockSession.name);

    const sessionDescriptionElement = fixture.nativeElement.querySelector('mat-card-content .description');
    expect(sessionDescriptionElement).toBeTruthy();
    expect(sessionDescriptionElement.textContent).toContain(mockSession.description);

    const teacherNameElement = fixture.nativeElement.querySelector('mat-card-subtitle .ml1');
    expect(teacherNameElement).toBeTruthy();
    expect(teacherNameElement.textContent).toContain(mockTeacher.firstName);
    expect(teacherNameElement.textContent).toContain(mockTeacher.lastName.toUpperCase());

    const attendeesElement = fixture.nativeElement.querySelector('mat-card-content .my2 div:first-child .ml1');
    expect(attendeesElement).toBeTruthy();
    expect(attendeesElement.textContent).toContain(`${mockSession.users.length} attendees`);

    const sessionDateElement = fixture.nativeElement.querySelector('mat-card-content .my2 div:last-child .ml1');
    expect(sessionDateElement).toBeTruthy();
    const formattedDate = new Intl.DateTimeFormat('en-US', { month: 'long', day: 'numeric', year: 'numeric' }).format(mockSession.date);
    expect(sessionDateElement.textContent).toContain(formattedDate);
  });

  it('should display delete button if user is admin', async () => {
    fixture.detectChanges();
    await fixture.whenStable();

    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).toBeTruthy();
    expect(deleteButton.textContent).toContain('Delete');
  });

  it('should not display delete button if user is not admin', async () => {
    component.isAdmin = false;
    fixture.detectChanges();
    await fixture.whenStable();

    const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).toBeNull();
  });

  it('should delete a session', async () => {
    jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({}));
    const navigateSpy = jest.spyOn(router, 'navigate');
  
    fixture.detectChanges();
  
    ngZone.run(() => {
      component.delete();
    });
  
    expect(sessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});
