import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { Session } from '../../interfaces/session.interface';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let teacherService: TeacherService;
  let sessionApiService: SessionApiService;
  
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
  }
  
  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'Margot',
    lastName: 'DELAHAYE',
    createdAt: new Date('2024-07-27T22:23:47'),
    updatedAt: new Date('2024-07-27T22:23:47')
  };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: { detail: jest.fn().mockReturnValue(of(mockSession)) } },
        { provide: TeacherService, useValue: { detail: jest.fn().mockReturnValue(of(mockTeacher)) } }
      ]
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
      teacherService = TestBed.inject(TeacherService);
    sessionApiService = TestBed.inject(SessionApiService);
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
  
    // Sélection du nom de la session
    const sessionNameElement = fixture.nativeElement.querySelector('mat-card-title h1');
    expect(sessionNameElement).toBeTruthy();
    expect(sessionNameElement.textContent).toContain(mockSession.name);
  
    // Sélection de la description de la session
    const sessionDescriptionElement = fixture.nativeElement.querySelector('mat-card-content .description');
    expect(sessionDescriptionElement).toBeTruthy();
    expect(sessionDescriptionElement.textContent).toContain(mockSession.description);
  
    // Sélection du nom de l'enseignant dans mat-card-subtitle
    const teacherNameElement = fixture.nativeElement.querySelector('mat-card-subtitle .ml1');
    expect(teacherNameElement).toBeTruthy();
    expect(teacherNameElement.textContent).toContain(mockTeacher.firstName);
    expect(teacherNameElement.textContent).toContain(mockTeacher.lastName.toUpperCase());
  
    // Sélection du nombre de participants
    const attendeesElement = fixture.nativeElement.querySelector('mat-card-content .my2 div:first-child .ml1');
    expect(attendeesElement).toBeTruthy();
    expect(attendeesElement.textContent).toContain(`${mockSession.users.length} attendees`);
  
    // Sélection de la date de la session
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
  
  
});

