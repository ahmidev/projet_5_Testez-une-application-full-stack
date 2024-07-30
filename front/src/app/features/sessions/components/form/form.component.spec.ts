import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Router, Routes } from '@angular/router';

import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

import { FormComponent } from './form.component';
import { NgZone } from '@angular/core';
import { Session } from '../../interfaces/session.interface';
import { ListComponent } from '../list/list.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;
  let ngZone: NgZone;

  const routes: Routes = [
    { path: 'sessions', component: ListComponent } 
  ];
  
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([{
      id: 1,
      firstName: 'Margot',
      lastName: 'DELAHAYE',
      createdAt: new Date('2024-07-27T22:23:47'),
      updatedAt: new Date('2024-07-27T22:23:47')
    }]))
  };

  const mockSession = {
    name: 'Yoga',
    description: 'yoga yoga',
    date: '2024-07-28',
    teacher_id: 1
  };

  const createdSession: Session = {
    ...mockSession,
    date: new Date('2024-07-28T00:00:00.000+00:00'),
    id: 1,
    users: [2],
    createdAt: new Date('2024-07-27T22:35:48'),
    updatedAt: new Date('2024-07-28T16:26:24')
  };

  const updatedSession: Session = {
    ...mockSession,
    date: new Date('2024-07-28T00:00:00.000+00:00'),
    id: 1,
    users: [2],
    createdAt: new Date('2024-07-27T22:35:48'),
    updatedAt: new Date('2024-07-28T16:26:24')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a session', () => {
    jest.spyOn(sessionApiService, 'create').mockReturnValue(of(createdSession));
    const navigateSpy = jest.spyOn(router, 'navigate');
    
    component.sessionForm?.controls['name'].setValue(mockSession.name);
    component.sessionForm?.controls['date'].setValue(mockSession.date);
    component.sessionForm?.controls['teacher_id'].setValue(mockSession.teacher_id);
    component.sessionForm?.controls['description'].setValue(mockSession.description);
    
    ngZone.run(() => {
      component.submit();
    });
    
    expect(sessionApiService.create).toHaveBeenCalledWith(mockSession);
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should update a session', async() => {
    component.onUpdate = true;
    component['id'] = '1';
    jest.spyOn(sessionApiService, 'update').mockReturnValue(of(updatedSession));
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.sessionForm?.controls['name'].setValue(mockSession.name);
    component.sessionForm?.controls['date'].setValue(mockSession.date);
    component.sessionForm?.controls['teacher_id'].setValue(mockSession.teacher_id);
    component.sessionForm?.controls['description'].setValue(mockSession.description);

    ngZone.run(() => {
      component.submit();
    });

    expect(sessionApiService.update).toHaveBeenCalledWith('1', mockSession);
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should display error if required fields are missing', () => {
    const nameControl = component.sessionForm?.controls['name'];
    const dateControl = component.sessionForm?.controls['date'];
    const teacherIdControl = component.sessionForm?.controls['teacher_id'];
    const descriptionControl = component.sessionForm?.controls['description'];

    nameControl?.setValue('');
    dateControl?.setValue('');
    teacherIdControl?.setValue('');
    descriptionControl?.setValue('');

    fixture.detectChanges();

    expect(nameControl?.hasError('required')).toBeTruthy();
    expect(dateControl?.hasError('required')).toBeTruthy();
    expect(teacherIdControl?.hasError('required')).toBeTruthy();
    expect(descriptionControl?.hasError('required')).toBeTruthy();

    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBeTruthy();
  });
});
