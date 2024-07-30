import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from 'src/app/services/user.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockUserService: any;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: true,
    password:'null',
    createdAt: new Date('2024-01-01T00:00:00.000Z'),
    updatedAt: new Date('2024-01-02T00:00:00.000Z')
  };
  
  beforeEach(async () => {
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display user information', () => {
    fixture.detectChanges();

    const compiled = fixture.nativeElement;

    const nameElement = compiled.querySelector('mat-card-content p:first-child');
    expect(nameElement.textContent).toContain('Name: John DOE');

    const emailElement = compiled.querySelector('mat-card-content p:nth-child(2)');
    expect(emailElement.textContent).toContain('Email: john.doe@example.com');

    const adminElement = compiled.querySelector('mat-card-content p.my2');
    expect(adminElement.textContent).toContain('You are admin');

    const createdAtElement = compiled.querySelector('mat-card-content .p2.w100 p:first-child');
    expect(createdAtElement.textContent).toContain('Create at:  January 1, 2024');

    const updatedAtElement = compiled.querySelector('mat-card-content .p2.w100 p:last-child');
    expect(updatedAtElement.textContent).toContain('Last update:  January 2, 2024');
  });
});
